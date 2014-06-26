package com.heschlie.vlablookup.vlab;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//    private class GetDevices extends AsyncTask<String, Void, Integer> {
//        AlertDialog.Builder alertDialog;
//        boolean error;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            alertDialog = new AlertDialog.Builder(MainActivity.this);
//            // showing dialog
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Please Wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected Integer doInBackground(String... urls) {
//
//            // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();
//
//            int deviceCount = urls.length;
//
//            for (String url : urls) {
//                // Making a request to url and getting response
//                String jsonStr = sh.makeServiceCall(urlPrefix + url, ServiceHandler.GET);
//
//                //Log.d("Response: ", "> " + jsonStr);
//
//                if (jsonStr != null) {
//                    // Error if no device is found
//                    if (jsonStr.equals("error")) {
//                        error = true;
//                    } else {
//                        try {
//                            JSONObject d = new JSONObject(jsonStr);
//                            HashMap<String, String> device = getDeviceInfo(d);
//
//                            // Checking if entry already exists, if so remove it
//                            for (int i = 0; i < deviceList.size(); i++) {
//                                HashMap<String, String> dev = deviceList.get(i);
//                                if (dev.get(TAG_NAME).equals(device.get(TAG_NAME))) {
//                                    deviceList.remove(i);
//                                }
//                            }
//                            // adding device to list
//                            deviceList.add(0, device);
//                            error = false;
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                } else {
//                    Log.e("ServiceHandler", "Couldn't get any data from the url");
//                }
//            }
//            return deviceCount;
//        }
//
//        // Grabs interesting info form JSONObject
//        private HashMap<String, String> getDeviceInfo(JSONObject d) throws JSONException {
//            String name = "";
//            String location = "";
//            String altitude = "";
//            String termsrv = "";
//            String termsrvPort = "";
//            String rpb = "";
//            String rpbPlug = "";
//            String owner = "";
//
//            // Checking if the fields exist in the JSON
//            if (d.has(TAG_NAME))
//                name = d.getString(TAG_NAME);
//            if (d.has(TAG_LOCATION))
//                location = d.getString(TAG_LOCATION);
//            if (d.has(TAG_ALTITUDE))
//                altitude = d.getString(TAG_ALTITUDE);
//            if (d.has(TAG_TERMSRV))
//                termsrv = d.getString(TAG_TERMSRV);
//            if (d.has(TAG_TERMSRV_PORT))
//                termsrvPort = d.getString(TAG_TERMSRV_PORT);
//            if (d.has(TAG_RPB))
//                rpb = d.getString(TAG_RPB);
//            if (d.has(TAG_RPB_PLUG))
//                rpbPlug = d.getString(TAG_RPB_PLUG);
//            if (d.has(TAG_OWNER))
//                owner = d.getString(TAG_OWNER);
//
//            // tmp map for single device
//            HashMap<String, String> device = new HashMap<String, String>();
//
//            // adding each child node to map
//            device.put(TAG_NAME, name);
//            device.put(TAG_LOCATION, location);
//            device.put(TAG_ALTITUDE, altitude);
//            device.put(TAG_TERMSRV, termsrv);
//            device.put(TAG_TERMSRV_PORT, termsrvPort);
//            device.put(TAG_RPB, rpb);
//            device.put(TAG_RPB_PLUG, rpbPlug);
//            device.put(TAG_OWNER, owner);
//
//            if (d.has("interfaces")) {
//                Log.d("Debug: ", "loading interfaces");
//                JSONObject i = d.getJSONObject("interfaces");
//                interfacesList.add(0, getInterfaces(i));
//            } else {
//                interfacesList.add(0, null);
//            }
//
//            return device;
//        }
//
//        // We have to loop through these in a more pragmatic way as we don't know the names of each
//        // field for absolute certainty
//        private HashMap<String, HashMap<String, String>> getInterfaces(JSONObject ifaces) throws JSONException {
//
//            HashMap<String, HashMap<String, String>> interfaces = new HashMap<String, HashMap<String, String>>();
//
//            // Loop through interfaces
//             for (Iterator<String> ifacesIter = ifaces.keys(); ifacesIter.hasNext();){
//                 String name = ifacesIter.next();
//                 //System.out.println("Name: " + name);
//                 JSONObject i = ifaces.getJSONObject(name);
//                 HashMap<String, String> ifaceFields = new HashMap<String, String>();
//                 // Loop through interface fields.
//                 for (Iterator<String> fIter = i.keys(); fIter.hasNext();){
//                     String field = fIter.next();
//                     String value = i.getString(field);
//                     //System.out.println(field + ": " + value);
//                     ifaceFields.put(field, value);
//                 }
//
//                 interfaces.put(name, ifaceFields);
//            }
//
//            return interfaces;
//        }
//
//        @Override
//        protected void onPostExecute(Integer deviceCount) {
//            super.onPostExecute(deviceCount);
//            // dismiss the progress dialog
//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
//
//            if (error) {
//                pDialog.dismiss();
//                alertDialog.setTitle("Resource not found!");
//                alertDialog
//                        .setMessage(textInput + " was not found VLAB or server could not be contacted")
//                        .setCancelable(false)
//                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                AlertDialog alert = alertDialog.create();
//                alert.show();
//            } else {
//                // update parsed json into listview
//                ListAdapter adapter = new SimpleAdapter(
//                        MainActivity.this, deviceList, R.layout.list_item,
//                        new String[] { TAG_NAME, TAG_LOCATION, TAG_ALTITUDE },
//                        new int[] { R.id.name, R.id.location, R.id.altitude}
//                );
//
//                setListAdapter(adapter);
//
//                if (deviceCount == 1) {
//                    Intent in = new Intent(getApplicationContext(), SingleDeviceActivity.class);
//                    in.putExtra("device", deviceList.get(0));
//                    if (interfacesList.get(0) != null){
//                        in.putExtra("interfaces", interfacesList.get(0));
//                    }
//
//                    startActivity(in);
//                }
//            }
//
//
//        }
//    }
}
