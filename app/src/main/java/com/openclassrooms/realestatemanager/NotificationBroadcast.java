package com.openclassrooms.realestatemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context, intent.getStringExtra("message"));
    }

    private void sendNotification(Context context, String m) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(context.getText(R.string.property_saved));
        bigTextStyle.bigText(String.format(context.getString(R.string.property_successfully_saved), m));

        builder.setStyle(bigTextStyle);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
}