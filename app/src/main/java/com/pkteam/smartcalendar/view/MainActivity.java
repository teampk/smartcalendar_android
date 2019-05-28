package com.pkteam.smartcalendar.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.pkteam.smartcalendar.AlarmReceiver;
import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.GetTimeInformation;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.model.MyData;
import com.singh.daman.gentletoast.GentleToast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "TIMESPOONTAG";
    private GetTimeInformation gti;
    private DBHelper dbHelper;
    private ArrayList<MyData> staticDataList;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new FragmentHome();
                    break;
                case R.id.nav_calendar:
                    selectedFragment = new FragmentCalendar();
                    break;
                case R.id.nav_setting:
                    selectedFragment = new FragmentSetting();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);

        // Bottom Navigation Bar
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        gti = new GetTimeInformation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAlarm();
    }

    private void setAlarm(){
        staticDataList = new ArrayList<>();
        staticDataList = dbHelper.getTodoStaticData();
        Calendar[] calendar = new Calendar[staticDataList.size()];
        for(int i=0; i<calendar.length;i++){
            Log.d(TAG, staticDataList.get(i).mTitle);
            String time = staticDataList.get(i).mTime.split("\\.")[0];
            int year = Integer.valueOf(time.substring(0,4));
            int month = Integer.valueOf(time.substring(4,6));
            int date = Integer.valueOf(time.substring(6,8));
            int hour = Integer.valueOf(time.substring(8,10));
            int minute = Integer.valueOf(time.substring(10,12));

            calendar[i] = Calendar.getInstance();
            calendar[i].set(year, month-1, date, hour, minute, 0);
        }
        startAlarm(calendar);
    }

    private void startAlarm(Calendar[] c) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender;

        for (int i=0; i<c.length;i++){
            intent.putExtra("id", staticDataList.get(i).mId);
            sender = PendingIntent.getBroadcast(this, i+1, intent, 0);
            if (c[i].before(Calendar.getInstance())) {
                c[i].add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c[i].getTimeInMillis(), sender);
            Log.d(TAG, "noti test" + gti.getDateByMs(c[i].getTimeInMillis()));
        }
    }


    long pressTime;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - pressTime < 3000){
            finish();
            return;
        }
        GentleToast.with(getApplicationContext()).longToast("한 번 더 누르시면 앱이 종료됩니다.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
        pressTime = System.currentTimeMillis();
    }


}
