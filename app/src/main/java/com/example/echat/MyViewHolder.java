package com.example.echat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView post_profile_Image_view;
    TextView PostHeaderUserName,postTimeView,postDescriptionView,likeCount,dislikeCount,commentCount;
    ImageView postImgView,likeIcon,dislikeIcon,commentIcon;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        post_profile_Image_view =itemView.findViewById(R.id.post_profile_Image_view);
        PostHeaderUserName =itemView.findViewById(R.id.PostHeaderUserName);
        postTimeView =itemView.findViewById(R.id.postTimeView);
        postDescriptionView =itemView.findViewById(R.id.postDescriptionView);
        likeCount =itemView.findViewById(R.id.likeCount);
        dislikeCount =itemView.findViewById(R.id.dislikeCount);
        commentCount =itemView.findViewById(R.id.commentCount);
        postImgView =itemView.findViewById(R.id.postImgView);
        likeIcon =itemView.findViewById(R.id.likeIcon);
        dislikeIcon =itemView.findViewById(R.id.dislikeIcon);
        commentIcon =itemView.findViewById(R.id.commentIcon);

    }
}
