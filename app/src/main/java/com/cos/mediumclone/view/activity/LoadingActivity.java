package com.cos.mediumclone.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cos.mediumclone.R;

public class LoadingActivity extends AppCompatActivity {

    private Context mContext = LoadingActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        startLoading();
    }

    public void startLoading(){
        Handler h = new Handler();
        h.postDelayed(() -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        }, 4000);
    }
}