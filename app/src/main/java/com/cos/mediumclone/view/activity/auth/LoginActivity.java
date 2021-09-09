package com.cos.mediumclone.view.activity.auth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cos.mediumclone.R;
import com.cos.mediumclone.config.SessionUser;
import com.cos.mediumclone.controller.UserController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
import com.cos.mediumclone.view.activity.LoadingActivity;
import com.cos.mediumclone.view.activity.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "LoginActivity";

    private Context mContext = LoginActivity.this;
    private UserController userController;

    private EditText tfUsername, tfPassword;
    private Button btnLogin/*, btnLinkGoogle*/;
    private TextView tvLinkJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: ");

        init();
        initLr();
        //getDataFromDb();


    }


    @Override
    public void init() {
        tvLinkJoin = findViewById(R.id.tvLinkJoin);
        tfUsername = findViewById(R.id.tfUsername);
        tfPassword = findViewById(R.id.tfPassword);
        btnLogin = findViewById(R.id.btnLogin);
        //btnLinkGoogle = findViewById(R.id.btnLinkGoogle);
    }

    @Override
    public void initLr() {
        // 구글 로그인 화면으로 가기
//        btnLinkGoogle.setOnClickListener(v->{
//            signIn();
//        });
        // 일반 로그인
        tvLinkJoin.setOnClickListener(v->{
            Intent intent = new Intent(mContext, JoinActivity.class);
            finish();
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v->{

            String username = tfUsername.getText().toString().trim();
            String password = tfPassword.getText().toString().trim();

            // 공백있을 시 로그인 불가
            if (username.equals("") || password.equals("")){
                MyToast.toast(mContext, "아이디와 비밀번호룰 입력해주세요");
                return;
            }

            userController = new UserController();
            userController.login(new LoginDTO(username, password)).enqueue(new Callback<CMRespDTO<User>>() {
                @Override
                public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                    Log.d(TAG, "onResponse: " + response);
                    if (response.body().getCode() == 1){

                        User user = response.body().getData();
                        //Log.d(TAG, "onResponse: " + user.getUsername());
                        //Log.d(TAG, "onResponse: " + response.headers().get("Authorization"));
                        SessionUser.user = user;
                        SessionUser.token = response.headers().get("Authorization");
                        Intent intent = new Intent(mContext, LoadingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<CMRespDTO<User>> call, Throwable t) {
                    t.printStackTrace();
                    MyToast.toast(mContext, "로그인 실패");
                }
            });
        });

    }

    //============ 구글 로그인을 위한 로직 ==============
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                onSignInResult(result); // 로그인 완료 되면 뭐할건지에 대한 함수를 정의해라
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result){
        Log.d(TAG, "onSignInResult: 구글 로그인 완료된 뒤 콜백 "); // firebase 가 push 해줌
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 세션 정보
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getIdpToken :" + response.getIdpToken());
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getEmail :" + user.getEmail());
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getProviderId :" + user.getProviderId());
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getUid: " + user.getUid());


            String username[] = user.getEmail().split("@", 0);

            User googleUser = User.builder()
                    .username(username[0])
                    .password(hashCode()+"")
                    .email(user.getEmail())
                    .build();

            // 메인화면 진입을 위함 -> 강제 회원가입, 로그인 (토큰 받아야됨)
            userController = new UserController();
            userController.join(googleUser).enqueue(new Callback<CMRespDTO<User>>() {
                @Override
                public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                    if (response.body().getCode()==1){
                        Log.d(TAG, "onResponse: 구글 정보로 회원가입 성공 ");
                        userController.login(new LoginDTO(googleUser.getUsername(), googleUser.getPassword())).enqueue(new Callback<CMRespDTO<User>>() {
                            @Override
                            public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                                if (response.body().getCode()==1) {
                                    Log.d(TAG, "onResponse: 구글 정보로 로그인 성공");
                                    // PK가 겹치치 않아서 같은 정보로 회원가입 계속 될것임..이건 테스트니까 알고만 있자
                                    // 회원가입이 계속 되는게 아니라 오류가 나는듯..? 서버 재실행하고 하면 됨
                                    SessionUser.user = response.body().getData();
                                    SessionUser.token = response.headers().get("Authorization");
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                            @Override
                            public void onFailure(Call<CMRespDTO<User>> call, Throwable t) {
                                Log.d(TAG, "onResponse: 구글 정보로 로그인 실패");
                                t.printStackTrace();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<CMRespDTO<User>> call, Throwable t) {
                    Log.d(TAG, "onResponse: 구글 정보로 회원가입 실패");
                    t.printStackTrace();
                }
            });

        } else {
            Log.d(TAG, "onSignInResult: 구글 로그인 실패 : " + response.getError());
            Log.d(TAG, "onSignInResult: 구글 로그인 실패 : " + response.toString());
        }
    }

    private void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        Log.d(TAG, "signIn: 구글 로그인 화면으로 이동");
    }

    //============ 구글 로그인을 위한 로직 끝 ==============

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}