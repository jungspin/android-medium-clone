package com.cos.mediumclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mediumclone.R;
import com.cos.mediumclone.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostSearchAdapter extends RecyclerView.Adapter<PostSearchAdapter.MyViewHolder>{

    private List<Post> posts = new ArrayList<>();




    // 컬렉션 데이터 셋팅 해야함
    public void addItems(List<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.post_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostSearchAdapter.MyViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.setItem(post);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvWriter, tvTitle;
        //private ImageView imgUser, imgThumb, icAddBookmark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWriter = itemView.findViewById(R.id.tvWriter);
            tvTitle = itemView.findViewById(R.id.tvTitle);

        }

        public void setItem(Post post){
            tvWriter.setText(post.getWriter());
            tvTitle.setText(post.getTitle());
        }
    }
}
