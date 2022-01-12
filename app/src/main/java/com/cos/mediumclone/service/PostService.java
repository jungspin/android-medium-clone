package com.cos.mediumclone.service;

import com.airbnb.lottie.L;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.schedulers.Schedulers;

<<<<<<< HEAD
public class PostService {
=======
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
            .baseUrl("http://172.30.1.43:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    PostService service = retrofit.create(PostService.class);
>>>>>>> b04c0a5e031b0b7676e05a40afe65ffcc2103001

    private PostAPI postAPI;
    public PostService(RetrofitInstance retrofitInstance){
        postAPI = retrofitInstance.getInstance.create(PostAPI.class);
    }
    public Single<CMRespDTO<List<Post>>> findAll(){
        return postAPI.findAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
