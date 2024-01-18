package com.example.echat;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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

    public void countLike(String postKey, String uid, DatabaseReference likeRef) {
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   int totalCount = (int) snapshot.getChildrenCount();
                   String countValue = String.valueOf(totalCount);
                   likeCount.setText(countValue);
                    //Log.d("Like_count", "Likes count: " + countValue);
                }else {
                    likeCount.setText("0");
                    //Log.d("Like_count", "No likes found for the post");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Like_count", "Error: " + error.getMessage());
            }
        });
        likeRef.child(postKey).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    likeIcon.setColorFilter(Color.BLUE);
                }else {
                    likeIcon.setColorFilter(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void countDislike(String postKey, String uid, DatabaseReference disLikeRef) {
        disLikeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalCount = (int) snapshot.getChildrenCount();
                    dislikeCount.setText(totalCount+"");
                }else {
                    dislikeCount.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Dislike_count","Dislike Error" + error.getMessage());
            }
        });
        disLikeRef.child(postKey).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    dislikeIcon.setColorFilter(Color.RED);
                }else {
                    dislikeIcon.setColorFilter(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Dislike_count","Dislike text Error" + error.getMessage());
            }
        });
    }
}
