package com.cos.mediumclone.view.activity.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cos.mediumclone.R;
import com.cos.mediumclone.config.SessionUser;
import com.cos.mediumclone.controller.UserController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
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

    private final Context mContext = LoginActivity.this;
    private UserController userController;

    private EditText tfUsername, tfPassword;
    private Button btnLogin/*, btnLinkGoogle*/;
    private TextView tvLinkJoin;
    ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        init();
        initLr();
        initData();
    }

    @Override
    public void init() {
        tvLinkJoin = findViewById(R.id.tvLinkJoin);
        tfUsername = findViewById(R.id.tfUsername);
        tfPassword = findViewById(R.id.tfPassword);
        btnLogin = findViewById(R.id.btnLogin);
        //btnLinkGoogle = findViewById(R.id.btnLinkGoogle);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void initLr() {
        // 구글 로그인 화면으로 가기
        // 일반 로그인
        tvLinkJoin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, JoinActivity.class);
            startActivity(intent);
            finish();
        });

        btnLogin.setOnClickListener(v -> {
            sendRequest();
        });

        tfPassword.setOnEditorActionListener((textView, i, keyEvent) -> {
            sendRequest();
            return false;
        });

    }

    private void sendRequest() {
        userController = new UserController();
        userController.login(new LoginDTO(tfUsername.getText().toString().trim(), tfPassword.getText().toString().trim()))
                .enqueue(new Callback<CMRespDTO<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<CMRespDTO<User>> call, @NonNull Response<CMRespDTO<User>> response) {
                        if (response.body() != null) {
                            if (response.body().getCode() == 1) {
                                SessionUser.user = response.body().getData();
                                SessionUser.token = response.headers().get("Authorization");

                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                MyToast.toast(mContext, response.body().getMsg());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CMRespDTO<User>> call, @NonNull Throwable t) {
                        MyToast.toast(mContext, "로그인 실패");
                        t.printStackTrace();
                    }
                });
    }


    //============ 구글 로그인을 위한 로직 ==============
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                onSignInResult(result); // 로그인 완료 되면 뭐할건지에 대한 함수를 정의해라
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        Log.d(TAG, "onSignInResult: 구글 로그인 완료된 뒤 콜백 "); // firebase 가 push 해줌
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 세션 정보
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getIdpToken :" + response.getIdpToken());
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getEmail :" + user.getEmail());
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getProviderId :" + user.getProviderId());
            Log.d(TAG, "onSignInResult: 구글 로그인 성공 : getUid: " + user.getUid());


            String username[] = user.getEmail().split("@", 0);


            User googleUser = new User();
            googleUser.setUsername(username[0]);
            googleUser.setPassword(String.valueOf(hashCode()));
            googleUser.setEmail(user.getEmail());

            // 메인화면 진입을 위함 -> 강제 회원가입, 로그인 (토큰 받아야됨)
            userController = new UserController();
            userController.join(googleUser).enqueue(new Callback<CMRespDTO<User>>() {
                @Override
                public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                    if (response.body().getCode() == 1) {
                        Log.d(TAG, "onResponse: 구글 정보로 회원가입 성공 ");
                        userController.login(new LoginDTO(googleUser.getUsername(), googleUser.getPassword())).enqueue(new Callback<CMRespDTO<User>>() {
                            @Override
                            public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                                if (response.body().getCode() == 1) {
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

    private void signIn() {
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
        progressBar.setVisibility(View.INVISIBLE);
    }

}