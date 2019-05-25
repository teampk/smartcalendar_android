package com.pkteam.smartcalendar;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.model.MyData;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;
    private DBHelper dbHelper;
    private int id;


    public NotificationHelper(Context base, int id) {
        super(base);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        this.id = id;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        MyData mData = dbHelper.getDataById(id);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(dbHelper.getDataById(id).mTitle)

                .setContentText(mData.mTime.split("\\.")[0].substring(8,10)+":"+mData.mTime.split("\\.")[0].substring(10,12)
                        +" ~ "+mData.mTime.split("\\.")[1].substring(8,10)+":"+mData.mTime.split("\\.")[1].substring(10,12))
                .setSmallIcon(R.drawable.logo_ts);
    }
    private Drawable getCategoryDrawable(int category){
        Drawable categoryDrawable = null;
        switch (category){
            case 1:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_1_24dp);
                break;
            case 2:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_2_24dp);
                break;
            case 3:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_3_24dp);
                break;
            case 4:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_4_24dp);
                break;
            case 5:
                categoryDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_category_5_24dp);
                break;
        }
        return categoryDrawable;

    }
}