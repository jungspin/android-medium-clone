package com.cos.mediumclone.view.activity.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cos.mediumclone.R;
import com.cos.mediumclone.controller.UserController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.util.CustomDialog;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "JoinActivity";

    private Context mContext = JoinActivity.this;
    private UserController userController;

    private EditText tfUsername, tfPassword, tfEmail;
    private Button btnJoin;
    private TextView tvLinkLogin;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Log.d(TAG, "onCreate: ");

        init();
        initLr();
    }

    @Override
    public void init() {
        tfUsername = findViewById(R.id.tfUsername);
        tfPassword = findViewById(R.id.tfPassword);
        tfEmail = findViewById(R.id.tfEmail);
        btnJoin = findViewById(R.id.btnJoin);
        tvLinkLogin = findViewById(R.id.tvLinkLogin);

    }

    @Override
    public void initLr() {
        tvLinkLogin.setOnClickListener(v->{
            Intent intent = new Intent(mContext, LoginActivity.class);
            finish();
            startActivity(intent);
        });


        btnJoin.setOnClickListener(v-> {
            String username = tfUsername.getText().toString().trim();
            String password = tfPassword.getText().toString().trim();
            String email = tfEmail.getText().toString().trim();

            // 공백있을 시 가입 불가
            if (username.equals("") || password.equals("") || email.equals("")){
                MyToast.toast(mContext, "모든 칸을 채워주세요");
                return;
            }

            User user = new User(username, password, email);

            CustomDialog.getInstance().showDialog(mContext, "회원가입", "회원가입하시겠습니까?", new CustomDialog.CustomDialogAction() {
                @Override
                public void setPositiveAction() {
                    join(user);
                }

                @Override
                public void setNegativeAction() {
                    Toast.makeText(mContext, "회원가입을 취소했습니다", Toast.LENGTH_SHORT).show();
                }
            });

        });

    }

    private void join(User user) {
        userController = new UserController();
        userController.join(user).enqueue(new Callback<CMRespDTO<User>>() {
            @Override
            public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                Log.d(TAG, "onResponse: 통신 성공: " + response);
                if (response.code() == 500){ // 이거는 원래 넘어가기 전에 다 막아야됨..중복검사로
                    MyToast.toast(mContext, "다시 시도해주세요");
                } else if (response.body().getCode() == 1){
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<CMRespDTO<User>> call, Throwable t) {
                t.printStackTrace();
                MyToast.toast(mContext, "회원가입 실패");
            }
        });
    }

    @Override
    public void initSetting() {
        //tfPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    @Override
    public void initData() {

    }
}