package com.cos.mediumclone.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.cos.mediumclone.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.adapter.BookmarkFragAdapter;
import com.cos.mediumclone.util.InitSettings;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class FragmentBookmark extends Fragment implements InitSettings {

    private static final String TAG = "FragmentBookmark";
    private MainActivity mContext;

    private ViewPager2 vpContainer;
    private TabLayout tabLayout;
    private BookmarkFragAdapter bookmarkFragAdapter;


    public FragmentBookmark(MainActivity mContext){
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: context : " + getContext());
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        vpContainer = (ViewPager2) mContext.findViewById(R.id.vpContainer);
        tabLayout = mContext.findViewById(R.id.tabLayout);

        initAdapter();
        initSetting();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void init() {

    }

    @Override
    public void initLr() {

    }

    @Override
    public void initAdapter() {
        bookmarkFragAdapter = new BookmarkFragAdapter(mContext);


        bookmarkFragAdapter.addFragment(new FragmentSaved(mContext));
        bookmarkFragAdapter.addFragment(new FragmentHighlighted(mContext));
        bookmarkFragAdapter.addFragment(new FragmentRecently(mContext));
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initSetting() {
        // tab 설정
        new TabLayoutMediator(tabLayout, vpContainer, (tab, position) -> {}).attach();

        tabLayout.getTabAt(0).setText("Saved");
        tabLayout.getTabAt(1).setText("Highlighted");
        tabLayout.getTabAt(2).setText("Recently viewed");

    }

    @Override
    public void initData() {

    }
}