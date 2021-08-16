package com.cos.mediumclone.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.cos.mediumclone.R;
import com.cos.mediumclone.view.fragment.FragmentBookmark;
import com.cos.mediumclone.view.fragment.FragmentHome;
import com.cos.mediumclone.view.fragment.FragmentProfile;
import com.cos.mediumclone.view.fragment.FragmentSearch;
import com.cos.mediumclone.util.InitSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "MainActivity2";
    private MainActivity mContext = MainActivity.this;

    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        initLr();
        initSetting();
        initNavigation();
        initData();
    }


    @Override
    public void init() {
        fragmentContainer = findViewById(R.id.fragmentContainer);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    @Override
    public void initLr() {

    }

    @Override
    public void initAdapter() {

    }

    @Override
    public void initNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // 이렇게하면 돌아올때 새로고침 -> 나중에 로직 변경
            switch (item.getItemId()){
                case  R.id.navHome:
                    selectedFragment = new FragmentHome(mContext);
                    break;
                case R.id.navSearch:
                    selectedFragment = new FragmentSearch(mContext);
                    break;
                case R.id.navBookmark:
                    selectedFragment = new FragmentBookmark(mContext);
                    break;
                case R.id.navProfile:
                    selectedFragment = new FragmentProfile(mContext);
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();
            return true;
        });
    }

    @Override
    public void initSetting() {

    }

    @Override
    public void initData() {
        // 초기 화면 셋팅
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new FragmentHome(mContext), null)
                .commit();
    }
}