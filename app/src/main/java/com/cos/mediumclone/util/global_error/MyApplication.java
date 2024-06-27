package com.cos.mediumclone.util.global_error;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class MyApplication extends Application {

    private static final String TAG = "MyExceptionHandler";

    private Activity lastActivity;
    private int activityCount = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        //setCrashHandler();
        startCatcher();
        //Thread.setDefaultUncaughtExceptionHandler(this::setCrashHandler);
    }


    public void setCrashHandler() {
        Log.d(TAG, "setCrashHandler: ");
        final Thread.UncaughtExceptionHandler deUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

            }
        };

        Thread.UncaughtExceptionHandler fabricExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

            }
        };
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, deUncaughtExceptionHandler, fabricExceptionHandler));
    }

    private void startCatcher() {

        Thread.UncaughtExceptionHandler systemUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
        // the following handler is used to catch exceptions thrown in background threads
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtHandler(new Handler()));

        while (true) {
            try {
                // Starting crash catch Looper
                Looper.loop();
                registerActivityLifecycleCallbacks(callbacks);
                Thread.setDefaultUncaughtExceptionHandler(systemUncaughtHandler);
                throw new RuntimeException("Main thread loop unexpectedly exited");
            } catch (BackgroundException e) {
                // Caught the exception in the background thread + e.threadName + ", TID: " + e.tid, e.getCause());
                showCrashDisplayActivity(e.getCause());
            } catch (Throwable e) {
                // "Caught the exception in the UI thread, e:", e);
                showCrashDisplayActivity(e);
            }
        }
    }

    void showCrashDisplayActivity(Throwable e) {
        Log.d(TAG, "showCrashDisplayActivity: ");
        Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
        intent.putExtra(ErrorActivity.EXTRA_INTENT, lastActivity.getIntent());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ErrorActivity.EXTRA_ERROR_TEXT, e);
        lastActivity.startActivity(intent);
    }

    /**
     * This handler catches exceptions in the background threads and propagates them to the UI thread
     */
    static class UncaughtHandler implements Thread.UncaughtExceptionHandler {

        private final Handler mHandler;

        UncaughtHandler(Handler handler) {
            mHandler = handler;
        }

        public void uncaughtException(Thread thread, final Throwable e) {
            final int tid = Process.myTid();
            final String threadName = thread.getName();
            mHandler.post(new Runnable() {
                public void run() {
                    throw new BackgroundException(e, tid, threadName);
                }
            });
        }
    }

    /**
     * Wrapper class for exceptions caught in the background
     */
    static class BackgroundException extends RuntimeException {

        final int tid;
        final String threadName;

        /**
         * @param e          original exception
         * @param tid        id of the thread where exception occurred
         * @param threadName name of the thread where exception occurred
         */
        BackgroundException(Throwable e, int tid, String threadName) {
            super(e);
            this.tid = tid;
            this.threadName = threadName;
        }
    }

    private boolean isSkipActivity(Activity activity){
        return activity instanceof ErrorActivity;
    }

    Application.ActivityLifecycleCallbacks callbacks = new SimpleActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
            if (isSkipActivity(activity)) {
                return;
            }
            lastActivity = activity;
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            super.onActivityStarted(activity);
            if (isSkipActivity(activity)) {
                return;
            }
            activityCount++;
            lastActivity = activity;
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            super.onActivityStopped(activity);
            if (isSkipActivity(activity)) {
                return;


            }
        }
    };
}


