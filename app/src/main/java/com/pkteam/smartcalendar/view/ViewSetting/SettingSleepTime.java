package com.pkteam.smartcalendar.view.ViewSetting;

import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.view.Fragments.FragmentSetting;
import com.pkteam.smartcalendar.view.TimePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by paeng on 2018. 8. 14..
 */

public class SettingSleepTime extends AppCompatActivity implements TimePickerFragment.OnDialogDismissed{

    private Button btnSubmit;
    public TextView tvStartTime, tvEndTime;
    private ArrayList<String> timeList;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_setting_sleep_time);

        bindingView();
        loadData();
    }

    @Override
    public void onDialogDismissed(String result) {
        String time = result.split("/")[0];
        String resultTime = editStringTime(time.split(":")[0]) + ":" + editStringTime(time.split(":")[1]);
        if(result.split("/")[1].equals("start")){
            tvStartTime.setText(resultTime);
        }else if (result.split("/")[1].equals("end")){
            tvEndTime.setText(resultTime);
        }
    }

    private String editStringTime(String time){
        if(Integer.valueOf(time)<10){
            return "0"+time;
        }else{
            return time;
        }
    }



    private void bindingView(){
        tvStartTime = findViewById(R.id.tv_start);
        tvEndTime = findViewById(R.id.tv_end);
        tvStartTime.setOnClickListener(listener);
        tvEndTime.setOnClickListener(listener);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(listener);

        // for testing
        TextView tvTopBarTesting = findViewById(R.id.tv_top_bar);
        tvTopBarTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), timeList.get(0)+","+timeList.get(1), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadData(){
        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);

        timeList = dbHelper.getAllSleepTime();
        tvStartTime.setText((timeList.get(0).substring(0,2)+":"+timeList.get(0).substring(2)));
        tvEndTime.setText((timeList.get(1).substring(0,2)+":"+timeList.get(1).substring(2)));


    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_submit:
                    dbHelper.sleepTimeUpdate(tvStartTime.getText().toString().replace(":",""), tvEndTime.getText().toString().replace(":",""));
                    Toast.makeText(SettingSleepTime.this, "수면시간이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case R.id.tv_start:

                    TimePickerFragment mTimePickerFragmentStart = new TimePickerFragment();
                    mTimePickerFragmentStart.show(getFragmentManager(), "start");

                    break;
                case R.id.tv_end:

                    TimePickerFragment mTimePickerFragmentEnd = new TimePickerFragment();
                    mTimePickerFragmentEnd.show(getFragmentManager(), "end");

                    break;

                default:
                    break;
            }
        }
    };
}
