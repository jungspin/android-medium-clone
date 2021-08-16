package com.cos.mediumclone.controller;

import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.controller.dto.LoginDTO;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.service.UserService;

import java.util.List;

import retrofit2.Call;

public class UserController {
    private UserService userService = UserService.service;

    public Call<CMRespDTO<User>> login(LoginDTO loginDTO){
        return userService.findByUsernameAndPassword(loginDTO);
    }

    public Call<CMRespDTO<User>> join(User user){
        return userService.insert(user);
    }

    public Call<CMRespDTO<List<User>>> findAll(){
        return userService.initUser();
    }
}
