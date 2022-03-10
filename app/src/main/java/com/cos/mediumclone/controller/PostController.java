package com.cos.mediumclone.controller;

import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.PostUpdateDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.service.PostService;

import java.util.List;

import retrofit2.Call;

public class PostController {

    private PostService postServiceOriginal = PostService.service;

    public Call<CMRespDTO<List<Post>>> findAll(){
        return postServiceOriginal.findAll();
    }

    public Call<CMRespDTO<Post>> findById(int id){
        return postServiceOriginal.findById(id);
    }

    public Call<CMRespDTO<Post>> updateById(int id, PostUpdateDTO postUpdateDTO){
        return postServiceOriginal.updateById(id, postUpdateDTO);
    }

    public Call<CMRespDTO> deleteById(int id){
        return postServiceOriginal.deleteById(id);
    }

    public Call<CMRespDTO<Post>> insert(Post post){
        return postServiceOriginal.insert(post);
    }
}
