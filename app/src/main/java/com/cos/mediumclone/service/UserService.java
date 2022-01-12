package com.cos.mediumclone.service;

import com.cos.mediumclone.BuildConfig;
import com.cos.mediumclone.config.HeaderInterceptor;
import com.cos.mediumclone.config.SessionUser;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.model.User;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
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



    // 특정 주소 걸 수 있는지 알아봐. 유저는 토큰 필요 없는 것도 있잖아
    // 첫번째 방법은 갈라치기 해서 쓰는거
    OkHttpClient client = new OkHttpClient.Builder().
            addInterceptor(new HeaderInterceptor()).build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            //.client(client)
            .build();

//    Retrofit retrofit2 = new Retrofit.Builder()
//            .client(client)
//            .baseUrl("http://172.30.1.7:8080")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();

    UserService service = retrofit.create(UserService.class);
}
