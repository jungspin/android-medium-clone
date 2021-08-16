package com.cos.mediumclone.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cos.mediumclone.view.activity.MainActivity;
import com.cos.mediumclone.view.activity.PostWriteActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.bean.SessionUser;
import com.cos.mediumclone.util.InitSettings;
import com.cos.mediumclone.view.activity.auth.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentProfile extends Fragment implements InitSettings {

    private static final String TAG = "FragmentProfile";
    private MainActivity mContext;

    private FloatingActionButton fabLogout;
    private TextView tvUsername;
    private AppCompatButton acbWriteForm;

    public FragmentProfile(MainActivity mContext){
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fabLogout = view.findViewById(R.id.fabLogout);
        tvUsername = view.findViewById(R.id.tvUsername);
        acbWriteForm = view.findViewById(R.id.acbWriteForm);

        initLr();
        initData();
        return view;
    }

    @Override
    public void init() {

    }

    @Override
    public void initLr() {
        fabLogout.setOnClickListener(v->{
            SessionUser.user=null;
            SessionUser.token=null;
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.finish();
            startActivity(intent);
        });
        acbWriteForm.setOnClickListener(v->{
            Log.d(TAG, "initLr: 글쓰기 가기 ");
            Intent intent = new Intent(mContext, PostWriteActivity.class);

            startActivity(intent);
        });

    }

    @Override
    public void initData() {
        tvUsername.setText(SessionUser.user.getUsername());
    }
}