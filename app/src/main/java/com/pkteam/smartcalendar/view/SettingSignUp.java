package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingSignupBinding;

public class SettingSignUp extends AppCompatActivity {

    FragmentSettingSignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_signup);
        binding.setSignup(this);

    }

    public void finishView(View view){
        finish();
    }

    public void signUpListener(View view){
        Toast.makeText(this, "회원가입", Toast.LENGTH_SHORT).show();
    }
}
