package com.example.echat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference dbRef, postRef;
    StorageReference storeImgRef;

    String profileImgV,fullNameV, userNameV;

    CircleImageView navHeaderImgV;
    TextView navHeaderUserName, input_description;
    ImageView upload_img, btn_upload_post;

    private final int POST_IMG_REQ_CODE=101;
    Uri  imgUri;

    ProgressDialog myLoading_Dialog;
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

        myAuth = FirebaseAuth.getInstance();
        myUser = myAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        storeImgRef = FirebaseStorage.getInstance().getReference().child("PostImage");

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_3line);
        }

        myLoading_Dialog = new ProgressDialog(this);


        View view = navigationView.inflateHeaderView(R.layout.nav_header_layout);
        navHeaderImgV = view.findViewById(R.id.post_profile_Image_view);
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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home){
                    Toast.makeText(MainActivity.this, "Click to home", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_Chat) {
                    Toast.makeText(MainActivity.this, "Click to Chat", Toast.LENGTH_SHORT).show();
                }else if (id==R.id.nav_friend){
                    Toast.makeText(MainActivity.this, "Click to Friend", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_find_friend) {
                    Toast.makeText(MainActivity.this, "Click to Find Friend", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_profile) {
                    Intent intent  = new Intent(MainActivity.this,SetupProfileActivity.class);
                    startActivity(intent);

                    Toast.makeText(MainActivity.this, "Click to Profile", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_logout) {
                    Toast.makeText(MainActivity.this, "Click to Logout", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "dont work", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String strDate= formatter.format(date);
            storeImgRef.child(myUser.getUid()+strDate).putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        storeImgRef.child(myUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                HashMap hashMap = new HashMap();
                                hashMap.put("postDate",strDate);
                                hashMap.put("postDescription",postDescription);
                                hashMap.put("post_img_url",uri.toString());
                                hashMap.put("user_profile_img",profileImgV);
                                hashMap.put("fullName",fullNameV);
                                hashMap.put("userName",userNameV);

                                postRef.child(myUser.getUid()+strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
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
                    if (snapshot.exists()){
                        profileImgV = snapshot.child("profileImage").getValue().toString();
                        Uri imgUri= Uri.parse(profileImgV);
                        fullNameV = snapshot.child("fullName").getValue().toString();
                        userNameV = snapshot.child("username").getValue().toString();

                        Picasso.get().load(profileImgV).into(navHeaderImgV);
                        navHeaderUserName.setText(fullNameV);
                        //navHeaderImgV.setImageURI(Uri.parse(profileImgV));
                    }
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