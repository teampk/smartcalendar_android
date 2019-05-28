package com.pkteam.smartcalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    private static String TAG = "TIMESPOONTAG";

    @Override
    public void onReceive(Context context, Intent intent){
        int id = intent.getIntExtra("id", 0);
        Log.d(TAG, "id"+id);


        NotificationHelper notificationHelper = new NotificationHelper(context, id);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(id, nb.build());

    }

}
