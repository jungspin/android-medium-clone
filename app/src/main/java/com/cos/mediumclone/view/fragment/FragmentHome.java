package com.cos.mediumclone.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cos.mediumclone.view.activity.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.adapter.KeywordAdapter;
import com.cos.mediumclone.adapter.PostAdapter;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.provider.KeywordProvider;
import com.cos.mediumclone.util.InitSettings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment implements InitSettings{

    private static final String TAG = "FragmentHome";
    private MainActivity mContext;
    private PostController postController;

    private RecyclerView rvKeywords;
    private RecyclerView.LayoutManager layoutManager;
    private KeywordAdapter keywordAdapter;

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FragmentHome(MainActivity mContext){
        this.mContext = mContext;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d(TAG, "onCreateView: context " + getContext());

        swipeRefreshLayout = view.findViewById(R.id.swipeLy);

        rvKeywords = (RecyclerView) view.findViewById(R.id.rvKeywords);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

        initAdapter();
        initSetting();
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
        rvKeywords.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        keywordAdapter = new KeywordAdapter();
        rvKeywords.setAdapter(keywordAdapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        postAdapter = new PostAdapter(mContext);
        rvPosts.setAdapter(postAdapter);

    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initSetting() {
        swipeRefreshLayout.setColorSchemeResources(R.color.green);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: ");
                initData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void initData() {
        KeywordProvider keywordProvider = new KeywordProvider();
        keywordAdapter.addItems(keywordProvider.findAll());
//
//        PostProvider postProvider = new PostProvider();
//        postAdapter.addItems(postProvider.findAll());

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