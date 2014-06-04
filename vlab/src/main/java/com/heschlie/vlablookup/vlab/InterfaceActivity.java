package com.heschlie.vlablookup.vlab;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by heschlie on 6/3/2014.
 */
public class InterfaceActivity extends ListActivity {
    private ArrayList<HashMap<String, HashMap<String, String>>> interfaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interfaceList = new ArrayList<HashMap<String, HashMap<String, String>>>();

        Intent in = getIntent();
        HashMap<String, HashMap<String, String>> interfaces = (HashMap) in.getSerializableExtra("interfaces");


    }
}
