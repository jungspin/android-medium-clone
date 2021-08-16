package com.cos.mediumclone.view.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cos.mediumclone.R;
import com.cos.mediumclone.bean.SessionUser;
import com.cos.mediumclone.controller.UserController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
import com.cos.mediumclone.view.activity.LoadingActivity;

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
                        finish();
                        startActivity(intent);
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

    @Override
    public void initData() {

    }
}