package com.cos.mediumclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mediumclone.config.LoadingFragment;
import com.cos.mediumclone.view.activity.PostDetailActivity;
import com.cos.mediumclone.R;
import com.cos.mediumclone.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    private static final String TAG = "PostAdapter";
    private Context mContext;
    //private PostController postController;

    private List<Post> posts = new ArrayList<>();

    public PostAdapter(Context mContext) {
        this.mContext = mContext;
    }



    // 컬렉션 데이터 셋팅 해야함
    public void addItems(List<Post> posts){
        this.posts = posts;
    }

    // 컬렉션 데이터 셋팅 해야함
    public void addItem(Post post){
        this.posts.add(post);
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
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.setItem(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvWriter, tvTitle, tvGender;
        //private ImageView imgUser, imgThumb, icAddBookmark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            init();
            initLr();

        }

        public void setItem(Post post){
          if (post.getUser().getUsername().equals("ssarssar")){
              tvGender.getBackground().setTint(Color.parseColor("#97D6FF"));
              tvGender.setText("여");
          }
            tvWriter.setText(post.getUser().getUsername());
            tvTitle.setText(post.getTitle());
        }

        private void init(){
            tvWriter = itemView.findViewById(R.id.tvWriter);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvGender = itemView.findViewById(R.id.tvGender);

        }

        private void initLr(){
            itemView.setOnClickListener(v->{
                Log.d(TAG, "initLr: 클릭됨 : " + getAdapterPosition());

                // 어댑터 포지션에 위치한 post 데이터를 가져옴
                Post post = posts.get(getAdapterPosition());
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("postId", post.getId());
                mContext.startActivity(intent);
            });
        }
    }
}
