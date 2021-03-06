package com.pkteam.smartcalendar.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pkteam.smartcalendar.R;
import com.pkteam.smartcalendar.databinding.FragmentSettingLoginBinding;
import com.singh.daman.gentletoast.GentleToast;

import java.util.Arrays;

public class SettingLogin extends AppCompatActivity {

    FragmentSettingLoginBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 10;
    private static final int RC_SIGN_UP_OK = 11;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_setting_login);
        binding.setLogin(this);

        mAuth = FirebaseAuth.getInstance();
        // Google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Facebook Login
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                GentleToast.with(getApplicationContext()).longToast("facebook 로그인 : 취소되었습니다.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                binding.pbSignIn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(FacebookException error) {
                GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_network)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                binding.pbSignIn.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RC_SIGN_IN:
                    mCallbackManager.onActivityResult(requestCode, resultCode, data);
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_network)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                        binding.pbSignIn.setVisibility(View.INVISIBLE);
                    }
                    break;
                case RC_SIGN_UP_OK:
                    finish();
                    break;
            }
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

        }
    }


    public void signInListener(View view){
        binding.pbSignIn.setVisibility(View.VISIBLE);
        if(CheckLogin()) {

            mAuth.signInWithEmailAndPassword(binding.etId.getText().toString(), binding.etPw.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        } catch (FirebaseNetworkException e) {
                            GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_network)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_sign_in)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                        } catch (FirebaseAuthInvalidUserException e){
                            GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_wrong_email)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                        } catch (Exception e) {
                            GentleToast.with(getApplicationContext()).longToast(getString(R.string.error_sign_in)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                        }
                    } else {
                        GentleToast.with(getApplicationContext()).longToast(getString(R.string.sign_in_success)).setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                        finish();
                    }
                    binding.pbSignIn.setVisibility(View.INVISIBLE);

                }


            });
        }
    }


    public void signUpListener(View view){
        Intent intentSignUp = new Intent(getApplicationContext(), SettingSignUp.class);
        startActivityForResult(intentSignUp, RC_SIGN_UP_OK);
    }

    public void findPasswordListener(View view){
        GentleToast.with(getApplicationContext()).longToast("비밀번호 찾기").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();

    }

    // Google Button Listener
    public void googleLoginListener(View view){
        binding.pbSignIn.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // firebase 로 값을 넘겨주는 부분
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // google 사용자일 때
                        if (task.isSuccessful()) {
                            GentleToast.with(getApplicationContext()).longToast("Google 로그인 완료").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            GentleToast.with(getApplicationContext()).longToast("Google Login : ERROR").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();

                        }
                        binding.pbSignIn.setVisibility(View.INVISIBLE);
                    }
                });
    }



    // Facebook Button Listener
    public void facebookLoginListener(View view){
        binding.pbSignIn.setVisibility(View.VISIBLE);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            GentleToast.with(getApplicationContext()).longToast("Facebook 로그인 완료").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            GentleToast.with(getApplicationContext()).longToast("Facebook Login : ERROR").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
                            updateUI(null);
                        }
                        binding.pbSignIn.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d("SIGNINLOG", "uid"+user.getProviderId()+"/name"+user.getDisplayName());
            finish();
        } else {
            Log.d("SIGNINLOG", "ERROR");
        }
    }

    private boolean CheckLogin(){
        if (binding.etId.getText().toString().length()==0){
            GentleToast.with(getApplicationContext()).longToast("아이디를 입력해주세요.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
            binding.pbSignIn.setVisibility(View.INVISIBLE);
            return false;
        }else if (binding.etPw.getText().toString().length()==0){
            GentleToast.with(getApplicationContext()).longToast("비밀번호를 입력해주세요.").setTextColor(R.color.material_white_1000).setBackgroundColor(R.color.colorPrimary).setBackgroundRadius(100).setImage(R.drawable.logo_ts).show();
            binding.pbSignIn.setVisibility(View.INVISIBLE);
            return false;
        }

        else{
            return true;
        }
    }

    public void finishView(View view){
        finish();
    }

}
