package com.cos.mediumclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cos.mediumclone.fragment.FragmentHome;
import com.cos.mediumclone.fragment.FragmentProfile;
import com.cos.mediumclone.fragment.FragmentSearch;
import com.cos.mediumclone.util.InitSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookmarkActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "BookmarkActivity";
    private Context mContext = BookmarkActivity.this;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
    }

    @Override
    public void init() {

    }

    @Override
    public void initLr() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // 이렇게하면 돌아올때 새로고침 -> 나중에 로직 변경
            switch (item.getItemId()){
                case  R.id.navHome:
                    selectedFragment = new FragmentHome((MainActivity) mContext);
                    break;
                case R.id.navSearch:
                    selectedFragment = new FragmentSearch((MainActivity) mContext);
                    break;
                case R.id.navBookmark:
                    Intent intent = new Intent(
                            mContext, BookmarkActivity.class
                    );
                    startActivity(intent);
                    break;
                case R.id.navProfile:
                    selectedFragment = new FragmentProfile();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();
            return true;
        });

    }

    @Override
    public void initAdapter() {

    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initSetting() {

    }

    @Override
    public void initData() {

    }
}