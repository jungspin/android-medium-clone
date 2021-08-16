package com.cos.mediumclone.helper;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cos.mediumclone.view.activity.auth.LoginActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.bean.SessionUser;

public class CustomAppbarActivity extends AppCompatActivity {

    private static final String TAG = "CustomAppBarActivity";

    protected void settingToolBar(){ // 자식만 호출할 수 있게

        Toolbar myToolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

    }

//    protected void settingToolBar(boolean isBackButton){ // 자식만 호출할 수 있게
//
//        Toolbar myToolbar = findViewById(R.id.myToolBar);
//        setSupportActionBar(myToolbar);
//        // 뒤로가기를 위한 설정
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(isBackButton);
//
//    }


    // 툴바 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actLogout){
            Log.d(TAG, "onOptionsItemSelected: actLogout");
            SessionUser.token="";
            SessionUser.user=null;

            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            finish();
            startActivity(intent);

            return true;
        } else if (item.getItemId() == R.id.actWrite){
            Log.d(TAG, "onOptionsItemSelected: actWrite");
//            Intent intent = new Intent(getBaseContext(), PostWriteActivity.class);
//            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.actUserInfo){
            Log.d(TAG, "onOptionsItemSelected: actUserInfo");
//            Intent intent = new Intent(getBaseContext(), UserInfoActivity.class);
//            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
