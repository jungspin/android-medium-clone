package com.cos.mediumclone.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.cos.mediumclone.R;
import com.cos.mediumclone.config.LoadingFragment;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.PostUpdateDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostUpdateActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "PostUpdateActivity";
    private Context mContext = PostUpdateActivity.this;

    private PostController postController = new PostController();


    private EditText etTitle;
    private RichEditor mEditor;
    private FloatingActionButton fabFinish, fabUpdate;


    // postId 전역으로 관리하기!!!
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
        etTitle = findViewById(R.id.etTitle);
        mEditor = findViewById(R.id.mEditor);
        fabUpdate = findViewById(R.id.fabUpdate);
        fabFinish = findViewById(R.id.fabFinish);
    }

    @Override
    public void initLr() {
        fabFinish.setOnClickListener(v->{
            finish();
        });

        fabUpdate.setOnClickListener(v->{

            int postId = getIntent().getIntExtra("postId", 0);

            String title = etTitle.getText().toString();
            String content = mEditor.getHtml();
            PostUpdateDTO postUpdateDTO = new PostUpdateDTO(title, content);

            postController.updateById(postId, postUpdateDTO).enqueue(new Callback<CMRespDTO<Post>>() {
                @Override
                public void onResponse(Call<CMRespDTO<Post>> call, Response<CMRespDTO<Post>> response) {
                    Log.d(TAG, "onResponse: " +response.body());
                    Post updatedPost = response.body().getData();
                    if (response.body().getCode() == 1){
                        // intent 하면 포스트 아이디를 가져가야함
                        //Intent intent = new Intent(mContext, PostDetailActivity.class);
                        //intent.putExtra("postId", updatedPost.getId());
                        // finish 하면 디테일의 resume 에서 데이터 다운 받아야됨
                        finish();
                        //startActivity(intent);
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
        // 에디터 설정
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        findViewById(R.id.actionHeading1).setOnClickListener(v -> {
            mEditor.setHeading(1);
        });
        findViewById(R.id.actionBlockquote).setOnClickListener(v -> {
            mEditor.setBlockquote();
        });

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
                    etTitle.setText(post.getTitle());
                    mEditor.setHtml(post.getContent());
                }
            }

            @Override
            public void onFailure(Call<CMRespDTO<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}