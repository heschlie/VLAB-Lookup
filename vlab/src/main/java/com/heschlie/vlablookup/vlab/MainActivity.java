package com.heschlie.vlablookup.vlab;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get JSON
    private static String url = "http://json.lab.nbttech.com/v1/resources/names/oak-sh100";

    // JSON Node names
    private static final String TAG_NAME = "name";
    private static final String TAG_TERMSRV = "termsrv";
    private static final String TAG_TERMSRV_PORT = "termsrv_port";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_ALTITUDE = "location_altitude";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_RPB = "rpb";
    private static final String TAG_RPB_PLUG = "rpb_plug";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for Listview
    ArrayList<HashMap<String, String>> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        new GetContacts().execute();
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

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject d = new JSONObject(jsonStr);

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

                    // adding device to list
                    deviceList.add(device);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
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
