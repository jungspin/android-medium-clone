package com.cos.mediumclone.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.cos.mediumclone.R;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = "LoadingActivity";

    private Context mContext = LoadingActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Log.d(TAG, "onCreate: ");

        startLoading();
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

    public void startLoading(){
        Handler h = new Handler();
        h.postDelayed(() -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 액티비티가 스택에 쌓이지 않게함

            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }, 4000);
    }
}