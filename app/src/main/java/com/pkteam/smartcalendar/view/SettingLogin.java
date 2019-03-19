package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingLoginBinding;

public class SettingLogin extends AppCompatActivity {

    FragmentSettingLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_login);
        binding.setLogin(this);


    }

    public void finishView(View view){
        finish();
    }

    public void signInListener(View view){
        Toast.makeText(this, "로그인", Toast.LENGTH_SHORT).show();
    }

    public void signUpListener(View view){
        Toast.makeText(this, "회원가입", Toast.LENGTH_SHORT).show();
    }

    public void findPasswordListener(View view){
        Toast.makeText(this, "로그인", Toast.LENGTH_SHORT).show();
    }

    public void googleLoginListener(View view){
        Toast.makeText(this, "구글", Toast.LENGTH_SHORT).show();
    }

    public void facebookLoginListener(View view){
        Toast.makeText(this, "페이스북", Toast.LENGTH_SHORT).show();
    }


}
