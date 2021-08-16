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
import com.cos.mediumclone.adapter.PostAdapter;
import com.cos.mediumclone.bean.SessionUser;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.util.InitSettings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentRecently extends Fragment implements InitSettings {

    private static final String TAG = "FragmentRecently";
    private MainActivity mContext;
    private PostController postController;

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;

    public FragmentRecently(MainActivity mContext){
        this.mContext = mContext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently, container, false);

        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

        initAdapter();
        initData();
        return view;
    }

    @Override
    public void init() {

    }

    @Override
    public void initLr() {

    }

    @Override
    public void initAdapter() {
        rvPosts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        postAdapter = new PostAdapter(mContext);
        rvPosts.setAdapter(postAdapter);

    }

    @Override
    public void initSetting() {

    }

    @Override
    public void initData() {

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
    }
}