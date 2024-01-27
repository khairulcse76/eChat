package com.example.echat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SMSViewHolder extends RecyclerView.ViewHolder {
    TextView secondUserMsg,myMsg;
    CircleImageView secondUserPic,myPic;
    public SMSViewHolder(@NonNull View itemView) {
        super(itemView);
        secondUserMsg=itemView.findViewById(R.id.secondUserTxtView);
        myMsg=itemView.findViewById(R.id.myMsgView);
        secondUserPic=itemView.findViewById(R.id.secondUserProfilePIc);
        myPic = itemView.findViewById(R.id.myProfilePIc);
    }
}
