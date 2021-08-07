package com.cos.mediumclone.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cos.mediumclone.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.adapter.KeywordAdapter;
import com.cos.mediumclone.adapter.PostAdapter;
import com.cos.mediumclone.provider.KeywordProvider;
import com.cos.mediumclone.provider.PostProvider;
import com.cos.mediumclone.util.InitSettings;

public class FragmentHome extends Fragment implements InitSettings{

    private static final String TAG = "FragmentHome";
    private MainActivity mContext;

    private RecyclerView rvKeywords;
    private RecyclerView.LayoutManager layoutManager;
    private KeywordAdapter keywordAdapter;

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;




    public FragmentHome(MainActivity mContext){
        this.mContext = mContext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d(TAG, "onCreateView: context " + getContext());

        rvKeywords = (RecyclerView) view.findViewById(R.id.rvKeywords);
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
        rvKeywords.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        keywordAdapter = new KeywordAdapter();
        rvKeywords.setAdapter(keywordAdapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        postAdapter = new PostAdapter();
        rvPosts.setAdapter(postAdapter);

    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initSetting() {

    }

    @Override
    public void initData() {
        KeywordProvider keywordProvider = new KeywordProvider();
        keywordAdapter.addItems(keywordProvider.findAll());

        PostProvider postProvider = new PostProvider();
        postAdapter.addItems(postProvider.findAll());
    }
}