package com.cos.mediumclone.service;

import com.cos.mediumclone.config.HeaderInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final RetrofitInstance instance = new RetrofitInstance();
    private RetrofitInstance(){} // 외부에서 new 하는 것을 막음
    public static RetrofitInstance getInstance(){
        return instance;
    }

    OkHttpClient client = new OkHttpClient.Builder().
            addInterceptor(new HeaderInterceptor()).build();


    Retrofit getInstance = new Retrofit.Builder()
            .baseUrl("http://192.168.1.103:8080")
            //.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
}
