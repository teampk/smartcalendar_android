package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemResultBinding;
import com.pkteam.smartcalendar.model.MyData;

import java.util.ArrayList;

public class ScheduleItemResultActivity extends AppCompatActivity {

    ActivityScheduleItemResultBinding binding;


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_result);
        binding.setScheduling(this);

        binding.tvResult.setText("다음과 같이 스케줄링이 진행되었습니다.\n\n");

        Intent getIntent = getIntent();
        ArrayList<MyData> scheduledStaticList = (ArrayList<MyData>) getIntent.getSerializableExtra("scheduled");

        for (int i=0;i<scheduledStaticList.size();i++){
            String time[] = scheduledStaticList.get(i).mTime.split("\\.");
            binding.tvResult.append("title : "+scheduledStaticList.get(i).mTitle + "\nTime : "+time[0]+"~"+time[1]+"\nscheduledId : "+scheduledStaticList.get(i).mScheduleId);
        }


    }

    public void finishView(View view){
        finish();
    }
}
