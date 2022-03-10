package com.cos.mediumclone.service;

import com.cos.mediumclone.BuildConfig;
import com.cos.mediumclone.config.HeaderInterceptor;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.PostUpdateDTO;
import com.cos.mediumclone.model.Post;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostService {

    @GET("/post")
    Call<CMRespDTO<List<Post>>> findAll();

    @GET("/post/{id}")
    Call<CMRespDTO<Post>> findById(@Path("id")int postId);

    @DELETE("/post/{id}")
    Call<CMRespDTO> deleteById(@Path("id")int postId);

    @PUT("/post/{id}")
    Call<CMRespDTO<Post>> updateById(@Path("id")int postId, @Body PostUpdateDTO postUpdateDTO);

    @POST("/post")
    Call<CMRespDTO<Post>> insert(@Body Post post);

    OkHttpClient client = new OkHttpClient.Builder().
            addInterceptor(new HeaderInterceptor()).build();


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    PostService service = retrofit.create(PostService.class);


}
