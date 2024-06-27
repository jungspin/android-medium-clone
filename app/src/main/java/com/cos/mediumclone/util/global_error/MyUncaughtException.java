package com.cos.mediumclone.util.global_error;

import android.os.Handler;
import android.os.Process;

import androidx.annotation.NonNull;


/**
 * This handler catches exceptions in the background threads and propagates them to the UI thread
 */
public class MyUncaughtException implements Thread.UncaughtExceptionHandler{

    private Handler handler;

    public MyUncaughtException(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        final int tid = Process.myTid();
        final String threadName = thread.getName();
        handler.post(() -> {

            try {
                throw new BackgroundException(throwable, tid, threadName);
            } catch (BackgroundException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Wrapper class for exceptions caught in the background
     */
    public static class BackgroundException extends Exception{

        private final int tid;
        private final String threadName;

        /**
         * @param throwable original exception
         * @param tid id of the thread where exception occurred
         * @param threadName name of the thread where exception occurred
         */
        public BackgroundException(Throwable throwable, int tid, String threadName){
            super(throwable);
            this.tid = tid;
            this.threadName = threadName;
        }

    }


}
