package com.heschlie.vlablookup.vlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Brought to you by Stephen Schlie
 */
public class SingleDeviceActivity extends Activity{

    // JSON Node names
    private static final String TAG_NAME = "name";
    private static final String TAG_TERMSRV = "termsrv";
    private static final String TAG_TERMSRV_PORT = "termsrv_port";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_ALTITUDE = "location_altitude";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_RPB = "rpb";
    private static final String TAG_RPB_PLUG = "rpb_plug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_device);

        // get our intent
        Intent in = getIntent();

        // get the hashmap containing the device info
        HashMap<String, String> device = (HashMap<String, String>) in.getSerializableExtra("device");

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

        lblName.setText(name);
        lblLocation.setText(location);
        lblAltitude.setText(altitude);
        lblTermsrv.setText(termsrv + " p" + termsrvPort);
        lblRpb.setText(rpb + " p" + rpbPlug);
        lblOwner.setText(owner);
    }
}
