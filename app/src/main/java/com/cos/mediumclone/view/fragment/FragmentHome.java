package com.cos.mediumclone.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cos.mediumclone.config.LoadingFragment;
import com.cos.mediumclone.databinding.FragmentHomeBinding;
import com.cos.mediumclone.model.User;
import com.cos.mediumclone.service.PostService;
import com.cos.mediumclone.service.RetrofitInstance;
import com.cos.mediumclone.util.ResultCode;
import com.cos.mediumclone.view.activity.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.adapter.KeywordAdapter;
import com.cos.mediumclone.adapter.PostAdapter;
import com.cos.mediumclone.controller.PostController;
import com.cos.mediumclone.controller.dto.CMRespDTO;
import com.cos.mediumclone.model.Post;
import com.cos.mediumclone.provider.KeywordProvider;
import com.cos.mediumclone.util.InitSettings;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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

        Log.d(TAG, "onCreateView: context " + getContext());


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
        binding.tvShowSample.setOnClickListener(v->{
           showSampleData();
        });

        String tel = "01025492219";
        String front = tel.substring(0,3);
        String middle = tel.substring(3,7);
        String end = tel.substring(7,11);
        Log.d(TAG, "initNavigation: tels : "+front+"-" +middle+"-"+end);

        StringBuffer buffer = new StringBuffer();
        buffer = buffer.append(tel);
        buffer.insert(3, "-");
        buffer.insert(8, "-");
        Log.d(TAG, "initLr: tel : " + buffer);

    }

    private void showSampleData(){
        List<Post> sampleList = new ArrayList<>();
        User user;
        for (int i=0; i<10; i++){
            user = new User(1, "boribori", "1234", "bori@naver.com", null, null);
            if (i % 2 == 0) {
                user = new User(2, "ssarssar", "1234", "bori@naver.com", null, null);
            }
            sampleList.add(new Post(i, "제목" + i, "내용" + i, user, null, null));
        }
        postAdapter.addItems(sampleList);
        postAdapter.notifyDataSetChanged();
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
        KeywordProvider keywordProvider = new KeywordProvider();
        keywordAdapter.addItems(keywordProvider.findAll());

        postController = new PostController();
        postController.findAll().enqueue(new Callback<CMRespDTO<List<Post>>>() {
            @Override
            public void onResponse(Call<CMRespDTO<List<Post>>> call, Response<CMRespDTO<List<Post>>> response) {
                Log.d(TAG, "onResponse: " + ResultCode.목록보기완료);
                Log.d(TAG, "onResponse: " + response.body());
                List<Post> posts = response.body().getData();
                postAdapter.addItems(posts);
                postAdapter.notifyDataSetChanged();
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