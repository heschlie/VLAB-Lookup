package com.heschlie.vlablookup.vlab;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get JSON
    private static final String urlPrefix = "http://json.lab.nbttech.com/v1/resources/names/";
    public String url;
    private EditText editText;
    public String lastDevice;

    // JSON Node names
    private static final String TAG_NAME = "name";
    private static final String TAG_TERMSRV = "termsrv";
    private static final String TAG_TERMSRV_PORT = "termsrv_port";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_ALTITUDE = "location_altitude";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_RPB = "rpb";
    private static final String TAG_RPB_PLUG = "rpb_plug";

    // list of Hashmap for Listview
    ArrayList<HashMap<String, String>> deviceList;

    //Hashmap for interfaces
    ArrayList<HashMap<String, HashMap<String, String>>> interfacesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceList = new ArrayList<HashMap<String, String>>();
        interfacesList = new ArrayList<HashMap<String, HashMap<String, String>>>();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(getApplicationContext(), SingleDeviceActivity.class);
                in.putExtra("device", deviceList.get(i));
                if (interfacesList.get(i) != null){
                    Log.d("Debug: ", "asdf");
                    in.putExtra("interfaces", interfacesList.get(i));
                }

                startActivity(in);
            }
        });

        editText = (EditText) findViewById(R.id.res_entry);

        // adding hooks for Go! button
        Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastDevice = editText.getText().toString();
                url = urlPrefix + lastDevice;

                if (!lastDevice.equals("")) {
                    new GetDevices().execute();
                    editText.setText("");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetDevices extends AsyncTask<Void, Void, Void> {
        AlertDialog.Builder alertDialog;
        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(MainActivity.this);
            // showing dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            //Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                // Error if no device is found
                if (jsonStr.equals("404")) {
                    error = true;
                } else {
                    try {
                        JSONObject d = new JSONObject(jsonStr);
                        HashMap<String, String> device = getDeviceInfo(d);

                        // adding device to list
                        deviceList.add(0, device);
                        error = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        // Grabs interesting info form JSONObject
        private HashMap<String, String> getDeviceInfo(JSONObject d) throws JSONException {

            String name = d.getString(TAG_NAME);
            String location = d.getString(TAG_LOCATION);
            String altitude = d.getString(TAG_ALTITUDE);
            String termsrv = d.getString(TAG_TERMSRV);
            String termsrvPort = d.getString(TAG_TERMSRV_PORT);
            String rpb = d.getString(TAG_RPB);
            String rpbPlug = d.getString(TAG_RPB_PLUG);
            String owner = d.getString(TAG_OWNER);

            // tmp map for single device
            HashMap<String, String> device = new HashMap<String, String>();

            // adding each child node to map
            device.put(TAG_NAME, name);
            device.put(TAG_LOCATION, location);
            device.put(TAG_ALTITUDE, altitude);
            device.put(TAG_TERMSRV, termsrv);
            device.put(TAG_TERMSRV_PORT, termsrvPort);
            device.put(TAG_RPB, rpb);
            device.put(TAG_RPB_PLUG, rpbPlug);
            device.put(TAG_OWNER, owner);

            if (d.has("interfaces")) {
                Log.d("Debug: ", "loading interfaces");
                JSONObject i = d.getJSONObject("interfaces");
                interfacesList.add(0, getInterfaces(i));
            } else {
                interfacesList.add(0, null);
            }

            return device;
        }

        // We have to loop through these in a more pragmatic way as we don't know the names of each
        // field for absolute certainty
        private HashMap<String, HashMap<String, String>> getInterfaces(JSONObject ifaces) throws JSONException {
            HashMap<String, HashMap<String, String>> interfaces = new HashMap<String, HashMap<String, String>>();
            HashMap<String, String> ifaceFields = new HashMap<String, String>();

            // Loop through interfaces
             for (Iterator<String> ifacesIter = ifaces.keys(); ifacesIter.hasNext();){
                 String name = ifacesIter.next();
                 JSONObject i = ifaces.getJSONObject(name);
                 // Loop through interface fields.
                 for (Iterator<String> fIter = i.keys(); fIter.hasNext();){
                     String field = fIter.next();
                     String value = i.getString(field);
                     ifaceFields.put(field, value);
                 }
                 interfaces.put(name, ifaceFields);
            }

            return interfaces;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (error) {
                pDialog.dismiss();
                alertDialog.setTitle("Resource not found!");
                alertDialog
                        .setMessage(lastDevice + " was not found VLAB")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }

            // update parsed json into listview
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, deviceList, R.layout.list_item,
                    new String[] { TAG_NAME, TAG_LOCATION, TAG_ALTITUDE },
                    new int[] { R.id.name, R.id.location, R.id.altitude}
            );

            setListAdapter(adapter);
        }
    }
}
