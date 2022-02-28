package com.openclassrooms.realestatemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {
    private final static String TAG = "TestMapsFragment";


    public MapsFragment() {}

    private GoogleMap map;
    private static Location lastKnownLocation = null;
    private static final int DEFAULT_ZOOM = 15;

    private boolean locationPermissionGranted = false;

    private LocationManager objGps;
    private LocationListener objListener;

    /**
     * Called when the activity is first created.
     */
    private void initGps() {
        Log.i(TAG, "MapsFragment.initGps");
        if (getContext() != null) {
            objGps = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            objListener = new MyObjListener();
        } else {
            Log.i(TAG, "MapsFragment.initGps getContext() null");
        }
    }

    private class MyObjListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location==null) Log.i(TAG, "MapsFragment.MyObjListener.onLocationChanged location null");
            else {
                Log.i(TAG, "MapsFragment.MyObjListener.onLocationChanged location not null");
                lastKnownLocation = location;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude()),
                        DEFAULT_ZOOM
                        )
                );
            }
        }
    }

    private void getDeviceLocation() {
        Log.i(TAG, "MapsFragment.getDeviceLocation");
        try {
            if (locationPermissionGranted) {
                Log.i(TAG, "MapsFragment.getDeviceLocation locationPermissionGranted");
                objGps.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        60 * 1000,
                        10.0F,
                        objListener);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "MapsFragment.getDeviceLocation Exception ", e);
        }

    }

    private final ActivityResultLauncher<String> permissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> locationPermissionGranted = result
    );

    private void getLocationPermission() {
//        Log.i(TAG, "MapsFragment.getLocationPermission");
        if ((getContext() != null) &&
                (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)) {
            locationPermissionGranted = true;
        } else {
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (map == null) return;
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                // button focus on the map
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e(TAG, "MapsFragment.updateLocationUI Exception ", e);
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.clear();
            initGps();
            Log.i(TAG, "MapsFragment.onMapReady first getLocationPermission()");
            getLocationPermission();
            while (!locationPermissionGranted) {
                updateLocationUI();
                getLocationPermission();
            }
            Log.i(TAG, "MapsFragment.onMapReady last updateLocationUI()");
            updateLocationUI();
            getDeviceLocation();
//            LatLng location = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "MapsFragment.onCreateView");
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        Log.i(TAG, "MapsFragment.onCreateView end");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "MapsFragment.onViewCreated");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            Log.i(TAG, "MapsFragment.onViewCreated mapFragment not null");
            mapFragment.getMapAsync(callback);
        }
    }
}