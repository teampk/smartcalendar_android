package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pkteam.smartcalendar.DBHelper;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingSchedulingModeBinding;
import com.singh.daman.gentletoast.GentleToast;

import java.util.ArrayList;

public class SettingSchedulingMode extends AppCompatActivity {

    FragmentSettingSchedulingModeBinding binding;
    private DBHelper dbHelper;
    private int selectedSchedulingMode = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_scheduling_mode);
        binding.setSchedulingMode(this);


        dbHelper = new DBHelper(getApplicationContext(), "SmartCal.db", null, 1);
        int schedulingMode = dbHelper.getSchedulingMode();
        Log.d("TimeSpoon", String.valueOf(schedulingMode));
        switch(schedulingMode){
            case 1:
                modeSelectListener1(binding.getRoot());
                break;
            case 2:
                modeSelectListener2(binding.getRoot());
                break;
            case 3:
                modeSelectListener3(binding.getRoot());
                break;
        }

    }

    public void modeSelectListener1(View view){
        selectedSchedulingMode=1;
        binding.ivScheduling1.setVisibility(View.VISIBLE);
        binding.ivScheduling2.setVisibility(View.INVISIBLE);
        binding.ivScheduling3.setVisibility(View.INVISIBLE);
        binding.llScheduling1Explain.setVisibility(View.VISIBLE);
        binding.llScheduling2Explain.setVisibility(View.GONE);
        binding.llScheduling3Explain.setVisibility(View.GONE);
    }
    public void modeSelectListener2(View view){
        selectedSchedulingMode=2;
        binding.ivScheduling1.setVisibility(View.INVISIBLE);
        binding.ivScheduling2.setVisibility(View.VISIBLE);
        binding.ivScheduling3.setVisibility(View.INVISIBLE);
        binding.llScheduling1Explain.setVisibility(View.GONE);
        binding.llScheduling2Explain.setVisibility(View.VISIBLE);
        binding.llScheduling3Explain.setVisibility(View.GONE);
    }
    public void modeSelectListener3(View view){
        selectedSchedulingMode=3;
        binding.ivScheduling1.setVisibility(View.INVISIBLE);
        binding.ivScheduling2.setVisibility(View.INVISIBLE);
        binding.ivScheduling3.setVisibility(View.VISIBLE);

        binding.llScheduling1Explain.setVisibility(View.GONE);
        binding.llScheduling2Explain.setVisibility(View.GONE);
        binding.llScheduling3Explain.setVisibility(View.VISIBLE);
    }

    public void completeListener(View view){

        if(selectedSchedulingMode == 3){
            GentleToast.with(getApplicationContext()).longToast("MODE 3는 준비중입니다...").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();

        }else{
            dbHelper.updateSchedulingMode(selectedSchedulingMode);
            GentleToast.with(getApplicationContext()).longToast("Scheduling Mode : "+selectedSchedulingMode).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
            finish();
        }


    }

}
