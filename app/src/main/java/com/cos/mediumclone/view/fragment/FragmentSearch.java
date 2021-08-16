package com.cos.mediumclone.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cos.mediumclone.view.activity.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.adapter.KeywordAdapter;
import com.cos.mediumclone.adapter.PostAdapter;
import com.cos.mediumclone.adapter.UserAdapter;
import com.cos.mediumclone.bean.SessionUser;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.UserController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.provider.KeywordProvider;
import com.cos.mediumclone.util.InitSettings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSearch extends Fragment implements InitSettings {

    private static final String TAG = "FragmentSearch";
    private MainActivity mContext;
    private PostController postController;
    private UserController userController;

    private RecyclerView rvKeywords;
    private RecyclerView.LayoutManager layoutManager;
    private KeywordAdapter keywordAdapter;

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;

    private RecyclerView rvUsers;
    private UserAdapter userAdapter;

    public FragmentSearch(MainActivity mContext){
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rvKeywords = (RecyclerView) view.findViewById(R.id.rvKeywords);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        rvUsers = (RecyclerView) view.findViewById(R.id.tvUsers);


        initAdapter();
        initData();
        return view;
    }

    @Override
    public void init() {
        // 왜 여기서는 안될까?
//        MainActivity mainActivity = (MainActivity) mContext;
//        rvKeywords = (RecyclerView) mainActivity.findViewById(R.id.rvKeywords);
//        rvPosts = (RecyclerView) mainActivity.findViewById(R.id.rvPosts);
//        rvUsers = (RecyclerView) mainActivity.findViewById(R.id.tvUsers);
    }

    @Override
    public void initLr() {

    }

    @Override
    public void initAdapter() {
        rvKeywords.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        keywordAdapter = new KeywordAdapter();
        rvKeywords.setAdapter(keywordAdapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        postAdapter = new PostAdapter(mContext);
        rvPosts.setAdapter(postAdapter);

        rvUsers.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        userAdapter = new UserAdapter();
        rvUsers.setAdapter(userAdapter);
    }

    @Override
    public void initData() {

        KeywordProvider keywordProvider = new KeywordProvider();
        keywordAdapter.addItems(keywordProvider.findAll());
//
//        PostProvider postProvider = new PostProvider();
//        postSearchAdapter.addItems(postProvider.findAll());
//
//        UserProvider userProvider = new UserProvider();
//        userAdapter.addItems(userProvider.findAll());

        postController = new PostController();
        postController.findAll().enqueue(new Callback<CMRespDTO<List<Post>>>() {
            @Override
            public void onResponse(Call<CMRespDTO<List<Post>>> call, Response<CMRespDTO<List<Post>>> response) {
                Log.d(TAG, "onResponse: " + response.body());
                List<Post> posts = response.body().getData();
                postAdapter.addItems(posts);
            }

            @Override
            public void onFailure(Call<CMRespDTO<List<Post>>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        userController = new UserController();
        userController.findAll().enqueue(new Callback<CMRespDTO<List<User>>>() {
            @Override
            public void onResponse(Call<CMRespDTO<List<User>>> call, Response<CMRespDTO<List<User>>> response) {
                //Log.d(TAG, "onResponse: " + response.body().getData().get(0).getUsername());
                List<User> users = response.body().getData();
                userAdapter.addItems(users);
            }

            @Override
            public void onFailure(Call<CMRespDTO<List<User>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}