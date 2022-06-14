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

import com.cos.mediumclone.databinding.FragmentHomeBinding;
import com.cos.mediumclone.util.ResultCode;
import com.cos.mediumclone.view.activity.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.adapter.KeywordAdapter;
import com.cos.mediumclone.adapter.PostAdapter;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.util.InitSettings;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment implements InitSettings{

    private static final String TAG = "FragmentHome";
    private MainActivity mContext;
    private PostController postController;

    private RecyclerView.LayoutManager layoutManager;
    private KeywordAdapter keywordAdapter;

    private PostAdapter postAdapter;


    private FragmentHomeBinding binding;
    ShimmerFrameLayout shimmerFrameLayout ;


    public FragmentHome(MainActivity mContext){
        this.mContext = mContext;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        shimmerFrameLayout = binding.shimmerFrameLayout;
        shimmerFrameLayout.startShimmer();


        initAdapter();
        initSetting();
        initData();
        initLr();
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
        binding.rvKeywords.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        keywordAdapter = new KeywordAdapter();
        binding.rvKeywords.setAdapter(keywordAdapter);

        binding.rvPosts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        postAdapter = new PostAdapter(mContext);
        binding.rvPosts.setAdapter(postAdapter);

    }

    @Override
    public void initNavigation() {


    }

    @Override
    public void initSetting() {
        binding.swipeLy.setColorSchemeResources(R.color.green);
        binding.swipeLy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: ");
                initData();
                binding.swipeLy.setRefreshing(false);
            }
        });
    }

    @Override
    public void initData() {
        List<String> keywords = new ArrayList<>();
        for(int i=0; i<10;i++){
            keywords.add("keyword");
        }
        keywordAdapter.addItems(keywords);

        postController = new PostController();
        postController.findAll().enqueue(new Callback<CMRespDTO<List<Post>>>() {
            @Override
            public void onResponse(Call<CMRespDTO<List<Post>>> call, Response<CMRespDTO<List<Post>>> response) {
                Log.d(TAG, "onResponse: " + ResultCode.목록보기완료);
                Log.d(TAG, "onResponse: " + response.body());
                List<Post> posts = response.body().getData();
                postAdapter.addItems(posts);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<CMRespDTO<List<Post>>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}