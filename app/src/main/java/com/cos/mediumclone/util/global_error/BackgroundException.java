package com.cos.mediumclone.util.global_error;

/**
 * Wrapper class for exceptions caught in the background
 */
public class BackgroundException extends Exception{

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
