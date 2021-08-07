package com.cos.mediumclone.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cos.mediumclone.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.adapter.PostSearchAdapter;
import com.cos.mediumclone.provider.PostProvider;
import com.cos.mediumclone.util.InitSettings;


public class FragmentRecently extends Fragment implements InitSettings {

    private static final String TAG = "FragmentRecently";
    private MainActivity mContext;

    private RecyclerView rvPosts;
    private PostSearchAdapter postSearchAdapter;

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
        postSearchAdapter = new PostSearchAdapter();
        rvPosts.setAdapter(postSearchAdapter);

    }

    @Override
    public void initSetting() {

    }

    @Override
    public void initData() {

        PostProvider postProvider = new PostProvider();
        postSearchAdapter.addItems(postProvider.findAll());
    }
}