package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingSignupBinding;
import com.singh.daman.gentletoast.GentleToast;

public class SettingSignUp extends AppCompatActivity {

    FragmentSettingSignupBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_signup);
        binding.setSignup(this);
        mAuth = FirebaseAuth.getInstance();


    }

    public void finishView(View view){
        finish();
    }

    public void signUpListener(View view){
        binding.pbSignUp.setVisibility(View.VISIBLE);
        if(checkJoin()){
            createUser(binding.etName.toString(), binding.etId.getText().toString(), binding.etPw.getText().toString());
        }else{
            binding.pbSignUp.setVisibility(View.INVISIBLE);
        }
    }

    private void createUser(String name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);


                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseNetworkException e) {
                                GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_network)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_weak_pw)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_format_sign_up)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_duplicate)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            } catch (Exception e) {
                                GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_sign_up)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            }
                            binding.pbSignUp.setVisibility(View.INVISIBLE);
                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(binding.etName.getText().toString())
                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                GentleToast.with(getApplicationContext()).longToast("회원가입 성공").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                                setResult(RESULT_OK, new Intent());
                                finish();
                            }
                        }
                    });



        } else {
            GentleToast.with(getApplicationContext()).longToast("Sign in Failed").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
        }
    }

    private boolean checkJoin(){

        if(binding.etId.getText().toString().length()==0){
            GentleToast.with(getApplicationContext()).longToast("e-mail을 입력해주세요.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
            return false;
        }else if(binding.etName.getText().toString().length()==0){
            GentleToast.with(getApplicationContext()).longToast("이름을 입력해주세요.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
            return false;
        }else if(binding.etPw.getText().toString().length()==0){
            GentleToast.with(getApplicationContext()).longToast("비밀번호를 입력해주세요.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
            return false;
        }
        else if(binding.etPw.getText().toString().length()<6){
            GentleToast.with(getApplicationContext()).longToast("비밀번호는 6자 이상으로 입력해주세요.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();

            return false;
        }
        else if (!binding.etPw.getText().toString().equals(binding.etPw2.getText().toString())){
            GentleToast.with(getApplicationContext()).longToast("비밀번호가 일치하지 않습니다.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();

            return false;
        }

        else{
            return true;
        }

    }
}
