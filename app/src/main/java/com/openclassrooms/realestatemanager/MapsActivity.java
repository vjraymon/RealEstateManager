package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MapsActivity extends AppCompatActivity {
    private final static String TAG = "TestMapsActivity";

    MapsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MapsActivity.onCreate");
        setContentView(R.layout.activity_maps);
        fragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
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

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragment)
//                        .add(R.id.map_container, fragment)
                        .commit();

            }


//            DisplayDetailedPropertyFragment fragment2 = (DisplayDetailedPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
//            if (fragment2 == null) {
            // B - Create new main fragment
            Log.i(TAG, "MapsActivity.onDisplayDetailedProperty fragment DisplayDetailedPropertyFragment");
            DisplayDetailedPropertyFragment fragment2 = new DisplayDetailedPropertyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("rowKey", event.property.getId());
            fragment2.setArguments(bundle);//            }
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.map_container, fragment2)
                    .add(R.id.map_container, fragment2)
                    .commit();
        }
    }
}