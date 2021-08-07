package com.cos.mediumclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mediumclone.BuildConfig;
import com.cos.mediumclone.R;
import com.cos.mediumclone.model.Keyword;

import java.util.ArrayList;
import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.MyViewHolder>{

    private List<Keyword> keywords = new ArrayList<>();


    // 컬렉션 데이터 셋팅 해야함
    public void addItems(List<Keyword> keywords){
        this.keywords = keywords;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.keyword_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordAdapter.MyViewHolder holder, int position) {
        Keyword keyword = keywords.get(position);
        holder.setItem(keyword);

    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private Button btnKeyword;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            btnKeyword = itemView.findViewById(R.id.btnKeyword);
        }

        public void setItem(Keyword keyword){
            btnKeyword.setText(keyword.getKeyword());
        }
    }
}
