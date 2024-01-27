package com.example.echat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ChatActivity extends AppCompatActivity {

    String otherUserID;
    CircleImageView profileimgView;
    TextView otherUserName,otherUserStatus;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ImageView btnUploadChatImg, btnSendMsg;
    EditText inputMsg;

    FirebaseAuth myAuth;
    FirebaseUser myUsers;
    DatabaseReference userRef;

    String otherUserNameDB,otherUserProfileImgUrlDB,otherUserStatusDB;

    private final int POST_IMG_REQ_CODE=101;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        profileimgView=findViewById(R.id.chatProfileImg);
        otherUserName=findViewById(R.id.chatOtherusername);
        otherUserStatus=findViewById(R.id.txtVStatus);
        btnUploadChatImg=findViewById(R.id.btnUploadChatImg);
        btnSendMsg=findViewById(R.id.btnSendMsg);
        inputMsg=findViewById(R.id.inputMsg);

        toolbar=findViewById(R.id.ChatToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        recyclerView=findViewById(R.id.recyleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        otherUserID=getIntent().getStringExtra("otherUserID").toString();

        myAuth=FirebaseAuth.getInstance();
        myUsers=myAuth.getCurrentUser();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");

        //Toast.makeText(this, otherUserID.toString(), Toast.LENGTH_SHORT).show();

        LoadOtherUser();
        btnUploadChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iUploadImg = new Intent(Intent.ACTION_GET_CONTENT);
                iUploadImg.setType("image/*");
                startActivityForResult(iUploadImg, POST_IMG_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==POST_IMG_REQ_CODE && resultCode==RESULT_OK & data!=null){
            imgUri=data.getData();
            btnUploadChatImg.setImageURI(imgUri);

        }
    }

    private void LoadOtherUser() {
        userRef.child(otherUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(ChatActivity.this, "Data Loaded", Toast.LENGTH_LONG).show();
                    otherUserNameDB=snapshot.child("fullName").getValue().toString();
                    otherUserProfileImgUrlDB=snapshot.child("profileImage").getValue().toString();
                    otherUserStatusDB=snapshot.child("status").getValue().toString();

                    Picasso.get().load(otherUserProfileImgUrlDB).into(profileimgView);
                    otherUserName.setText(otherUserNameDB);
                    otherUserStatus.setText(otherUserStatusDB);

                }else {
                    Toast.makeText(ChatActivity.this, "Data not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}