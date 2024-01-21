package com.example.echat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    CircleImageView profileImg;
    EditText inputUsername, inputfullname, inputphonenumber,inputProfession,inputCity, inputCountry;
    Button btnUpdate;
    Toolbar toolbar;
    FirebaseAuth myAuth;
    FirebaseUser myUsers;
    DatabaseReference dbUserRef;
    StorageReference storeRef;
    ProgressDialog myProgressBar;
    static final int PROF_IMG_REQ_CODE = 101;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImg = findViewById(R.id.UpdateprofileImgView);
        inputUsername = findViewById(R.id.inputUsername);
        inputfullname = findViewById(R.id.fullName);
        inputphonenumber = findViewById(R.id.inputPhoneNumber);
        inputProfession = findViewById(R.id.inputprofessionUpd);
        inputCity = findViewById(R.id.inputCityUpd);
        inputCountry = findViewById(R.id.inputCountryUpd);
        btnUpdate = findViewById(R.id.btnUpdate);
        toolbar = findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAuth=FirebaseAuth.getInstance();
        myUsers=myAuth.getCurrentUser();
        dbUserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        storeRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        myProgressBar=new ProgressDialog(this);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iUploadImg = new Intent(Intent.ACTION_GET_CONTENT);
                iUploadImg.setType("image/*");
                startActivityForResult(iUploadImg, PROF_IMG_REQ_CODE);
            }
        });

        dbUserRef.child(myUsers.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String profileImgUrl = "";
                    String username = "";
                    String fullname = "";
                    String country = "";
                    String city = "";
                    String phoneNumber = "";
                    String profession = "";

                    // Assuming 'R.drawable.default_profile_image' is the resource ID for your default image
                    int defaultImageResourceId = R.drawable.profile;

                    String defaultImageUrl = "android.resource://" + getPackageName() + "/" + defaultImageResourceId;

                    profileImgUrl = snapshot.child("profileImage").getValue() != null ?
                            snapshot.child("profileImage").getValue().toString() : defaultImageUrl;


                    if (snapshot.child("username").exists() && snapshot.child("username").getValue() != null) {
                        username = snapshot.child("username").getValue().toString();
                    }

                    if (snapshot.child("fullName").exists() && snapshot.child("fullName").getValue() != null) {
                        fullname = snapshot.child("fullName").getValue().toString();
                    }

                    if (snapshot.child("country").exists() && snapshot.child("country").getValue() != null) {
                        country = snapshot.child("country").getValue().toString();
                    }

                    if (snapshot.child("city").exists() && snapshot.child("city").getValue() != null) {
                        city = snapshot.child("city").getValue().toString();
                    }

                    if (snapshot.child("phoneNumber").exists() && snapshot.child("phoneNumber").getValue() != null) {
                        phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                    }

                    if (snapshot.child("profession").exists() && snapshot.child("profession").getValue() != null) {
                        profession = snapshot.child("profession").getValue().toString();
                    }

                    /*String profileImgUrl = snapshot.child("profileImage").getValue().toString();
                    String username = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                    String fullname = snapshot.child("fullName").getValue().toString();
                    String country = snapshot.child("country").getValue().toString();
                    String city = snapshot.child("city").getValue().toString();
                    String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                    String profession = snapshot.child("profession").getValue().toString();*/

                    Picasso.get().load(profileImgUrl).into(profileImg);
                    inputUsername.setText(username);
                    inputfullname.setText(fullname);
                    inputphonenumber.setText(phoneNumber);
                    inputCity.setText(city);
                    inputCountry.setText(country);
                    inputProfession.setText(profession);


                    btnUpdate.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            
                            String user = inputUsername.getText().toString();
                            String fullname = inputfullname.getText().toString();
                            String number = inputphonenumber.getText().toString();
                            String city = inputCity.getText().toString();
                            String country = inputCountry.getText().toString();
                            String profession = inputProfession.getText().toString();
                            String profileimg = snapshot.child("profileImage").getValue().toString();

                            Map<String, Object> updates = new HashMap<>();
                            updates.put("username",user);
                            updates.put("fullName",fullname);
                            updates.put("country",country);
                            updates.put("city",city);
                            updates.put("status","Offline");
                            updates.put("phoneNumber",number);
                            updates.put("profession",profession);
                           updates.put("profileImage",profileimg);

                            if (imgUri!=null){
                                storeRef.child(myUsers.getUid()).putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()){
                                            storeRef.child(myUsers.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Map<String, Object> updatesImg = new HashMap<>();
                                                    updatesImg.put("profileImage",uri.toString());

                                                    dbUserRef.child(myUsers.getUid()).updateChildren(updatesImg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Profile_Activity.this.notify();
                                                                    Toast.makeText(Profile_Activity.this, "Profile Image Update", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Handle failure, log the error, or show an error message
                                                                    Toast.makeText(Profile_Activity.this, "Error updating profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    //Toast.makeText(Profile_Activity.this, "Profile Image Update", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }

                            dbUserRef.child(myUsers.getUid()).updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        /*Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);*/
                                        Toast.makeText(Profile_Activity.this, "Profile update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }else {
                    Intent iproSetup = new Intent(getApplicationContext(),SetupProfileActivity.class);
                    startActivity(iproSetup);
                    Toast.makeText(Profile_Activity.this, "User Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_Activity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PROF_IMG_REQ_CODE && resultCode==RESULT_OK && data!=null){
            imgUri=data.getData();
            profileImg.setImageURI(imgUri);
        }
    }
}