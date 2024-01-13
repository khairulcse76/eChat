package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    EditText inputUserName, inputEmail,inputPass, inputPassConfirm;
    TextView haveAnAccount;
    ProgressDialog mLoadingBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        inputUserName = findViewById(R.id.inputUserName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);
        inputPassConfirm = findViewById(R.id.InputPassConfirm);
        haveAnAccount = findViewById(R.id.txtVHaveAnAccount);

        auth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

        haveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSetupProfile = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(iSetupProfile);
            }
        });
    }

    private void registration() {
        //String username = inputUserName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPass.getText().toString();
        String passConfirm = inputPassConfirm.getText().toString();

        /*if (username.isEmpty()){
            showerrors(inputUserName,"User Name is Required");*/
        if (email.isEmpty()) {
            showerrors(inputEmail,"Email Address Required");
        } else if (!email.contains("@gmail")) {
            showerrors(inputEmail,"Please enter valid Email Address");
        } else if (password.isEmpty()) {
                showerrors(inputPass, "Please enter Password..!");
        }else if (password.length()<5 || password.length()>12){
                showerrors(inputPass,"Password must between 5 to 12 Character");
            } else if(passConfirm.isEmpty()) {
            showerrors(inputPassConfirm, "Please enter Confirm Password..!");
        } else if (!passConfirm.equals(password)) {
            showerrors(inputPassConfirm, "Password not mach..??");
        }else{
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait, while your Credential..");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                                mLoadingBar.dismiss();
                                Intent intent = new Intent(getApplicationContext(),SetupProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, "Registration is failed..", Toast.LENGTH_SHORT).show();
                                mLoadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void showerrors(EditText field, String errMsg) {
        field.setError(errMsg);
        field.requestFocus();
    }


}

