package com.cos.mediumclone.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cos.mediumclone.R;
import com.cos.mediumclone.config.LoadingFragment;
import com.cos.mediumclone.config.SessionUser;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "PostDetailActivity";

    private Context mContext = PostDetailActivity.this;
    private PostController postController;

    private TextView tvTitle, tvWriter, tvContent;
    private Button btnUpdate, btnDelete;
    private FloatingActionButton fabFinish;

    private MyToast myToast = new MyToast();

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        init();
        initLr();
        //initSetting();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvWriter = findViewById(R.id.tvWriter);
        tvContent = findViewById(R.id.tvContent);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        fabFinish = findViewById(R.id.fabFinish);
    }

    @Override
    public void initLr() {
        int postId = getIntent().getIntExtra("postId", 0);

        fabFinish.setOnClickListener(v->{
            Intent intent = new Intent(mContext, MainActivity.class); // 서버에 재요청해서 데이터 받음
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        btnUpdate.setOnClickListener(v->{

            Intent intent = new Intent(mContext, PostUpdateActivity.class);
            intent.putExtra("postId", postId);
            startActivity(intent);

        });
        btnDelete.setOnClickListener(v->{
            postController.deleteById(postId).enqueue(new Callback<CMRespDTO>() {
                @Override
                public void onResponse(Call<CMRespDTO> call, Response<CMRespDTO> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    if (response.body().getCode() == 1){
                        // 이전 페이지 다 삭제하고 리스트만 남게해서!!
                        // finish 하면 뒤로 갈때 마다 다운로드 다시 받게 되니까 -> ux를 생각해보면 된다
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        myToast.toast(mContext, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<CMRespDTO> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });


    }

    @Override
    public void initSetting() {


    }

    @Override
    public void initData() {

        postController = new PostController();
        int postId = getIntent().getIntExtra("postId", 0);
        postController.findById(postId).enqueue(new Callback<CMRespDTO<Post>>() {
            @Override
            public void onResponse(Call<CMRespDTO<Post>> call, Response<CMRespDTO<Post>> response) {
                if (response.body().getCode() == 1){
                    Post post = response.body().getData();
                    //Log.d(TAG, "onResponse: " + post.getTitle());
                    tvTitle.setText(post.getTitle());
                    tvWriter.setText(post.getUser().getUsername());
                    tvContent.setText(post.getContent());

                    if (!SessionUser.user.getUsername().equals(post.getUser().getUsername())){
                        btnUpdate.setVisibility(View.INVISIBLE);
                        btnDelete.setVisibility(View.INVISIBLE);
                    } else {
                        btnUpdate.setVisibility(View.VISIBLE);
                        btnDelete.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<CMRespDTO<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}