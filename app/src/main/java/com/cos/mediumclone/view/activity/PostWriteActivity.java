package com.cos.mediumclone.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cos.mediumclone.R;
import com.cos.mediumclone.config.SessionUser;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.util.MyToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostWriteActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "PostWriteActivity";
    private Context mContext = PostWriteActivity.this;

    private PostController postController;

    private EditText etTitle;
    private RichEditor mEditor;
    private FloatingActionButton fabFinish, fabSave;

    //private MyToast myToast = new MyToast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        init();
        initLr();
        initSetting();
        initData();

    }

    @Override
    public void init() {
        etTitle = findViewById(R.id.etTitle);
        mEditor = findViewById(R.id.mEditor);
        fabSave = findViewById(R.id.fabSave);
        fabFinish = findViewById(R.id.fabFinish);
    }

    @Override
    public void initLr() {

        fabFinish.setOnClickListener(v->{
            Intent intent = new Intent(mContext, MainActivity.class);
            finish();
            startActivity(intent);
        });

        fabSave.setOnClickListener(v->{
            String title = etTitle.getText().toString();
            String content = mEditor.getHtml();
            Spanned content2 = Html.fromHtml(content);

            Log.d(TAG, "initLr: fabSave : " + title);
            Log.d(TAG, "initLr: fabSave : " + content);
            Log.d(TAG, "initLr: fabSave : " + content2);

            Post post = Post.builder().title(title).content(content).build();
            postController = new PostController();
            postController.insert(post).enqueue(new Callback<CMRespDTO<Post>>() {
                @Override
                public void onResponse(Call<CMRespDTO<Post>> call, Response<CMRespDTO<Post>> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Post writtenPost = response.body().getData();
                    Log.d(TAG, "onResponse: 결과 : " + writtenPost.getTitle());


                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra("postId", writtenPost.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //finish();
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<CMRespDTO<Post>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                    MyToast.toast(mContext, "통신 실패");
                }
            });

        });

    }

    @Override
    public void initSetting() {
        Toolbar myToolbar = findViewById(R.id.postWriteToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

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

    }
}