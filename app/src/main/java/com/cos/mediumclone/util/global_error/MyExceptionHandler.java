package com.cos.mediumclone.util.global_error;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "MyExceptionHandler";

    private Application application;
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private Thread.UncaughtExceptionHandler fabricExceptionHandler;

    private Activity lastActivity;
    private int activityCount = 0;

    public MyExceptionHandler(Application application, Thread.UncaughtExceptionHandler defaultExceptionHandler, Thread.UncaughtExceptionHandler fabricExceptionHandler){
        Log.d(TAG, "MyExceptionHandler: ");
        this.application = application;
        this.defaultExceptionHandler = defaultExceptionHandler;
        this.fabricExceptionHandler = fabricExceptionHandler;

        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks(){
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                if (isSkipActivity(activity)){
                    return;
                }
                lastActivity = activity;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                super.onActivityStarted(activity);
                if (isSkipActivity(activity)){
                    return;
                }
                activityCount++;
                lastActivity = activity;
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                super.onActivityStopped(activity);
                if (isSkipActivity(activity)){
                    return;
                }
                activityCount--;
                if (activityCount < 0){
                    lastActivity = null;
                }
            }
        });
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.d(TAG, "lastActivity: " + lastActivity);

        fabricExceptionHandler.uncaughtException(t, e);
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        Log.d(TAG, "writer.toString(): " + writer.toString());
        startErrorActivity(lastActivity, writer.toString());


        Process.killProcess(Process.myPid());
        System.exit(-1);

    }

    private boolean isSkipActivity(Activity activity){
        return activity instanceof ErrorActivity;
    }

    private void startErrorActivity(Activity activity, String errorText){
        Intent intent = new Intent(application.getApplicationContext(), ErrorActivity.class);
        intent.putExtra(ErrorActivity.EXTRA_INTENT, activity.getIntent());
        intent.putExtra(ErrorActivity.EXTRA_ERROR_TEXT, errorText);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent);
        activity.finish();
    }

}
