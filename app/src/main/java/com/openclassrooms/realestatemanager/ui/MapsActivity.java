package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.event.DisplayDetailedPropertyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MapsActivity extends AppCompatActivity {
    private final static String TAG = "TestMapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MapsActivity.onCreate");
        setContentView(R.layout.activity_maps);
        MapsFragment fragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
        if (fragment == null) {
            // B - Create new main fragment
            Log.i(TAG, "MapsActivity.onCreate fragment null");
            fragment = new MapsFragment();
        }
        // C - Add it to FrameLayout container
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_container, fragment)
                .commit();
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
        if (event.property != null) {
            Log.i(TAG, "MapsActivity.onDisplayDetailedProperty property = " + event.property.getAddress());
            DisplayDetailedPropertyFragment fragment = new DisplayDetailedPropertyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("rowKey", event.property.getId());
            fragment.setArguments(bundle);//            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, fragment)
                    .commit();
        }
    }
}