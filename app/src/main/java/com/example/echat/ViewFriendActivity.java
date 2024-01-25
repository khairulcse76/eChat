package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewFriendActivity extends AppCompatActivity {

    FirebaseAuth myAuth;
    FirebaseUser myUser;
    DatabaseReference userRef, requestRef, friendRef;

    String userID,  ProfileImgUrl, username,fullName,city,country, profession, myProfileImgUrl, myusername, myfullName, mycity, mycountry, myprofession;

    CircleImageView profileImg;
    TextView UserName,inputFullName,address;
    Button btnSendRequ,btnDeclineRequ;
    String CurrentState = "nothing_happen";
    String CurrentState_Friend="Friend";
    String CurrentState_pending="Pending";
    String CurrentState_decline="Decline";
    String CurrentState_Unfriend="Unfriend";
    String He_Request_sent_pending="He_Request_sent_pending";
    String He_Request_sent_decline="He_Request_sent_pending";
    String I_Request_sent_pending="I_Request_sent_pending";
    String I_Request_sent_decline="I_Request_sent_decline";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        UserName = findViewById(R.id.userFullName);
        address = findViewById(R.id.address);
        profileImg = findViewById(R.id.FVProfileImg);
        btnDeclineRequ = findViewById(R.id.btnDeclineRequ);
        btnSendRequ = findViewById(R.id.btnSendRequ);

        userID = getIntent().getStringExtra("userKey");

        Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();

        myAuth=FirebaseAuth.getInstance();
        myUser=myAuth.getCurrentUser();
        assert userID != null;
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        requestRef= FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef= FirebaseDatabase.getInstance().getReference().child("Friends");

        LoadUsers(userID);
        LoadMyProfile();
        btnSendRequ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(userID);
            }
        });// btn send Request Close
        checkUserExistence(userID);
        btnDeclineRequ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnfriendPerform(userID);
            }
        });
    } // on Create Close



    private void UnfriendPerform(String userID) {
        if (CurrentState.equals(CurrentState_Friend)){
            friendRef.child(myUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        friendRef.child(userID).child(myUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ViewFriendActivity.this, "you are unfriend..", Toast.LENGTH_SHORT).show();
                                    CurrentState="nothing_happen";
                                    btnSendRequ.setText("Send Friend Request");
                                    btnDeclineRequ.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });
        }//friend current state close
        if (CurrentState.equals(He_Request_sent_pending)){
            HashMap hashMap=new HashMap();
            hashMap.put("status","decline");

            requestRef.child(userID).child(myUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        CurrentState = He_Request_sent_decline;
                        btnDeclineRequ.setVisibility(View.GONE);
                        btnSendRequ.setText("Send Friend Request");
                    }
                }
            });
        }
    }

    private void checkUserExistence(String userID) {
        friendRef.child(myUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CurrentState=CurrentState_Friend;
                    btnSendRequ.setText("Send SMS");
                    btnSendRequ.setBackgroundColor(getColor(R.color.colorgreen));
                    btnDeclineRequ.setText("Unfriend");
                    btnDeclineRequ.setBackgroundColor(getColor(R.color.Red));
                    btnDeclineRequ.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        friendRef.child(userID).child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CurrentState=CurrentState_Friend;
                    btnSendRequ.setText("Send SMS");
                    btnSendRequ.setBackgroundColor(getColor(R.color.colorgreen));
                    btnDeclineRequ.setText("Unfriend");
                    btnDeclineRequ.setBackgroundColor(getColor(R.color.Red));
                    btnDeclineRequ.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(myUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals(CurrentState_pending)){
                        CurrentState = I_Request_sent_pending;
                        btnSendRequ.setText("Cancel Friend Request");
                        btnSendRequ.setBackgroundColor(Color.MAGENTA);
                        btnDeclineRequ.setVisibility(View.GONE);
                    }
                    if (snapshot.child("status").getValue().toString().equals(CurrentState_decline)){
                        CurrentState = I_Request_sent_decline;
                        btnSendRequ.setText("Cancel Friend Request");
                        btnSendRequ.setBackgroundColor(Color.MAGENTA);
                        btnDeclineRequ.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }); //request Ref close
        requestRef.child(userID).child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals(CurrentState_pending)){
                        CurrentState=He_Request_sent_pending;
                        btnSendRequ.setText("Accept Friend Request");
                        btnDeclineRequ.setText("Decline Friend Request");
                        btnDeclineRequ.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (CurrentState.equals("nothing_happen")){
            btnDeclineRequ.setVisibility(View.GONE);
            btnSendRequ.setText("Send Friend Request");
        }
    }

    private void PerformAction(String userID) {
        if (CurrentState.equals("nothing_happen")){
            HashMap hashMap=new HashMap<>();
            hashMap.put("status",CurrentState_pending);
            requestRef.child(myUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        btnSendRequ.setText("Cancel Friend Request");
                        btnSendRequ.setBackgroundColor(Color.MAGENTA);
                        CurrentState = I_Request_sent_pending;
                        btnDeclineRequ.setVisibility(View.GONE);
                        Toast.makeText(ViewFriendActivity.this, "Request Send", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }//if nothing_happen Close
        if (CurrentState.equals(I_Request_sent_pending) || CurrentState.equals(I_Request_sent_decline)){
            requestRef.child(myUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ViewFriendActivity.this, "You Have Canceled Friend Request", Toast.LENGTH_LONG).show();
                        CurrentState = "nothing_happen";
                        btnSendRequ.setText("Send Friend Request");
                        btnSendRequ.setBackgroundColor(getColor(R.color.colorgreen));
                        btnDeclineRequ.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }//Request_sent_pending condition Close

        if (CurrentState.equals(He_Request_sent_pending)){
            requestRef.child(userID).child(myUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        final HashMap hashMap=new HashMap<>();
                        hashMap.put("status",CurrentState_Friend);
                        hashMap.put("username", username);
                        hashMap.put("fullName", fullName);
                        hashMap.put("ProfileImgUrl", ProfileImgUrl);
                        hashMap.put("profession", profession);

                        friendRef.child(myUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    final HashMap myHashMap=new HashMap<>();
                                    myHashMap.put("status",CurrentState_Friend);
                                    myHashMap.put("username", myusername);
                                    myHashMap.put("fullName", myfullName);
                                    myHashMap.put("ProfileImgUrl", myProfileImgUrl);
                                    myHashMap.put("profession", myprofession);
                                    friendRef.child(userID).child(myUser.getUid()).updateChildren(myHashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            CurrentState=CurrentState_Friend;
                                            Toast.makeText(ViewFriendActivity.this, "Friend Request Accepted", Toast.LENGTH_SHORT).show();
                                            btnSendRequ.setText("Send SMS");
                                            btnSendRequ.setBackgroundColor(getColor(R.color.colorgreen));

                                            btnDeclineRequ.setText("Unfriend");
                                            btnDeclineRequ.setBackgroundColor(getColor(R.color.Red));
                                            btnDeclineRequ.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }


                                else {
                                    Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }// He_Request_sent_pending Close
        if (CurrentState.equals(CurrentState_Friend)){
            Toast.makeText(this, "send Friend not configure", Toast.LENGTH_LONG).show();
        }
    }

    private void LoadUsers(String userID) {
        userRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("profileImage").exists() && snapshot.child("profileImage").getValue()!=null){
                        ProfileImgUrl=snapshot.child("profileImage").getValue().toString();
                    }
                    if (snapshot.child("username").exists() && snapshot.child("username").getValue()!=null){
                        username=snapshot.child("username").getValue().toString();
                    }
                    if (snapshot.child("fullName").exists() && snapshot.child("fullName").getValue()!=null){
                        fullName=snapshot.child("fullName").getValue().toString();
                    }
                    if (snapshot.child("city").exists() && snapshot.child("city").getValue()!=null){
                        city=snapshot.child("city").getValue().toString();
                    }
                    if (snapshot.child("country").exists() && snapshot.child("country").getValue()!=null){
                        country=snapshot.child("country").getValue().toString();
                    }
                    if (snapshot.child("profession").exists() && snapshot.child("profession").getValue()!=null){
                        profession=snapshot.child("profession").getValue().toString();
                    }

                    UserName.setText(fullName + " ("+ username +")" );
                    Picasso.get().load(ProfileImgUrl).into(profileImg);
                    address.setText(city + ", " + country);

                }else {
                    Toast.makeText(ViewFriendActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }//load user Close
    private void LoadMyProfile() {
        userRef.child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //
                if (snapshot.exists()){
                    if (snapshot.child("profileImage").exists() && snapshot.child("profileImage").getValue()!=null){
                        myProfileImgUrl=snapshot.child("profileImage").getValue().toString();
                    }
                    if (snapshot.child("username").exists() && snapshot.child("username").getValue()!=null){
                        myusername=snapshot.child("username").getValue().toString();
                    }
                    if (snapshot.child("fullName").exists() && snapshot.child("fullName").getValue()!=null){
                        myfullName=snapshot.child("fullName").getValue().toString();
                    }
                    if (snapshot.child("city").exists() && snapshot.child("city").getValue()!=null){
                        mycity=snapshot.child("city").getValue().toString();
                    }
                    if (snapshot.child("country").exists() && snapshot.child("country").getValue()!=null){
                        mycountry=snapshot.child("country").getValue().toString();
                    }
                    if (snapshot.child("profession").exists() && snapshot.child("profession").getValue()!=null){
                        myprofession=snapshot.child("profession").getValue().toString();
                    }

                }else {
                    Toast.makeText(ViewFriendActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
}