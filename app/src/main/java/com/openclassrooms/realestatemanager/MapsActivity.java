package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MapsActivity extends AppCompatActivity {
    private final static String TAG = "TestMainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainActivity2.onCreate");
        setContentView(R.layout.activity_maps);
    }
}