package com.cos.mediumclone.service;

import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {

    @POST("/login")
    Call<CMRespDTO<User>> findByUsernameAndPassword(@Body LoginDTO loginDTO);

    @POST("/join")
    Call<CMRespDTO<User>> insert(@Body User user);

    @GET("/init/user")
    Call<CMRespDTO<List<User>>> initUser();

    @GET("/user/{id}")
    Call<CMRespDTO> findById(@Header("Authorization")String authorization);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://172.30.1.15:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);
}
