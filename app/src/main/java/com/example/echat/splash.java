package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class splash extends AppCompatActivity {

    FirebaseAuth myAuth;
    DatabaseReference dbRef;
    FirebaseUser myUser;
    StorageReference storeRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myAuth  = FirebaseAuth.getInstance();
        myUser = myAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference().child( "Users");

        Intent iLogin = new Intent(splash.this,LoginActivity.class);
        Intent iMain = new Intent(splash.this,MainActivity.class);
        Intent iSetupProfile = new Intent(splash.this,SetupProfileActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myUser!=null){
                    dbRef.child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                startActivity(iMain);
                            }else {
                                startActivity(iSetupProfile);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    startActivity(iLogin);
                    finish();
                }

            }
        }, 5000);
    }
}