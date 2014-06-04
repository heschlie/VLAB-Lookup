package com.heschlie.vlablookup.vlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Brought to you by Stephen Schlie
 */
public class SingleDeviceActivity extends Activity{

    private HashMap<String, HashMap<String, String>> ifaces;
    private String ifaceNames;

    // JSON Node names
    private static final String TAG_NAME = "name";
    private static final String TAG_TERMSRV = "termsrv";
    private static final String TAG_TERMSRV_PORT = "termsrv_port";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_ALTITUDE = "location_altitude";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_RPB = "rpb";
    private static final String TAG_RPB_PLUG = "rpb_plug";
    private static final String TAG_IFACE = "interfaces";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_device);

        ifaceNames = "";

        // get our intent
        Intent in = getIntent();

        // get the hashmap containing the device info
        HashMap<String, String> device = (HashMap<String, String>) in.getSerializableExtra("device");

        // Check for the Hashmap for interfaces
        if (in.hasExtra("interfaces")) {

            ifaces = (HashMap<String, HashMap<String, String>>) in.getSerializableExtra("interfaces");

            // Putting all the interface names into a String for the textview
            for (String key : ifaces.keySet()) {
                if (ifaceNames.equals("")) {
                    ifaceNames += key;
                } else {
                    ifaceNames += "\n" + key;
                }
            }
        }

        String name = device.get(TAG_NAME);
        String termsrv = device.get(TAG_TERMSRV);
        String termsrvPort = device.get(TAG_TERMSRV_PORT);
        String location = device.get(TAG_LOCATION);
        String altitude = device.get(TAG_ALTITUDE);
        String rpb = device.get(TAG_RPB);
        String rpbPlug = device.get(TAG_RPB_PLUG);
        String owner = device.get(TAG_OWNER);

        TextView lblName = (TextView) findViewById(R.id.device_name);
        TextView lblLocation = (TextView) findViewById(R.id.device_location);
        TextView lblAltitude = (TextView) findViewById(R.id.device_altitude);
        TextView lblTermsrv = (TextView) findViewById(R.id.device_termsrv);
        TextView lblRpb = (TextView) findViewById(R.id.device_rpb);
        TextView lblOwner = (TextView) findViewById(R.id.device_owner);
        TextView lblIfaces = (TextView) findViewById(R.id.interfaces);

        lblName.setText(name);
        lblLocation.setText(location);
        lblAltitude.setText(altitude);
        lblTermsrv.setText(termsrv + "  p" + termsrvPort);
        lblRpb.setText(rpb + "  p" + rpbPlug);
        lblIfaces.setText(ifaceNames);
        lblOwner.setText(owner);

        // Making the interfaces clickable
        if (in.hasExtra("interfaces")) {
            lblIfaces.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getApplicationContext(), InterfaceActivity.class);
                    in.putExtra("interfaces", ifaces);

                    startActivity(in);
                }
            });
        }
    }
}
