package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.echat.ui.theme.Utills.Friend_Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ChatListActivity extends AppCompatActivity {
    Toolbar toolbar;

    FirebaseRecyclerAdapter<Friend_Model,FriendViewHolder> adapter;
    FirebaseRecyclerOptions<Friend_Model> options;
    RecyclerView recyclerView;
    FirebaseAuth myAuth;
    FirebaseUser myUser;
    DatabaseReference userRef, friendRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView=findViewById(R.id.friendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar=findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat>>Friend>>List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAuth = FirebaseAuth.getInstance();
        myUser = myAuth.getCurrentUser();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        friendRef= FirebaseDatabase.getInstance().getReference().child("Friends");

        LoadFriend("");
    }

    private void LoadFriend(String s) {
        Query query=friendRef.child(myUser.getUid()).orderByChild("username").startAt(s).endAt(s+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<Friend_Model>().setQuery(query,Friend_Model.class).build();
        adapter=new FirebaseRecyclerAdapter<Friend_Model, FriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull Friend_Model model) {
                if (!myUser.getUid().equals(getRef(position).getKey())) {
                    Picasso.get().load(model.getProfileImgUrl()).into(holder.profileImg);
                    holder.username.setText(model.getFullName() + "(" + model.getUsername() + ")");
                    holder.profession.setText(model.getProfession());
                }
                /*Picasso.get().load(model.getProfileImgUrl()).into(holder.profileImg);
                holder.username.setText(model.getFullName() + "(" + model.getUsername() + ")");
                holder.profession.setText(model.getProfession());*/

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(ChatListActivity.this,ChatActivity.class);
                        intent.putExtra("otherUserID",getRef(holder.getAdapterPosition()).getKey().toString());
                        startActivity(intent);
                    }
                });

            }
            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_friend_view,parent,false);
                return new FriendViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}