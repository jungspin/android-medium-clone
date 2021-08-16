package com.cos.mediumclone.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cos.mediumclone.view.activity.MainActivity;
import com.cos.mediumclone.R;

public class FragmentHighlighted extends Fragment {

    private static final String TAG = "FragmentHighlighted";
    private MainActivity mContext;

    public FragmentHighlighted(MainActivity mContext){
        this.mContext = mContext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highlighted, container, false);
        return view;
    }
}