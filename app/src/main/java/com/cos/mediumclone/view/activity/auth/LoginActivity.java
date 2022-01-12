package com.cos.mediumclone.view.activity.auth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cos.mediumclone.R;
import com.cos.mediumclone.config.LoadingFragment;
import com.cos.mediumclone.config.SessionUser;
import com.cos.mediumclone.controller.UserController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.databinding.ActivityLoginBinding;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.util.CustomDialog;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
import com.cos.mediumclone.view.activity.MainActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
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
    ProgressBar progressBar;

    private Dialog customDialog;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: ");

        checkAppUpdate();
        init();
        initLr();
        initData();
        //getDataFromDb();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        tfPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                userController = new UserController();
                userController.login(new LoginDTO(tfUsername.getText().toString().trim(), tfPassword.getText().toString().trim())).enqueue(new Callback<CMRespDTO<User>>() {
                    @Override
                    public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body().getCode() == 1){

                            User user = response.body().getData();
                            //Log.d(TAG, "onResponse: " + user.getUsername());
                            //Log.d(TAG, "onResponse: " + response.headers().get("Authorization"));
                            SessionUser.user = user;
                            SessionUser.token = response.headers().get("Authorization");

                            Intent intent = new Intent(mContext, MainActivity.class);
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
                return false;
            }
        });




    }

    private void checkAppUpdate(){
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(mContext)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("jungspin", "https://github.com/jungspin/android-medium-clone.git")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", String.valueOf(update.getLatestVersionCode()));
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", String.valueOf(update.getUrlToDownload()));
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
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
//        btnLinkGoogle.setOnClickListener(v->{
//            signIn();
//        });
        // 일반 로그인
        tvLinkJoin.setOnClickListener(v->{

           //showDialog("비회원입니다", "회원가입 하시겠습니까?");
            Post post = new Post();
            post.setTitle("게시글 제목 입니다");
            post.setContent("게시글 내용이에요. 테스트 할거에요");
            post.setCreated("2022-01-01");
            post.setUpdated("2022-01-01");


            CustomDialog customDialog = new CustomDialog(mContext);
            customDialog.showAlertDialog(false,"비회원입니다", post.toString(), () -> {
                Intent intent = new Intent(mContext, JoinActivity.class);
                startActivity(intent);
                finish();
            });



        });

        btnLogin.setOnClickListener(v->{
          /*  progressBar.setVisibility(View.VISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/


            //LoadingFragment.showProgressDialog(this);

            userController = new UserController();
            userController.login(new LoginDTO(tfUsername.getText().toString().trim(), tfPassword.getText().toString().trim())).enqueue(new Callback<CMRespDTO<User>>() {
                @Override
                public void onResponse(Call<CMRespDTO<User>> call, Response<CMRespDTO<User>> response) {
                    Log.d(TAG, "onResponse: " + response);
                    if (response.body().getCode() == 1){

                        User user = response.body().getData();
                        //Log.d(TAG, "onResponse: " + user.getUsername());
                        //Log.d(TAG, "onResponse: " + response.headers().get("Authorization"));
                        SessionUser.user = user;
                        SessionUser.token = response.headers().get("Authorization");

                        Intent intent = new Intent(mContext, MainActivity.class);
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

    private void showDialog(String header, String message){
        customDialog.show();
        TextView title = customDialog.findViewById(R.id.mDialog_title);
        title.setText(header);
        TextView content = customDialog.findViewById(R.id.mDialog_content);
        content.setText(message);
        Button no = customDialog.findViewById(R.id.mDialog_btnNo);
        no.setOnClickListener(v->{
            customDialog.dismiss();
        });
        Button yes = customDialog.findViewById(R.id.mDialog_btnYes);
        yes.setOnClickListener(v->{
            Intent intent = new Intent(mContext, JoinActivity.class);
            startActivity(intent);
            finish();
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
       progressBar.setVisibility(View.INVISIBLE);
       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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