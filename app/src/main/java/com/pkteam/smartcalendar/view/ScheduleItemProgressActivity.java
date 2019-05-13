package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.GetTimeInformation;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemProgressBinding;
import com.pkteam.smartcalendar.model.MyData;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleItemProgressActivity extends AppCompatActivity{
    static final String TAG = "TimeSpoonLOG";
    static final int SPLASH_DISPLAY_LENGTH = 3000;
    ActivityScheduleItemProgressBinding binding;
    DBHelper dbHelper;
    GetTimeInformation gti;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmm");

    private boolean isError = false;
    private ArrayList<MyData> scheduledStaticList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_progress);
        binding.setScheduling(this);
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        gti = new GetTimeInformation();
        isError = false;

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


        // 스케줄링 모드
        int schedulingMode = dbHelper.getSchedulingMode();


        // tvTest >>>>>
        String scheduledDataList = "";
        for (int i=0; i<selectedData.size();i++){
            scheduledDataList += selectedData.get(i).mId+"/"+selectedData.get(i).mTitle+"/"+selectedData.get(i).mTime.split("\\.")[2]+"/n:"+selectedData.get(i).mNeedTime+"\n\n";
        }

        binding.tvTest.append("===== 현재 시간 =====\n"+currentTime+"\n\n");
        binding.tvTest.append("===== 수면 시간 =====\n");
        binding.tvTest.append(sleepStart+"/"+sleepEnd+"\n\n");
        binding.tvTest.append("===== 스케줄링 모드 =====\n"+schedulingMode+"\n\n");
        binding.tvTest.append("===== 스케줄링 할 Dynamic list =====\n\n");
        binding.tvTest.append(scheduledDataList+"\n");
        // >>>>>>>>>>>>


        ArrayList<MyData> staticDataAll = new ArrayList<>();
        scheduledStaticList = new ArrayList<>();
        staticDataAll = dbHelper.getTodoStaticData();


        // 선택된 데이터에 대하여 스케줄링
        for (int index=0; index<selectedData.size(); index++){

            // 선택된 항목의 데이터 불러오기
            int needTime = selectedData.get(index).mNeedTime;
            String timeDeadline = selectedData.get(index).mTime.split("\\.")[2];
            long timeDeadlineLong = Long.valueOf(timeDeadline);
            int dday = gti.getDdayInt(timeDeadline) * (-1);
            int dMinute = gti.getdTimeInt(timeDeadline) * (-1);
            int dHour = dMinute / 60;

            // -- d-day까지 시간 할당
            boolean occupiedTime[][] = new boolean[dday+1][24];


            // tvTest >>>>>
            binding.tvTest.append("\n\n===== 선택된 Dynamic 일정 ("+ index +") =====\n\nid:"+selectedData.get(index).mId + " //deadline:" + timeDeadline + "\n//필요시간:" + selectedData.get(index).mNeedTime + "///\nD-day:" + dday + "///D-Minute:" + dMinute + "///D-Minute:" + dHour +"\n\n");
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
                binding.tvTest.append("\n\nstatic Start:\t"+staticStart+"\nstatic End:\t"+staticEnd+"//\n\n");

                // currentTime  :   201905111401
                // Static start :   201905112000
                // Static end   :   201905112300
                // Deadline     :   201905170000


                for (int day=0;day<=dday;day++){
                    for (int time=0;time<24;time++){
                        String year_day = String.valueOf(Integer.valueOf(gti.getCurrentDate().substring(0, 8))+day);
                        long year_day_time = Long.valueOf(year_day +gti.timeZeroProblem(String.valueOf(time))+"00");

                        if(staticStart <= year_day_time && year_day_time <= staticEnd){
                            occupiedTime[day][time] = true;

                            // static 끝나는 시간이 23:00 같이 정각인 경우
                            // 23부터 비워놓을 수 있게 마지막 시간을 false로 만들어 준다.
                            // 23:03 의 경우는 true로 한다.
                            if(staticEndString.substring(10, 12).equals("00") && staticEnd<timeDeadlineLong){
                                occupiedTime[gti.getDdayInt(staticEndString)*(-1)][Integer.valueOf(staticEndString.substring(8, 10))] = false;
                            }

                        }
                    }
                }
            }

            // -- 남은 일정들 보여줘
            binding.tvTest.append("\n\n");
            binding.tvTest.append("===== OCCUPIED STATUS =====\n");
            binding.tvTest.append(showOccupied(occupiedTime, dday));


            // -- 비어있는 시간들을 배열로 정리
            binding.tvTest.append("\n\n");
            long startTimeMs = 0;
            long endTimeMs = 0;
            try{
                // format : 2019/03/11/22/30
                startTimeMs = sdf.parse(currentTime.substring(0, 11)+"00/00").getTime();
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            // surplus format = day:time:surplus
            ArrayList<String> surplus = new ArrayList<>();
            int count = 0;
            int day_start = 0;
            int time_start = 0;
            for (int day=0;day<=dday;day++){
                for(int time=0;time<24;time++){
                    if(!occupiedTime[day][time]){
                        if(count==0){
                            day_start=day;
                            time_start=time;
                        }
                        count++;
                    }else if (occupiedTime[day][time] && count!=0){
                        surplus.add(day_start+":"+time_start+":"+count);
                        day_start=0;
                        time_start=0;
                        count=0;
                    }
                }
            }
            int sum = 0;
            for (int t=0;t<surplus.size();t++){
                binding.tvTest.append(surplus.get(t)+"/\n");
                sum+=Integer.valueOf(surplus.get(t).split(":")[2]);
            }
            binding.tvTest.append("sum:"+sum+"\n\n");


            // 비어있는 시간에 스케줄링 진행
            if(sum<needTime){
                // 남은 모든 시간의 합이 needtime 보다 적은 경우
                // 스케줄링을 수행하지 않는다.
                Log.d(TAG, "ERROR 1");
                Intent errorIntent = new Intent();
                errorIntent.putExtra("error", selectedData.get(index).mId);
                setResult(RESULT_OK, errorIntent);
                isError = true;
            }else{
                switch(dbHelper.getSchedulingMode()){

                    // MODE 1)
                    case 1:
                        Log.d(TAG, "MODE 1");

                        // surplus
                        // needtime

                        int unit = (int) Math.ceil((double)needTime / (double)surplus.size());
                        int needtime_cal = needTime;

                        for (int i=0;i<surplus.size();i++){




                            if(needtime_cal == 0){
                                break;
                            }
                        }




                        MyData scheduledStatic;
                        int dd = 1;
                        int tt = 13;
                        int nn = 5;

                        String time[] = getTimeByIndex(startTimeMs, dd, tt, nn).split(":");

                        scheduledStatic = new MyData(0, selectedData.get(index).mTitle, selectedData.get(index).mLocation, false, false,
                                time[0]+"."+time[1]+".000000000000", selectedData.get(index).mCategory, selectedData.get(index).mMemo, 0, 0, selectedData.get(index).mId);
                        scheduledStaticList.add(scheduledStatic);

                        for (int q = 0 ; q < nn ; q++){
                            occupiedTime[dd][tt+q] = true;
                        }


                        // -- 스케줄 된 일정
                        binding.tvTest.append("\n\n=== 스케줄 된 일정 === ");
                        binding.tvTest.append(scheduledStatic.mTitle+"/"+scheduledStatic.mTime.split("\\.")[0]+"~"+scheduledStatic.mTime.split("\\.")[1]);

                        break;

                    // MODE 2)
                    case 2:
                        Log.d(TAG, "MODE 2");

                        break;


                    // MODE 3)
                    case 3:
                        Log.d(TAG, "MODE 3");

                        break;
                }
            }


            // -- 남은 일정들 보여줘
            binding.tvTest.append("\n\n");
            binding.tvTest.append("===== 남은 일정 Scheduled =====\n");
            binding.tvTest.append(showOccupied(occupiedTime, dday));





        }



        showAnimationAndExit();
    }

    private String getTimeByIndex(long currentMs, int day, int time, int needtime){
        long startTimeMs = currentMs;
        startTimeMs += (86400000 * day + (3600000*time));
        long endTimeMs = startTimeMs + 3600000 * needtime;
        return sdf2.format(new Date(startTimeMs))+":"+sdf2.format(new Date(endTimeMs));
    }

    private String showOccupied(boolean[][] occupiedTime, int dday){
        String occupied= "";

        for (int day=0; day<dday+1;day++){
            int theDay = Integer.valueOf(gti.getCurrentDate().substring(6, 8)) + day;
            occupied += "day:"+theDay+"일\n";
            for (int time = 0; time<24;time++){
                if (time % 6 == 0){
                    occupied += gti.timeZeroProblem(String.valueOf(time))+"~"+gti.timeZeroProblem(String.valueOf(time+5))+":\t";
                }
                if (occupiedTime[day][time]){
                    occupied += " X /";
                }else{
                    occupied += " O /";
                }
                if (time % 6 == 5){
                    occupied += "\n";
                }
            }
            occupied += "\n";
        }
        return occupied;
    }



    private void showAnimationAndExit(){
        final AnimationDrawable drawable = (AnimationDrawable) binding.ivScheduling.getBackground();
        drawable.start();

        /*

        new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if (isError){
                        finish();
                    }else{
                        Intent intent = new Intent(ScheduleItemProgressActivity.this, ScheduleItemResultActivity.class);
                        intent.putExtra("scheduled", scheduledStaticList);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);

            */

    }
}
