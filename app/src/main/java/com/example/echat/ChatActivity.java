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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.echat.ui.theme.ChatModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.util.Objects;

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
    DatabaseReference userRef, smsRef;

    String otherUserNameDB,otherUserProfileImgUrlDB,otherUserStatusDB;
    String myProfileImgUrl;

    private final int POST_IMG_REQ_CODE=101;
    Uri imgUri;

    FirebaseRecyclerOptions<ChatModel>options;
    FirebaseRecyclerAdapter<ChatModel,SMSViewHolder>adapter;
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
        smsRef= FirebaseDatabase.getInstance().getReference().child("Messages");



        //Toast.makeText(this, otherUserID.toString(), Toast.LENGTH_SHORT).show();

        LoadOtherUser();
        LoadMyProfile();
        LoadMessages("");
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSms();
            }
        });
        btnUploadChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iUploadImg = new Intent(Intent.ACTION_GET_CONTENT);
                iUploadImg.setType("image/*");
                startActivityForResult(iUploadImg, POST_IMG_REQ_CODE);
            }
        });
    }

    private void LoadMyProfile() {
        userRef.child(myUsers.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    myProfileImgUrl=snapshot.child("profileImage").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadMessages(String s) {
        options=new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(smsRef.child(myUsers.getUid()).child(otherUserID), ChatModel.class).build();
        adapter=new FirebaseRecyclerAdapter<ChatModel, SMSViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SMSViewHolder holder, int position, @NonNull ChatModel model) {
                if (model.getUserID().equals(myUsers.getUid())){
                    holder.secondUserMsg.setVisibility(View.GONE);
                    holder.secondUserPic.setVisibility(View.GONE);
                    holder.myMsg.setVisibility(View.VISIBLE);
                    holder.myPic.setVisibility(View.VISIBLE);

                    holder.myMsg.setText(model.getSms());
                    Picasso.get().load(myProfileImgUrl).into(holder.myPic);
                }else {
                    holder.secondUserMsg.setVisibility(View.VISIBLE);
                    holder.secondUserPic.setVisibility(View.VISIBLE);
                    holder.myMsg.setVisibility(View.GONE);
                    holder.myPic.setVisibility(View.GONE);

                    holder.secondUserMsg.setText(model.getSms());
                    Picasso.get().load(otherUserProfileImgUrlDB).into(holder.secondUserPic);
                }
            }

            @NonNull
            @Override
            public SMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_view_sms,parent,false);
                return new SMSViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void SendSms() {
        String sms=inputMsg.getText().toString();
        if (sms.isEmpty()){
            //inputMsg.setError("Please Write something..");
            showerrors(inputMsg,"Please Write something..");
        }else {
            HashMap hashMap= new HashMap<>();
            hashMap.put("sms",sms);
            hashMap.put("status","unseen");
            /*if (!imgUri.equals(null)){
                hashMap.put("imgUrl",imgUri.toString());
            }*/
            hashMap.put("userID", myUsers.getUid());
            smsRef.child(otherUserID).child(myUsers.getUid()).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        smsRef.child(myUsers.getUid()).child(otherUserID).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    inputMsg.setText("");
                                    Toast.makeText(ChatActivity.this, "Message Send", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ChatActivity.this, "Msg not saved in Database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

        }
    }

    private void showerrors(EditText field, String errorMsg) {
        field.setError(errorMsg);
        field.requestFocus();
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