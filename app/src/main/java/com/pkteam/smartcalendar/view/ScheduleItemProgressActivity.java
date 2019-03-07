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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_progress);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);


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
        for (int i=0; i<selectedData.size();i++){

        }

        int needTime = selectedData.get(0).mNeedTime;
        long time = Long.parseLong(selectedData.get(0).mTime.split("\\.")[2]);
        ArrayList<MyData> allStaticData = new ArrayList<>();
        allStaticData = dbHelper.getTodoStaticData();

        String testing2 = "";
        for (int i=0; i<allStaticData.size();i++){
            testing2 += allStaticData.get(i).mId+"/"+allStaticData.get(i).mTitle+"/"+allStaticData.get(i).mTime.split("\\.")[2]+"\n\n";
        }
        binding.tvTest.setText(testing2);









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
