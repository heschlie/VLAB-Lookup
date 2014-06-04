package com.heschlie.vlablookup.vlab;

import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by heschlie on 6/3/2014.
 */
public class InterfaceActivity extends ExpandableListActivity {
    private ArrayList<HashMap<String, HashMap<String, String>>> interfaceList;
    private HashMap<String, HashMap<String, String>> ifaces;
    private ExpandableListView expListView;
    private ExpandableListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_layout);
        interfaceList = new ArrayList<HashMap<String, HashMap<String, String>>>();

        Intent in = getIntent();
        ifaces = (HashMap<String, HashMap<String, String>>) in.getSerializableExtra("interfaces");

        expListView = getExpandableListView();
        adapter = new ExpandableListAdapter(this, ifaces);
        expListView.setAdapter(adapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return true;
            }
        });
    }
}
