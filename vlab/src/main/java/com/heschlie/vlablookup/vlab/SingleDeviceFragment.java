package com.heschlie.vlablookup.vlab;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleDeviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SingleDeviceFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "device";
    private static final String ARG_PARAM2 = "interfaces";

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

    // TODO: Rename and change types of parameters
    private HashMap<String, String> device;
    private HashMap<String, HashMap<String, String>> interfaces;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleDeviceFragment newInstance(HashMap<String, String> device, HashMap<String, HashMap<String, String>> interfaces) {
        SingleDeviceFragment fragment = new SingleDeviceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, device);
        args.putSerializable(ARG_PARAM2, interfaces);
        fragment.setArguments(args);
        return fragment;
    }
    public SingleDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = (HashMap<String, String>) getArguments().getSerializable(ARG_PARAM1);
            interfaces = (HashMap<String, HashMap<String, String>>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_device, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ifaceNames = "";
        loadDeviceInfo(device, interfaces);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadDeviceInfo(HashMap<String, String> device, HashMap<String, HashMap<String, String>> interfaces) {
        ifaceNames = "";

        // Check for the Hashmap for interfaces
        if (interfaces != null) {
            ifaces = interfaces;

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

        TextView lblName = (TextView) getView().findViewById(R.id.device_name);
        TextView lblLocation = (TextView) getView().findViewById(R.id.device_location);
        TextView lblAltitude = (TextView) getView().findViewById(R.id.device_altitude);
        TextView lblTermsrv = (TextView) getView().findViewById(R.id.device_termsrv);
        TextView lblRpb = (TextView) getView().findViewById(R.id.device_rpb);
        TextView lblOwner = (TextView) getView().findViewById(R.id.device_owner);
        TextView lblIfaces = (TextView) getView().findViewById(R.id.interfaces);

        lblName.setText(name);
        lblLocation.setText(location);
        lblAltitude.setText(altitude);
        lblTermsrv.setText(termsrv + "  p" + termsrvPort);
        lblRpb.setText(rpb + "  p" + rpbPlug);
        lblIfaces.setText(ifaceNames);
        lblOwner.setText(owner);

        // Making the interfaces clickable
//        if (in.hasExtra("interfaces")) {
//            lblIfaces.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent in = new Intent(getApplicationContext(), InterfaceActivity.class);
//                    in.putExtra("interfaces", ifaces);
//
//                    startActivity(in);
//                }
//            });
//        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(HashMap<String, String> jsonData);
    }

}
