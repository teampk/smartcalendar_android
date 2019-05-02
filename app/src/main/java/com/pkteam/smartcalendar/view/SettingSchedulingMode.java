package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingSchedulingModeBinding;

public class SettingSchedulingMode extends AppCompatActivity {

    FragmentSettingSchedulingModeBinding binding;
    private int schedulingMode = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_scheduling_mode);
        binding.setSchedulingMode(this);


    }

    public void selectMode1(View view){
        schedulingMode=1;
        binding.ivScheduling1.setVisibility(View.VISIBLE);
        binding.ivScheduling2.setVisibility(View.INVISIBLE);
        binding.ivScheduling3.setVisibility(View.INVISIBLE);
    }
    public void selectMode2(View view){
        schedulingMode=2;
        binding.ivScheduling1.setVisibility(View.INVISIBLE);
        binding.ivScheduling2.setVisibility(View.VISIBLE);
        binding.ivScheduling3.setVisibility(View.INVISIBLE);
    }
    public void selectMode3(View view){
        schedulingMode=3;
        binding.ivScheduling1.setVisibility(View.INVISIBLE);
        binding.ivScheduling2.setVisibility(View.INVISIBLE);
        binding.ivScheduling3.setVisibility(View.VISIBLE);
    }

    public void completeListener(View view){
        Toast.makeText(this, "스케줄링 모드"+String.valueOf(schedulingMode), Toast.LENGTH_SHORT).show();
        finish();
    }


}
