package com.heschlie.vlablookup.vlab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

public class MainActivity extends Activity implements SingleDeviceFragment.OnFragmentInteractionListener, MainFragActivity.SingleFragmentData{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getFragmentManager();

        Fragment frag = new MainFragActivity();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, frag);
        ft.commit();
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

    @Override
    public void onFragmentInteraction(HashMap<String, String> jsonData) {
    }

    @Override
    public void sendData(int destFrag, HashMap<String, String> device, HashMap<String, HashMap<String, String>> interfaces) {
//        SingleDeviceFragment singleDeviceFrag = (SingleDeviceFragment) fm.findFragmentById(R.id.single_table);
//        if (singleDeviceFrag != null)
//            singleDeviceFrag.loadDeviceInfo(device, interfaces);
//        else {
            SingleDeviceFragment newFragment = SingleDeviceFragment.newInstance(device, interfaces);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();
//        }
    }
}
