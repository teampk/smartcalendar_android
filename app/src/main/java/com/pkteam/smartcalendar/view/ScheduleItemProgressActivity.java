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

import java.text.ParseException;
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
        int sleepStartInt = Integer.parseInt(sleepStart.substring(0,2));
        int sleepEndInt = Integer.parseInt(sleepEnd.substring(0,2));

        ArrayList<MyData> allStaticData = new ArrayList<>();
        allStaticData = dbHelper.getTodoStaticData();

        ArrayList<MyData> scheduledData = new ArrayList<>();




        // 선택된 데이터에 대하여 스케줄링
        for (int i=0; i<selectedData.size(); i++){

            // 선택된 항목의 데이터 불러오기
            int needTime = selectedData.get(i).mNeedTime;
            String timeDeadline = selectedData.get(i).mTime.split("\\.")[2];
            int dday = timeInformation.getDdayInt(timeDeadline) * (-1);
            int dMinute = timeInformation.getdTimeInt(timeDeadline) * (-1);
            int dHour = dMinute / 60;

            // -- d-day까지 시간 할당
            boolean occupiedTime[][] = new boolean[dday+1][24];

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmm");
            // 2019/03/11/22/30
            String currentTime = sdf.format(new Date(System.currentTimeMillis()));



            // -- 시작 시간 전은 true
            for (int t=Integer.parseInt(currentTime.split("/")[3]); t>=0;t--){
                occupiedTime[0][t] = true;
            }

            // -- 데드라인 후는 true
            for (int t=Integer.parseInt(timeDeadline.substring(8,10));t<24;t++){
                occupiedTime[dday][t] = true;
            }

            // -- 수면시간은 true
            if ((24 - sleepStartInt)<(24 - sleepEndInt)){
                for (int day=0;day<=dday;day++){
                    for (int time = sleepStartInt;time<24;time++){
                        occupiedTime[day][time] = true;
                    }
                    for (int time = 0;time<=sleepEndInt;time++){
                        occupiedTime[day][time] = true;
                    }
                }
            }else{
                for (int day=0;day<=dday;day++){
                    for (int time = sleepStartInt;time<=sleepEndInt;time++){
                        occupiedTime[day][time] = true;
                    }
                }
            }


            // -- 모든 static 일정들은 true
            //
            //





            long startTime = 0;
            long endTime = 0;
            try{
                startTime = sdf.parse(currentTime.substring(0, 11)+"00/00").getTime();
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            MyData scheduledStatic = null;



            // needtime 만큼 비는 시간이 있으면 바로 넣어준다.
            int count = 0;
            for (int day=0;day<=dday;day++){
                for (int time=0;time<24;time++){
                    if (!occupiedTime[day][time]){
                        count++;
                    }
                    if (count == needTime){
                        startTime += (86400000*day + (3600000*(time+1-count)));
                        endTime = startTime + 3600000*count;
                        Date startDate = new Date(startTime);
                        Date endDate = new Date(endTime);
                        String nextStart = sdf2.format(startDate);
                        String nextEnd = sdf2.format(endDate);
                        scheduledStatic = new MyData(0, selectedData.get(i).mTitle, selectedData.get(i).mLocation, false, false, nextStart+"."+nextEnd+".000000000000", selectedData.get(i).mCategory, selectedData.get(i).mMemo, 0, 0, selectedData.get(i).mId);
                    }
                }
            }


            // 확인용
            String testing3 = "(수면 시간)\n\n";
            testing3 += sleepStart + "/" + sleepEnd + "\n\n";

            testing3 += "(현재 시간)\n"+currentTime+"\n\n";
            testing3 += "(선택된 일정)\n"+selectedData.get(0).mId + "/deadline:" + timeDeadline + "/필요시간:" + selectedData.get(0).mNeedTime + "///\nD-day:" + dday + "///D-Time:" + dMinute + "/" + dHour + "/" + occupiedTime[0][0]+"\n\n";
            for (int day=0; day<dday+1;day++){
                testing3 += "day:"+day+"\n";
                for (int time = 0; time<24;time++){
                    testing3 += occupiedTime[day][time] + "/";
                    if (time % 6 == 5){
                        testing3 += "\n";
                    }
                }
                testing3 += "\n\n";
            }

            testing3 += scheduledStatic.mTime;
            binding.tvTest.setText(testing3);

        }
















        //showAnimationAndExit();
    }
    private void showAnimationAndExit(){
        final AnimationDrawable drawable = (AnimationDrawable) binding.ivScheduling.getBackground();
        drawable.start();

        /*
        binding.tvTest.setText("스케줄링 준비중입니다...");
        new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent mainIntent = new Intent(ScheduleItemProgressActivity.this, ScheduleItemResultActivity.class);
                    ScheduleItemProgressActivity.this.startActivity(mainIntent);
                    ScheduleItemProgressActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        */

    }
}
