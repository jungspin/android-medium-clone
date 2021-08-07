package com.cos.mediumclone.helper;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cos.mediumclone.BookmarkActivity;
import com.cos.mediumclone.MainActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.fragment.FragmentHome;
import com.cos.mediumclone.fragment.FragmentProfile;
import com.cos.mediumclone.fragment.FragmentSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper extends AppCompatActivity {

    public static void enableNav(Context mContext, BottomNavigationView bottomNavigation){
        MainActivity main = (MainActivity) mContext;

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // 이렇게하면 돌아올때 새로고침 -> 나중에 로직 변경
            switch (item.getItemId()){
                case  R.id.navHome:
                    selectedFragment = new FragmentHome(main);
                    break;
                case R.id.navSearch:
                    selectedFragment = new FragmentSearch(main);
                    break;
                case R.id.navBookmark:
                    Intent intent = new Intent(
                            main, BookmarkActivity.class
                    );
                    main.startActivity(intent);
                    break;
                case R.id.navProfile:
                    selectedFragment = new FragmentProfile();
                    break;
            }
            main.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();
            return true;
        });
    }
}
