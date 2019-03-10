package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.GetTimeInformation;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemProgressBinding;
import com.pkteam.smartcalendar.model.MyData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleItemProgressActivity extends AppCompatActivity {
    static final int SPLASH_DISPLAY_LENGTH = 3000;
    ActivityScheduleItemProgressBinding binding;
    DBHelper dbHelper;
    GetTimeInformation timeInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_progress);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        timeInformation = new GetTimeInformation();

        Intent intent = getIntent();
        ArrayList<Integer> selectedId = intent.getIntegerArrayListExtra("selectedDynamic");

        ArrayList<MyData> selectedData = new ArrayList<>();
        for (int i=0; i<selectedId.size();i++){
            selectedData.add(dbHelper.getDataById(selectedId.get(i)));
        }

        // testing
        String testing = "";
        for (int i=0; i<selectedData.size();i++){
            testing += selectedData.get(i).mId+"/"+selectedData.get(i).mTitle+"/"+selectedData.get(i).mTime.split("\\.")[2]+"/n:"+selectedData.get(i).mNeedTime+"\n\n";
        }
        binding.tvTest.setText(testing);
        //

        // -- scheduling algorithm --

        String sleepStart = dbHelper.getAllSleepTime().get(0);
        String sleepEnd = dbHelper.getAllSleepTime().get(1);
        ArrayList<MyData> allStaticData = new ArrayList<>();
        allStaticData = dbHelper.getTodoStaticData();

        ArrayList<MyData> scheduledData = new ArrayList<>();


        for (int i=0; i<selectedData.size(); i++){

        }

        MyData dynamicData = selectedData.get(0);
        int needTime = dynamicData.mNeedTime;
        String deadline = dynamicData.mTime.split("\\.")[2];
        int dday = timeInformation.getDdayInt(dynamicData.mTime.split("\\.")[2]) * (-1);
        int dMinute = timeInformation.getdTimeInt(dynamicData.mTime.split("\\.")[2]);
        int dHour = dMinute / 60;


        boolean occupiedTime[][] = new boolean[dday+1][24];


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/kk/mm");
        String mDate = sdf.format(new Date(System.currentTimeMillis()));
        int startHour = Integer.parseInt(mDate.split("/")[3]);


        // 시작 시간 전은 false
        for (int i=startHour; i>=0;i--){
            occupiedTime[0][i] = true;
        }

        // 데드라인 후는 true
        for (int i=Integer.parseInt(deadline.substring(8,10));i<24;i++){
            occupiedTime[dday][i] = true;
        }

        int count = 0;
        for (int i=0;i<=dday;i++){
            for (int j=0;j<24;j++){
                if (!occupiedTime[i][j]){
                    count++;
                }
                if (count == needTime){
                    MyData scheduledStatic = new MyData(0, dynamicData.mTitle, dynamicData.mLocation, false, false, "", dynamicData.mCategory, dynamicData.mMemo, 0, 0, dynamicData.mId);
                }
            }
        }





        // 201903102050
        String testing3 = sleepStart + "/" + sleepEnd + "\n\n";
        testing3 += selectedData.get(0).mId + "//" + deadline + "//" + selectedData.get(0).mNeedTime + "///\nD-day:" + dday + "///D-Time:" + dMinute + "/" + dHour + "/" + occupiedTime[0][0]+"\n\n";
        testing3 += mDate+"\n\n";
        for (int i=0; i<dday+1;i++){
            for (int j = 0; j<24;j++){
                testing3 += occupiedTime[i][j] + "/";
                if (j % 8 == 7){
                    testing3 += "\n";
                }
            }
            testing3 += "\n";
        }
        binding.tvTest.setText(testing3);















        // showAnimationAndExit();
    }
    private void showAnimationAndExit(){
        final AnimationDrawable drawable = (AnimationDrawable) binding.ivScheduling.getBackground();
        drawable.start();
        binding.tvTest.setText("스케줄링 준비중입니다...");
        new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent mainIntent = new Intent(ScheduleItemProgressActivity.this, ScheduleItemResultActivity.class);
                    ScheduleItemProgressActivity.this.startActivity(mainIntent);
                    ScheduleItemProgressActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);



    }
}
