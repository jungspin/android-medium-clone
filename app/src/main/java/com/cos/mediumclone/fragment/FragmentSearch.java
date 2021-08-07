package com.cos.mediumclone.fragment;

import android.content.Context;
import android.os.Bundle;

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
import com.cos.mediumclone.adapter.PostSearchAdapter;
import com.cos.mediumclone.adapter.UserAdapter;
import com.cos.mediumclone.provider.KeywordProvider;
import com.cos.mediumclone.provider.PostProvider;
import com.cos.mediumclone.provider.UserProvider;
import com.cos.mediumclone.util.InitSettings;


public class FragmentSearch extends Fragment implements InitSettings {

    private static final String TAG = "FragmentSearch";
    private Context mContext;

    private RecyclerView rvKeywords;
    private RecyclerView.LayoutManager layoutManager;
    private KeywordAdapter keywordAdapter;

    private RecyclerView rvPosts;
    private PostSearchAdapter postSearchAdapter;

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

        Log.d(TAG, "onCreateView: rvKeywords: " +rvKeywords);
        Log.d(TAG, "onCreateView: rvPosts: " +rvPosts);
        Log.d(TAG, "onCreateView: rvUsers: " +rvUsers);


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
        postSearchAdapter = new PostSearchAdapter();
        rvPosts.setAdapter(postSearchAdapter);

        rvUsers.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        userAdapter = new UserAdapter();
        rvUsers.setAdapter(userAdapter);
    }

    @Override
    public void initData() {

        KeywordProvider keywordProvider = new KeywordProvider();
        keywordAdapter.addItems(keywordProvider.findAll());

        PostProvider postProvider = new PostProvider();
        postSearchAdapter.addItems(postProvider.findAll());

        UserProvider userProvider = new UserProvider();
        userAdapter.addItems(userProvider.findAll());
    }
}