package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    ProgressDialog myProgressBar;
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

        myProgressBar=new ProgressDialog(this);
        dbUserRef.child(myUsers.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String profileImgUrl = snapshot.child("profileImage").getValue().toString();
                    String username = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                    String fullname = snapshot.child("fullName").getValue().toString();
                    String country = snapshot.child("country").getValue().toString();
                    String city = snapshot.child("city").getValue().toString();
                    String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                    String profession = snapshot.child("profession").getValue().toString();

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

                            Map<String, Object> updates = new HashMap<>();
                            updates.put("username",user);
                            updates.put("fullName",fullname);
                            updates.put("country",country);
                            updates.put("city",city);
                            updates.put("phoneNumber",number);
                            updates.put("profession",profession);
                            dbUserRef.child(myUsers.getUid()).updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        Toast.makeText(Profile_Activity.this, "Profile update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }else {
                    Toast.makeText(Profile_Activity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_Activity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}