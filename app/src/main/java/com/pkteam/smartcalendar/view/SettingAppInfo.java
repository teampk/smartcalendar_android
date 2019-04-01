package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.ActivityAddItemCategoryBinding;
import com.pkteam.smartcalendar.databinding.FragmentSettingAppInfoBinding;

public class SettingAppInfo extends AppCompatActivity {

    FragmentSettingAppInfoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_app_info);
        binding.setAppInfo(this);






    }
    public void finishView(View view){
        finish();
    }

}
