package com.example.echat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCommentViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImg;
    TextView userName,cmtView;
    public MyCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImg =  itemView.findViewById(R.id.UpdateprofileImgView);
        userName =  itemView.findViewById(R.id.single_cmtUserName);
        cmtView =  itemView.findViewById(R.id.single_cmtView);

    }
}
