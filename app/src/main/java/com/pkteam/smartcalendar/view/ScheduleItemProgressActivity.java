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
    GetTimeInformation gti;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmm");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_progress);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        gti = new GetTimeInformation();


        Intent intent = getIntent();
        ArrayList<Integer> selectedId = intent.getIntegerArrayListExtra("selectedDynamic");

        // Scheduling 하기 위해 선택된 데이터
        ArrayList<MyData> selectedData = new ArrayList<>();
        for (int i=0; i<selectedId.size();i++){
            selectedData.add(dbHelper.getDataById(selectedId.get(i)));
        }

        // 현재 시간 format : 2019/03/11/22/30
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));
        long currentTimeLong = Long.valueOf(sdf2.format(new Date(System.currentTimeMillis())));


        // 수면시간
        String sleepStart = dbHelper.getAllSleepTime().get(0);
        String sleepEnd = dbHelper.getAllSleepTime().get(1);
        int sleepStartInt = Integer.parseInt(sleepStart.substring(0,2));
        int sleepEndInt = Integer.parseInt(sleepEnd.substring(0,2));




        // tvTest >>>>>
        String scheduledDataList = "";
        for (int i=0; i<selectedData.size();i++){
            scheduledDataList += selectedData.get(i).mId+"/"+selectedData.get(i).mTitle+"/"+selectedData.get(i).mTime.split("\\.")[2]+"/n:"+selectedData.get(i).mNeedTime+"\n\n";
        }
        binding.tvTest.append("\n\n===== 스케줄링 할 데이터 list =====\n\n");
        binding.tvTest.append(scheduledDataList+"\n");

        binding.tvTest.append("===== 현재 시간 =====\n"+currentTime+"\n\n");

        binding.tvTest.append("===== 수면 시간 =====\n");
        binding.tvTest.append(sleepStart+"/"+sleepEnd+"\n");
        // >>>>>>>>>>>>


        ArrayList<MyData> staticDataAll = new ArrayList<>();
        ArrayList<MyData> staticDataInRange = new ArrayList<>();

        ArrayList<MyData> scheduledData = new ArrayList<>();

        staticDataAll = dbHelper.getTodoStaticData();




        // 선택된 데이터에 대하여 스케줄링
        for (int i=0; i<selectedData.size(); i++){

            // 선택된 항목의 데이터 불러오기
            int needTime = selectedData.get(i).mNeedTime;
            String timeDeadline = selectedData.get(i).mTime.split("\\.")[2];
            long timeDeadlineLong = Long.valueOf(timeDeadline);
            int dday = gti.getDdayInt(timeDeadline) * (-1);
            int dMinute = gti.getdTimeInt(timeDeadline) * (-1);
            int dHour = dMinute / 60;

            // -- d-day까지 시간 할당
            boolean occupiedTime[][] = new boolean[dday+1][24];


            // tvTest >>>>>
            binding.tvTest.append("\n\n===== 선택된 일정 ("+ i +") =====\n\nid:"+selectedData.get(i).mId + " //deadline:" + timeDeadline + "\n//필요시간:" + selectedData.get(i).mNeedTime + "///\nD-day:" + dday + "///D-Minute:" + dMinute + "///D-Minute:" + dHour +"\n\n");
            // >>>>>>>>>>>>



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

            // -- deadline 내의 static 일정들은 true
            binding.tvTest.append("===== Static 일정 LIST =====\n");
            for (int s=0;s<staticDataAll.size();s++){
                String staticStartString = staticDataAll.get(s).mTime.split("\\.")[0];
                String staticEndString = staticDataAll.get(s).mTime.split("\\.")[1];
                long staticStart = Long.valueOf(staticStartString);
                long staticEnd = Long.valueOf(staticEndString);
                binding.tvTest.append("static Start:\t"+staticStart+"\nstatic End:\t"+staticEnd+"//\n\n");

                // currentTime  :   201905111401
                // Static start :   201905112000
                // Static end   :   201905112300
                // Deadline     :   201905170000

                int startTime = Integer.valueOf(staticStartString.substring(8, 10));
                int endTime = Integer.valueOf(staticEndString.substring(8, 10));

                int staticStartDday = gti.getDdayInt(staticStartString)*(-1);
                int staticEndDday = gti.getDdayInt(staticEndString)*(-1);

                if(currentTimeLong<staticStart && staticEnd < timeDeadlineLong){

                    for (int t = startTime; t<=endTime; t++){
                        occupiedTime[staticStartDday][t] = true;
                    }


                    if (staticEndString.substring(10, 12).equals("00")){
                        occupiedTime[gti.getDdayInt(staticStartString)*(-1)][endTime] = false;
                    }
                }

                for (int day=0;day<=dday;day++){
                    for (int time=0;time<24;time++){
                        int year_day = Integer.valueOf(gti.getCurrentDate().substring(0, 8))+day;


                        //occupiedTime[day][time];
                    }
                }


            }
            binding.tvTest.append("\n\n");


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
            String testing3 = "";

            for (int day=0; day<dday+1;day++){
                testing3 += "day:"+day+"\n";
                for (int time = 0; time<24;time++){
                    if (time % 6 == 0){
                        testing3 += time+"~"+(time+5)+": ";
                    }
                    testing3 += occupiedTime[day][time] + "/";
                    if (time % 6 == 5){
                        testing3 += "\n";
                    }
                }
                testing3 += "\n\n";
            }

            // testing3 += scheduledStatic.mTime;
            binding.tvTest.append("===== 남은 일정 =====\n");
            binding.tvTest.append(testing3);

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
