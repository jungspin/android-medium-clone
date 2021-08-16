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
import com.cos.mediumclone.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    private List<User> users = new ArrayList<>();


    // 컬렉션 데이터 셋팅 해야함
    public void addItems(List<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.follow_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        User user = users.get(position);
        holder.setItem(user);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUsername, tvUserInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUserInfo = itemView.findViewById(R.id.tvUserInfo);

        }

        public void setItem(User user){
            tvUsername.setText(user.getUsername());
            tvUserInfo.setText(user.getEmail());
        }
    }
}
