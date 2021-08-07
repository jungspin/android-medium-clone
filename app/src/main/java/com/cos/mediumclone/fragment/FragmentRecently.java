package com.cos.mediumclone.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cos.mediumclone.MainActivity;
import com.cos.mediumclone.R;


public class FragmentRecently extends Fragment {

    private static final String TAG = "FragmentRecently";
    private MainActivity mContext;

    public FragmentRecently(MainActivity mContext){
        this.mContext = mContext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently, container, false);
        return view;
    }
}