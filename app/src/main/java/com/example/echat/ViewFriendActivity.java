package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewFriendActivity extends AppCompatActivity {

    FirebaseAuth myAuth;
    FirebaseUser myUser;
    DatabaseReference userRef;

    String ProfileImgUrl, username,fullName,city,country;

    CircleImageView profileImg;
    TextView UserName,inputFullName,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        UserName = findViewById(R.id.userFullName);
        address = findViewById(R.id.address);
        profileImg = findViewById(R.id.FVProfileImg);

        String userID = getIntent().getStringExtra("userKey");
        Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();

        myAuth=FirebaseAuth.getInstance();
        myUser=myAuth.getCurrentUser();
        assert userID != null;
        userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        LoadUsers();

    }

    private void LoadUsers() {
        userRef.addValueEventListener(new ValueEventListener() {
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
    }
}