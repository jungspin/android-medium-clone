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
        // ?????? ????????? ???????????? ??????
        // ?????? ?????????
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
                        MyToast.toast(mContext, "????????? ??????");
                        t.printStackTrace();
                    }
                });
    }


    //============ ?????? ???????????? ?????? ?????? ==============
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                onSignInResult(result); // ????????? ?????? ?????? ??????????????? ?????? ????????? ????????????
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        Log.d(TAG, "onSignInResult: ?????? ????????? ????????? ??? ?????? "); // firebase ??? push ??????
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // ?????? ??????
            Log.d(TAG, "onSignInResult: ?????? ????????? ?????? : getIdpToken :" + response.getIdpToken());
            Log.d(TAG, "onSignInResult: ?????? ????????? ?????? : getEmail :" + user.getEmail());
            Log.d(TAG, "onSignInResult: ?????? ????????? ?????? : getProviderId :" + user.getProviderId());
            Log.d(TAG, "onSignInResult: ?????? ????????? ?????? : getUid: " + user.getUid());


            String username[] = user.getEmail().split("@", 0);


            User googleUser = new User();
            googleUser.setUsername(username[0]);
            googleUser.setPassword(String.valueOf(hashCode()));
            googleUser.setEmail(user.getEmail());

            // ???????????? ????????? ?????? -> ?????? ????????????, ????????? (?????? ????????????)
            userController = new UserController();
            userController.join(googleUser).enqueue(new Callback<CMRespDTO<User>>() {
                @Override
                public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                    if (response.body().getCode() == 1) {
                        Log.d(TAG, "onResponse: ?????? ????????? ???????????? ?????? ");
                        userController.login(new LoginDTO(googleUser.getUsername(), googleUser.getPassword())).enqueue(new Callback<CMRespDTO<User>>() {
                            @Override
                            public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                                if (response.body().getCode() == 1) {
                                    Log.d(TAG, "onResponse: ?????? ????????? ????????? ??????");
                                    // PK??? ????????? ????????? ?????? ????????? ???????????? ?????? ?????????..?????? ??????????????? ????????? ??????
                                    // ??????????????? ?????? ????????? ????????? ????????? ?????????..? ?????? ??????????????? ?????? ???
                                    SessionUser.user = response.body().getData();
                                    SessionUser.token = response.headers().get("Authorization");
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                            @Override
                            public void onFailure(Call<CMRespDTO<User>> call, Throwable t) {
                                Log.d(TAG, "onResponse: ?????? ????????? ????????? ??????");
                                t.printStackTrace();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<CMRespDTO<User>> call, Throwable t) {
                    Log.d(TAG, "onResponse: ?????? ????????? ???????????? ??????");
                    t.printStackTrace();
                }
            });

        } else {
            Log.d(TAG, "onSignInResult: ?????? ????????? ?????? : " + response.getError());
            Log.d(TAG, "onSignInResult: ?????? ????????? ?????? : " + response.toString());
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
        Log.d(TAG, "signIn: ?????? ????????? ???????????? ??????");
    }

    //============ ?????? ???????????? ?????? ?????? ??? ==============

    @Override
    public void initData() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}