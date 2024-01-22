package com.example.echat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.echat.ui.theme.Utills.Post_Model;
import com.example.echat.ui.theme.Utills.comment_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drToggle;

    //Firebase Import
    FirebaseAuth myAuth;
    FirebaseUser myUser;
    DatabaseReference dbRef, postRef, likeRef, disLikeRef, commentRef;
    StorageReference storeImgRef;

    String profileImgV,fullNameV, userNameV;

    CircleImageView navHeaderImgV;
    TextView navHeaderUserName, input_description;
    ImageView upload_img, btn_upload_post;

    private final int POST_IMG_REQ_CODE=101;
    Uri  imgUri;

    ProgressDialog myLoading_Dialog;

    //Recycler Adapter
    FirebaseRecyclerAdapter<Post_Model,MyViewHolder> adapter;
    FirebaseRecyclerOptions<Post_Model>options;

    // comment Adapter
    FirebaseRecyclerAdapter<comment_model, MyCommentViewHolder>cmtAdapter;
    FirebaseRecyclerOptions<comment_model>ctmOptions;

    RecyclerView recylContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        upload_img = findViewById(R.id.img_add_post);
        btn_upload_post = findViewById(R.id.upload_post);
        input_description = findViewById(R.id.edtCreatePost);


        recylContainer = findViewById(R.id.recylContainer);

        recylContainer.setLayoutManager(new LinearLayoutManager(this));



        myAuth = FirebaseAuth.getInstance();
        myUser = myAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        storeImgRef = FirebaseStorage.getInstance().getReference().child("PostImage");
        //faching and store like and dislike db reference
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        disLikeRef = FirebaseDatabase.getInstance().getReference().child("Dislikes");

        // for comment add
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_3line);
        }

        myLoading_Dialog = new ProgressDialog(this);


        View view = navigationView.inflateHeaderView(R.layout.nav_header_layout);
        navHeaderImgV = view.findViewById(R.id.UpdateprofileImgView);
        navHeaderUserName = view.findViewById(R.id.txtViewUser);

        btn_upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });

        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iUploadImg = new Intent(Intent.ACTION_GET_CONTENT);
                iUploadImg.setType("image/*");
                startActivityForResult(iUploadImg, POST_IMG_REQ_CODE);
            }
        });


        ActionBarDrawerToggle drToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(drToggle);
        drToggle.syncState();


        //Fragment Manager

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home){
                    Intent intent  = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Click to home", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_Chat) {
                    Toast.makeText(MainActivity.this, "Click to Chat", Toast.LENGTH_SHORT).show();
                }else if (id==R.id.nav_friend){
                    Toast.makeText(MainActivity.this, "Click to Friend", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_find_friend) {
                    Intent intent  = new Intent(MainActivity.this,Find_Friend_Activity.class);
                    startActivity(intent);
                } else if (id==R.id.nav_profile) {
                    Intent intent  = new Intent(MainActivity.this,Profile_Activity.class);
                    startActivity(intent);/*
                    LoadFragment(new ProfileFragment());*/
                    Toast.makeText(MainActivity.this, "Click to Profile", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_logout) {
                    myAuth.signOut();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "dont work", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        LoadPosts();
    }

    private void LoadFragment(ProfileFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frContainer, fragment);
        ft.commit();
    }

    private void LoadPosts() {
        options = new FirebaseRecyclerOptions.Builder<Post_Model>().setQuery(postRef, Post_Model.class).build();
        adapter = new FirebaseRecyclerAdapter<Post_Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Post_Model model) {
                final String postKey = getRef(position).getKey(); //post key for using likes and dislikes Table
                holder.postDescriptionView.setText(model.getPostDescription());
                holder.PostHeaderUserName.setText(model.getFullName());
                //holder.postTimeView.setText(model.getPostDate());
                String timeVAgo = calculateTimeAgo(model.getPostDate());
                holder.postTimeView.setText(timeVAgo);
                Picasso.get().load(model.getPost_img_url()).into(holder.postImgView);
                Picasso.get().load(model.getUser_profile_img()).into(holder.post_profile_Image_view);


                //like dislike button identifire
                assert postKey != null;

                disLikeRef.child(postKey).child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.dislikeIcon.setColorFilter(Color.RED);
                        }else {
                            holder.dislikeIcon.setColorFilter(Color.BLACK);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //like or dislike count
                holder.countLike(postKey,myUser.getUid(),likeRef);
                holder.countDislike(postKey,myUser.getUid(),disLikeRef);
                holder.commentCount(postKey,myUser.getUid(),commentRef);

                /*holder.commentIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyViewHolder.recyclerViewCmt.getVisibility()==View.VISIBLE){
                            MyViewHolder.recyclerViewCmt.setVisibility(View.GONE);
                        }else {
                            MyViewHolder.recyclerViewCmt.setVisibility(View.VISIBLE);
                        }
                    }
                    //LoadComment(postKey);
                });*/
//like and dislike button initialization
                holder.likeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeRef.child(postKey).child(myUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    likeRef.child(postKey).child(myUser.getUid()).removeValue();
                                    holder.likeIcon.setColorFilter(Color.BLACK);
                                    //notifyDataSetChanged();
                                }else {
                                    likeRef.child(postKey).child(myUser.getUid()).setValue("Like");
                                    holder.likeIcon.setColorFilter(Color.BLUE);
                                    //notifyDataSetChanged();
                                    disLikeRef.child(postKey).child(myUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                disLikeRef.child(postKey).child(myUser.getUid()).removeValue();
                                                holder.dislikeIcon.setColorFilter(Color.BLACK);
                                               // notifyDataSetChanged();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                holder.dislikeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disLikeRef.child(postKey).child(myUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    disLikeRef.child(postKey).child(myUser.getUid()).removeValue();
                                    holder.dislikeIcon.setColorFilter(Color.BLACK);
                                    //notifyDataSetChanged();
                                }else {
                                    disLikeRef.child(postKey).child(myUser.getUid()).setValue("Dislike");
                                    holder.dislikeIcon.setColorFilter(Color.BLUE);
                                    //notifyDataSetChanged();
                                    likeRef.child(postKey).child(myUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                likeRef.child(postKey).child(myUser.getUid()).removeValue();
                                                holder.likeIcon.setColorFilter(Color.BLACK);
                                                //notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                holder.addcmtImgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = holder.inputTxtComment.getText().toString();
                        if (comment.isEmpty()){
                            holder.inputTxtComment.setError("??");
                            Toast.makeText(MainActivity.this, "Please Enter Something...", Toast.LENGTH_SHORT).show();
                        }else {
                            addCommentToDB(postKey,holder,myUser.getUid(),commentRef, comment);
                        }
                    }
                });
                // comment view
                LoadComment(postKey);
            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_view,parent,false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recylContainer.setAdapter(adapter);
    }

    private void LoadComment(String postKey) {
        MyViewHolder.recyclerViewCmt.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ctmOptions = new FirebaseRecyclerOptions.Builder<comment_model>().setQuery(commentRef.child(postKey), comment_model.class).build();
        cmtAdapter = new FirebaseRecyclerAdapter<comment_model, MyCommentViewHolder>(ctmOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyCommentViewHolder holder, int position, @NonNull comment_model model) {
                Picasso.get().load(model.getProfileImgUrl()).into(holder.profileImg);
                holder.userName.setText(model.getUserName());
                holder.cmtView.setText(model.getComment());

            }

            @NonNull
            @Override
            public MyCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_view,parent,false);
                return new MyCommentViewHolder(view);
            }
        };
        MyViewHolder.recyclerViewCmt.setAdapter(cmtAdapter);
        cmtAdapter.startListening();
    }

    private void addCommentToDB(String postKey, MyViewHolder holder, String uid, DatabaseReference commentRef, String comment) {
        String commentKey = commentRef.child(postKey).push().getKey();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("userName",fullNameV);
        hashMap.put("profileImgUrl",profileImgV);
        hashMap.put("comment",comment);


        assert commentKey != null;
        commentRef.child(postKey).child(commentKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                    cmtAdapter.notifyDataSetChanged();
                    holder.inputTxtComment.setText("");
                }else {
                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String calculateTimeAgo(String postDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            long time = sdf.parse(postDate).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addPost() {
        String postDescription = input_description.getText().toString();
        if (postDescription.isEmpty() || postDescription.length()<3){
            input_description.setError("Please Write Something...");
        } else if (imgUri==null) {
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show();
        }else {
            myLoading_Dialog.setTitle("Adding Post");
            myLoading_Dialog.setCanceledOnTouchOutside(false);
            myLoading_Dialog.show();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String strDate= formatter.format(date);
            storeImgRef.child(myUser.getUid()+strDate).putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        storeImgRef.child(myUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("postDate",strDate);
                                hashMap.put("postDescription",postDescription);
                                hashMap.put("post_img_url",uri.toString());
                                hashMap.put("user_profile_img",profileImgV);
                                hashMap.put("fullName",fullNameV);
                                hashMap.put("userName",userNameV);

                                postRef.child(myUser.getUid()+strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            myLoading_Dialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Post Added", Toast.LENGTH_SHORT).show();
                                            input_description.setText("");
                                            upload_img.setImageResource(R.drawable.image_icon);
                                        }else {
                                            myLoading_Dialog.dismiss();
                                            Toast.makeText(MainActivity.this,"" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        myLoading_Dialog.dismiss();
                        Toast.makeText(MainActivity.this,"" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==POST_IMG_REQ_CODE && resultCode==RESULT_OK && data!=null){
            imgUri = data.getData();
            upload_img.setImageURI(imgUri);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (myUser==null){
            sendToLoginActivity();
        }else {
            dbRef.child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Check if "profileImage" exists and is not null
                        if (snapshot.child("profileImage").exists() && snapshot.child("profileImage").getValue() != null) {
                            profileImgV = snapshot.child("profileImage").getValue().toString();
                            Uri imgUri = Uri.parse(profileImgV);
                            Picasso.get().load(profileImgV).into(navHeaderImgV);
                            // navHeaderImgV.setImageURI(Uri.parse(profileImgV));
                        }

                        // Check if "fullName" exists and is not null
                        if (snapshot.child("fullName").exists() && snapshot.child("fullName").getValue() != null) {
                            fullNameV = snapshot.child("fullName").getValue().toString();
                            navHeaderUserName.setText(fullNameV);
                        }

                        // Check if "username" exists and is not null
                        if (snapshot.child("username").exists() && snapshot.child("username").getValue() != null) {
                            userNameV = snapshot.child("username").getValue().toString();
                        }
                    }


                    /*if (snapshot.exists()){
                        profileImgV = snapshot.child("profileImage").getValue().toString();
                        Uri imgUri= Uri.parse(profileImgV);
                        fullNameV = snapshot.child("fullName").getValue().toString();
                        userNameV = snapshot.child("username").getValue().toString();

                        Picasso.get().load(profileImgV).into(navHeaderImgV);
                        navHeaderUserName.setText(fullNameV);
                        //navHeaderImgV.setImageURI(Uri.parse(profileImgV));
                    }*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Opps..!! something want wrong...!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    //Appbar Home button Open Calling code
   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }*/
}