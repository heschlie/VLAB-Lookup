package com.heschlie.vlablookup.vlab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by heschlie on 6/5/2014.
 */
public class MainFragActivity extends ListFragment {
    private ProgressDialog pDialog;
    private SingleFragmentData mCallBack;
    // URL to get JSON
    private static final String urlPrefix = "http://json.lab.nbttech.com/v1/resources/names/";
    //public String url;
    private EditText editText;
    public String textInput;

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


    public interface SingleFragmentData {
        public void sendData(int destFrag, HashMap<String, String> device, HashMap<String,
                             HashMap<String, String>> interfaces);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceList = new ArrayList<HashMap<String, String>>();
        interfacesList = new ArrayList<HashMap<String, HashMap<String,String>>>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (SingleFragmentData) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SingleFragmentData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_list_fragment, container, false);

        editText = (EditText) view.findViewById(R.id.res_entry);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                textInput = editText.getText().toString();

                if (!textInput.equals("")) {
                    String[] split = textInput.split("\\s+");
                    new GetDevices().execute(split);

                    editText.setText("");
                }
                return true;
            }
        });

        // adding hooks for Go! button
        Button button = (Button) view.findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInput = editText.getText().toString();

                if (!textInput.equals("")) {
                    String[] split = textInput.split("\\s+");
                    new GetDevices().execute(split);

                    editText.setText("");
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            deviceList = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("deviceList");
            interfacesList = (ArrayList<HashMap<String, HashMap<String, String>>>) savedInstanceState.getSerializable("interfaces");
        }
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (interfacesList.get(0) != null)
                    mCallBack.sendData(0, deviceList.get(0), interfacesList.get(0));
                else
                    mCallBack.sendData(0, deviceList.get(0), null);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("deviceList", deviceList);
        outState.putSerializable("interfaces", interfacesList);
    }

    private class GetDevices extends AsyncTask<String, Void, Integer> {
        AlertDialog.Builder alertDialog;
        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(getActivity());
            // showing dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... urls) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            int deviceCount = urls.length;

            for (String url : urls) {
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(urlPrefix + url, ServiceHandler.GET);

                //Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {
                    // Error if no device is found
                    if (jsonStr.equals("error")) {
                        error = true;
                    } else {
                        try {
                            JSONObject d = new JSONObject(jsonStr);
                            HashMap<String, String> device = getDeviceInfo(d);

                            // Checking if entry already exists, if so remove it
                            for (int i = 0; i < deviceList.size(); i++) {
                                HashMap<String, String> dev = deviceList.get(i);
                                if (dev.get(TAG_NAME).equals(device.get(TAG_NAME))) {
                                    deviceList.remove(i);
                                }
                            }
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
            }
            return deviceCount;
        }

        // Grabs interesting info form JSONObject
        private HashMap<String, String> getDeviceInfo(JSONObject d) throws JSONException {
            String name = "";
            String location = "";
            String altitude = "";
            String termsrv = "";
            String termsrvPort = "";
            String rpb = "";
            String rpbPlug = "";
            String owner = "";

            // Checking if the fields exist in the JSON
            if (d.has(TAG_NAME))
                name = d.getString(TAG_NAME);
            if (d.has(TAG_LOCATION))
                location = d.getString(TAG_LOCATION);
            if (d.has(TAG_ALTITUDE))
                altitude = d.getString(TAG_ALTITUDE);
            if (d.has(TAG_TERMSRV))
                termsrv = d.getString(TAG_TERMSRV);
            if (d.has(TAG_TERMSRV_PORT))
                termsrvPort = d.getString(TAG_TERMSRV_PORT);
            if (d.has(TAG_RPB))
                rpb = d.getString(TAG_RPB);
            if (d.has(TAG_RPB_PLUG))
                rpbPlug = d.getString(TAG_RPB_PLUG);
            if (d.has(TAG_OWNER))
                owner = d.getString(TAG_OWNER);

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

            // Loop through interfaces
            for (Iterator<String> ifacesIter = ifaces.keys(); ifacesIter.hasNext();){
                String name = ifacesIter.next();
                //System.out.println("Name: " + name);
                JSONObject i = ifaces.getJSONObject(name);
                HashMap<String, String> ifaceFields = new HashMap<String, String>();
                // Loop through interface fields.
                for (Iterator<String> fIter = i.keys(); fIter.hasNext();){
                    String field = fIter.next();
                    String value = i.getString(field);
                    //System.out.println(field + ": " + value);
                    ifaceFields.put(field, value);
                }

                interfaces.put(name, ifaceFields);
            }

            return interfaces;
        }

        @Override
        protected void onPostExecute(Integer deviceCount) {
            super.onPostExecute(deviceCount);
            // dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (error) {
                pDialog.dismiss();
                alertDialog.setTitle("Resource not found!");
                alertDialog
                        .setMessage(textInput + " was not found VLAB or server could not be contacted")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alert = alertDialog.create();
                alert.show();
            } else {
                // update parsed json into listview
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), deviceList, R.layout.list_item,
                        new String[] { TAG_NAME, TAG_LOCATION, TAG_ALTITUDE },
                        new int[] { R.id.name, R.id.location, R.id.altitude}
                );

                setListAdapter(adapter);

                if (deviceCount == 1) {
                    if (interfacesList.get(0) != null)
                        mCallBack.sendData(0, deviceList.get(0), interfacesList.get(0));
                    else
                        mCallBack.sendData(0, deviceList.get(0), null);
//                    Intent in = new Intent(getActivity(), SingleDeviceActivity.class);
//                    in.putExtra("device", deviceList.get(0));
//                    if (interfacesList.get(0) != null){
//                        in.putExtra("interfaces", interfacesList.get(0));
//                    }
//
//                    startActivity(in);
                }
            }


        }
    }
}
