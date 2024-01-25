package com.example.echat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImg;
    TextView username,profession;
    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImg = itemView.findViewById(R.id.FVprofileImg);
        profession = itemView.findViewById(R.id.FVProfession);
        username = itemView.findViewById(R.id.FVuserName);
    }
}
