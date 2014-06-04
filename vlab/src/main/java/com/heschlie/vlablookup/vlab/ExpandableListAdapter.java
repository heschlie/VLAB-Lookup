package com.heschlie.vlablookup.vlab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by heschlie on 6/4/2014.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private HashMap<String, HashMap<String, String>> ifaces;
    private ArrayList<String> keyIndex;

    public ExpandableListAdapter(Activity context, HashMap<String, HashMap<String, String>> ifaces) {
        this.context = context;
        this.ifaces = ifaces;
        keyIndex = getKeyIndex();
    }

    private ArrayList<String> getKeyIndex() {
        ArrayList<String> index = new ArrayList<String>();
        for (Map.Entry<String, HashMap<String, String>> entry : ifaces.entrySet()) {
            String key = entry.getKey();
            index.add(key);
            Log.d("Key set: ", key);
        }
        return index;
    }

    private ArrayList<String> getKeyIndex(HashMap<String, String> map) {
        ArrayList<String> index = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            index.add(key);
        }
        return index;
    }


    @Override
    public int getGroupCount() {
        return ifaces.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return ifaces.get(keyIndex.get(i));
    }

    @Override
    public Object getChild(int i, int i2) {
        HashMap<String, String> details = ifaces.get(keyIndex.get(i));
        ArrayList<String> index = getKeyIndex(details);
        Pair<String, String> child = new Pair<String, String>(index.get(i2), details.get(index.get(i2)));
        return child;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String iface = keyIndex.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_item, null);
        }
        TextView item = (TextView) view.findViewById(R.id.iface);
        item.setTypeface(null, Typeface.BOLD);
        item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        item.setText(iface);
        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        //Pair<String, String> child = (Pair<String, String>) getChild(i, i2);
        HashMap<String, String> child = ifaces.get(keyIndex.get(i));
        LayoutInflater inflater = context.getLayoutInflater();

        view = inflater.inflate(R.layout.child_item, null);
        TableLayout layout = (TableLayout) view;

        for (Map.Entry<String, String> entry : child.entrySet()) {
            TextView keyText = new TextView(context);
            keyText.setTextAppearance(context, R.style.col1);
            keyText.setText(entry.getKey() + ": ");

            TextView valText = new TextView(context);
            valText.setTextAppearance(context, R.style.col2);
            valText.setText(entry.getValue());

            TableRow tr = new TableRow(context);
            tr.addView(keyText);
            tr.addView(valText);

            layout.addView(tr);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}
