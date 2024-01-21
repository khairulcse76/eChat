package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.echat.ui.theme.Utills.users_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Find_Friend_Activity extends AppCompatActivity {

    Toolbar myToolbar;
    FirebaseAuth myAuth;
    FirebaseUser myUsers;
    DatabaseReference userRef;

    FirebaseRecyclerAdapter<users_model,MyFriendViewHolder>adapter;
    FirebaseRecyclerOptions<users_model>options;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        recyclerView =findViewById(R.id.recylerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myToolbar = findViewById(R.id.app_bar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Find Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAuth = FirebaseAuth.getInstance();
        myUsers=myAuth.getCurrentUser();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        LoadUsers("");

    }

    private void LoadUsers(String s) {
        Query query = userRef.orderByChild("usernaem").startAt(s).endAt(s+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<users_model>().setQuery(query, users_model.class).build();
        adapter=new FirebaseRecyclerAdapter<users_model, MyFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyFriendViewHolder holder, int position, @NonNull users_model model) {
                
            }

            @NonNull
            @Override
            public MyFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_find_friend_view,parent,false);
                return new MyFriendViewHolder(view);
            }
        };
    }
}