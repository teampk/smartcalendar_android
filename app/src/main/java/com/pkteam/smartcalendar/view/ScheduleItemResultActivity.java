package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.ActivityScheduleItemResultBinding;

public class ScheduleItemResultActivity extends AppCompatActivity {

    ActivityScheduleItemResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_item_result);

    }
}
