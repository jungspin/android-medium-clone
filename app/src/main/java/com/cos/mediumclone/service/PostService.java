package com.cos.mediumclone.service;

import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostService {

    @GET("/post")
    Call<CMRespDTO<List<Post>>> findAll(@Header("Authorization")String authorization);

    @GET("/post/{id}")
    Call<CMRespDTO<Post>> findById(@Path("id")int postId, @Header("Authorization")String authorization);

    @DELETE("/post/{id}")
    Call<CMRespDTO> deleteById(@Path("id")int postId, @Header("Authorization")String authorization);

    @PUT("/post/{id}")
    Call<CMRespDTO<Post>> updateById(@Path("id")int postId, @Header("Authorization")String authorization
            ,@Body Post post);

    @POST("/post")
    Call<CMRespDTO<Post>> insert(@Header("Authorization")String authorization,@Body Post post);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://172.30.1.15:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    PostService service = retrofit.create(PostService.class);
}
