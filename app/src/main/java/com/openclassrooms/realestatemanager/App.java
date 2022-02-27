package com.openclassrooms.realestatemanager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class App extends Application {
    private final static String TAG = "TestApp" ;


    public static final String CHANNEL_ID = "channel 1";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"App.onCreate");

        createNotificationChannel();

    }

    private void createNotificationChannel() {
        Log.i(TAG,"App.createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }

    }
}
