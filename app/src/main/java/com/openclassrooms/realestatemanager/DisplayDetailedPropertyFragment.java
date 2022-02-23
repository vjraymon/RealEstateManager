package com.openclassrooms.realestatemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class DisplayDetailedPropertyFragment extends Fragment {
    private static final String TAG = "TestDetailedFragment";

    public DisplayDetailedPropertyFragment() {
        // Required empty public constructor
    }

    TextView mAddress;
/*
    public static DisplayDetailedPropertyFragment newInstance(String param1, String param2) {
        DisplayDetailedPropertyFragment fragment = new DisplayDetailedPropertyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.display_detailed_property_fragment, container, false);
        mAddress = v.findViewById(R.id.detailed_property_address);
        initialization(null);
        return v;
    }

    private void initialization(Property property) {
        mAddress.setText(((property == null) || (property.getAddress() == null))
                ? "Address unknown"
                : "Address : " +property.getAddress());
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDisplayDetailedProperty(DisplayDetailedPropertyEvent event) {
        if (event.property != null)
        {
            Log.i(TAG, "DisplayDetailedPropertyFragment.onDisplayDetailedProperty property = " + event.property.getAddress());
            initialization(event.property);
        }
    }
}