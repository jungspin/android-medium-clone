package com.cos.mediumclone.util;

import android.util.Log;

import com.cos.mediumclone.bean.SessionUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;



public class HeaderInterceptor implements Interceptor {

    private static final String TAG = "HeaderInterceptor";
    private String token;

    public HeaderInterceptor(String token) {
        this.token = token;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Log.d(TAG, "intercept: 실행됨");
        token = SessionUser.token;
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", token).build();
        return chain.proceed(request);
    }
}
