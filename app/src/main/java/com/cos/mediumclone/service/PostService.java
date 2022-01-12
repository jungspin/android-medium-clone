package com.cos.mediumclone.service;

import com.airbnb.lottie.L;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class PostService {

    private PostAPI postAPI;
    public PostService(RetrofitInstance retrofitInstance){
        postAPI = retrofitInstance.getInstance.create(PostAPI.class);
    }
    public Single<CMRespDTO<List<Post>>> findAll(){
        return postAPI.findAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
