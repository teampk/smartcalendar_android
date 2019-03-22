package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingProfileBinding;

public class SettingProfile extends AppCompatActivity {

    private FragmentSettingProfileBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_profile);
        binding.setProfile(this);




    }

    public void finishView(View view){
        finish();
    }
    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(SettingProfile.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
