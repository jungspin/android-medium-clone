package com.cos.mediumclone.config;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private static final String TAG = "HeaderInterceptor";


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Log.d(TAG, "intercept: 실행됨");

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", SessionUser.token).build();
        return chain.proceed(request);
    }
}
