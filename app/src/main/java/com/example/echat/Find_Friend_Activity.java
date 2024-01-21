package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.echat.ui.theme.Utills.users_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

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
        Query query = userRef.orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<users_model>().setQuery(query, users_model.class).build();
        adapter=new FirebaseRecyclerAdapter<users_model, MyFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyFriendViewHolder holder, int position, @NonNull users_model model) {
                if (!myUsers.getUid().equals(getRef(position).getKey())){
                    Picasso.get().load(model.getProfileImage()).into(holder.profileImg);
                    holder.username.setText(model.getFullName());
                    holder.profession.setText(model.getProfession());
                }else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
            }

            @NonNull
            @Override
            public MyFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_find_friend_view,parent,false);
                return new MyFriendViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem= menu.findItem(R.id.search);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setDrawingCacheBackgroundColor(Color.MAGENTA);
        //searchIcon.setColorFilter(ContextCompat.getColor(this, R.color.customSearchIconColor), PorterDuff.Mode.SRC_IN);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadUsers(newText);
                return true;
            }
        });
        return true;
    }
}