package com.example.echat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.PrivateKey;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupProfileActivity extends AppCompatActivity {
    CircleImageView profile_img;
    EditText inputName,inputUserName,inputPhoneNumber,inputProfession,inputCity,inputCountry;
    TextView txtSkip;
    Button btnSave;
    private final int IMAGE_REQ_CODE = 111;
    Uri imgUri;

    //firebase Referenced
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference dbRef;
    StorageReference StoreRef;

    ProgressDialog myProgresBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        profile_img         = findViewById(R.id.profile_image);
        inputName           = findViewById(R.id.inputName);
        inputUserName       = findViewById(R.id.inpUserName);
        inputPhoneNumber    = findViewById(R.id.inputphoneNumber);
        inputProfession     = findViewById(R.id.inputProfession);
        inputCity           = findViewById(R.id.inputCity);
        inputCountry        = findViewById(R.id.inputCountry);
        txtSkip             = findViewById(R.id.txtSkip);
        btnSave             = findViewById(R.id.btnsave);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        StoreRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        myProgresBar = new ProgressDialog(this);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_REQ_CODE);
            }
        });
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMainActivity=new Intent(SetupProfileActivity.this, MainActivity.class);
                startActivity(iMainActivity);
                finish();
            }
        });
    }

    private void SaveData() {
        String name = inputName.getText().toString();
        String userName = inputUserName.getText().toString();
        String phoneNumber = inputPhoneNumber.getText().toString();
        String profession = inputProfession.getText().toString();
        String city = inputCity.getText().toString();
        String country  = inputCountry.getText().toString();

        if (name.isEmpty() || name.length()<3){
            ShowErrors(inputName,"Please fill the name field.");
        } else if (userName.isEmpty()||userName.length()<3) {
            ShowErrors(inputUserName,"Enter User name");
        } else if (phoneNumber.isEmpty()) {
            ShowErrors(inputPhoneNumber,"Phone Number must be 11 digit");
        } else if (profession.isEmpty() | profession.length()<3) {
            ShowErrors(inputProfession,"Enter min 3 letter..");
        } else if (city.isEmpty() | city.length()<3) {
            ShowErrors(inputCity,"Enter min 3 letter of city..");
        } else if (country.isEmpty() | country.length()<3) {
            ShowErrors(inputCountry,"Enter min 3 letter of Country..");
        }else {
            myProgresBar.setTitle("Profile Setup");
            myProgresBar.setMessage("Please wait while your request is save....");
            myProgresBar.setCanceledOnTouchOutside(false);
            myProgresBar.show();
            StoreRef.child(mUser.getUid()).putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        StoreRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap<>();

                                hashMap.put("username",userName);
                                hashMap.put("fullName",name);
                                hashMap.put("phoneNumber",phoneNumber);
                                hashMap.put("profession",profession);
                                hashMap.put("city", city);
                                hashMap.put("country",country);
                                hashMap.put("profileImage",uri.toString());
                                dbRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        myProgresBar.dismiss();
                                        Intent iMainActivity = new Intent(SetupProfileActivity.this, MainActivity.class);
                                        startActivity(iMainActivity);
                                        Toast.makeText(SetupProfileActivity.this, "Profile Setup Success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        myProgresBar.dismiss();
                                        Toast.makeText(SetupProfileActivity.this, "Setup Failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }

    }

    private void ShowErrors(EditText fieldName, String errMsg) {
        fieldName.setError(errMsg);
        fieldName.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQ_CODE && resultCode ==RESULT_OK && data!=null){
            imgUri = data.getData();
            profile_img.setImageURI(imgUri);
        }
    }
}