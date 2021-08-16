package com.cos.mediumclone.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.cos.mediumclone.R;
import com.cos.mediumclone.bean.SessionUser;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostUpdateActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "PostUpdateActivity";
    private Context mContext = PostUpdateActivity.this;

    private PostController postController = new PostController();

    private EditText tfTitle, tfWriter, tfContent;
    private Button btnUpdate;
    private FloatingActionButton fabFinish;


    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);

        init();
        initLr();
        initSetting();
        initData();
    }

    @Override
    public void init() {
        tfTitle = findViewById(R.id.tfTitle);
        tfWriter = findViewById(R.id.tfWriter);
        tfContent = findViewById(R.id.tfContent);
        btnUpdate = findViewById(R.id.btnUpdate);
        fabFinish = findViewById(R.id.fabFinish);
    }

    @Override
    public void initLr() {
        fabFinish.setOnClickListener(v->{
            finish();
        });
        btnUpdate.setOnClickListener(v->{
            int postId = getIntent().getIntExtra("postId", 0);

            String title = tfTitle.getText().toString();
            String content = tfContent.getText().toString();
            Post post = Post.builder().title(title).content(content).build();

            postController.updateById(postId, post).enqueue(new Callback<CMRespDTO<Post>>() {
                @Override
                public void onResponse(Call<CMRespDTO<Post>> call, Response<CMRespDTO<Post>> response) {
                    Log.d(TAG, "onResponse: " +response.body());
                    Post updatedPost = response.body().getData();
                    if (response.body().getCode() == 1){

                        Intent intent = new Intent(mContext, PostDetailActivity.class);
                        intent.putExtra("postId", updatedPost.getId());
                        finish();
                        startActivity(intent);
                    } else {
                        MyToast.toast(mContext, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<CMRespDTO<Post>> call, Throwable t) {
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
        int postId = getIntent().getIntExtra("postId", 0);
        postController.findById(postId).enqueue(new Callback<CMRespDTO<Post>>() {
            @Override
            public void onResponse(Call<CMRespDTO<Post>> call, Response<CMRespDTO<Post>> response) {
                if (response.body().getCode() == 1){
                    Post post = response.body().getData();
                    //Log.d(TAG, "onResponse: " + post.getTitle());
                    tfTitle.setText(post.getTitle());
                    tfWriter.setText(post.getUser().getUsername());
                    tfContent.setText(post.getContent());
                }
            }

            @Override
            public void onFailure(Call<CMRespDTO<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}