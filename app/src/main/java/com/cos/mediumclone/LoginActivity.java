package com.cos.mediumclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cos.mediumclone.bean.SessionUser;
import com.cos.mediumclone.controller.UserController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.util.InitSettings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "LoginActivity";

    private Context mContext = LoginActivity.this;
    private UserController userController;

    private EditText tfUsername, tfPassword;
    private Button btnLogin;
    private TextView tvLinkJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initLr();
    }

    @Override
    public void init() {
        tvLinkJoin = findViewById(R.id.tvLinkJoin);
        tfUsername = findViewById(R.id.tfUsername);
        tfPassword = findViewById(R.id.tfPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    public void initLr() {
        tvLinkJoin.setOnClickListener(v->{
            Intent intent = new Intent(mContext, JoinActivity.class);
            finish();
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v->{
            String username = tfUsername.getText().toString().trim();
            String password = tfPassword.getText().toString().trim();

            userController = new UserController();
            userController.login(new LoginDTO(username, password)).enqueue(new Callback<CMRespDTO<User>>() {
                @Override
                public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                    Log.d(TAG, "onResponse: " + response);
                    User user = response.body().getData();
                    //Log.d(TAG, "onResponse: " + user.getUsername());
                    //Log.d(TAG, "onResponse: " + response.headers().get("Authorization"));
                    SessionUser.user = user;
                    SessionUser.token = response.headers().get("Authorization");
                    Intent intent = new Intent(mContext, LoadingActivity.class);
                    finish();
                    startActivity(intent);

                }

                @Override
                public void onFailure(Call<CMRespDTO<User>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });

    }

    @Override
    public void initData() {

    }
}