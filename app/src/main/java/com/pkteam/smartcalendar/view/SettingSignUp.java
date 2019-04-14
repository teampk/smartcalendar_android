package com.pkteam.smartcalendar.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
        if(checkJoin()){
            createUser(binding.etName.toString(), binding.etId.getText().toString(), binding.etPw.getText().toString());
        }
    }

    private void createUser(String name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            GentleToast.with(getApplicationContext()).longToast("회원가입 성공").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            finish();
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

                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

        } else {
            Toast.makeText(getApplicationContext(), "Sign in Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkJoin(){


        if(binding.etPw.getText().toString().length()<6){
            Toast.makeText(getApplicationContext(), "비밀번호는 6자 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!binding.etPw.getText().toString().equals(binding.etPw2.getText().toString())){
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        else{
            return true;
        }

    }
}
