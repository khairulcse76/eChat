package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextView txtViewReg , txtVForgotPass, txtVSignup;
    EditText inputUserName,inputLoginPass;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtViewReg = findViewById(R.id.textViewReg);
        txtVForgotPass= findViewById(R.id.txtVForgotPass);
        txtVSignup=findViewById(R.id.txtVSignup);
        inputUserName= findViewById(R.id.inputUserName);
        inputLoginPass=findViewById(R.id.inputLoginPass);
        btnLogin=findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        mProgressBar = new ProgressDialog(this);

        txtVForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Forgot pass are not configuration", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptLogin();
            }
        });

        txtVSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRegistration = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(iRegistration);
            }
        });

    }

    private void AtemptLogin() {

        String email = inputUserName.getText().toString();
        String password = inputLoginPass.getText().toString();

        if (email.isEmpty()) {
            showerrors(inputUserName,"Email Address Required");
        } else if (!email.contains("@gmail")) {
            showerrors(inputUserName,"Please enter valid Email Address");
        } else if (password.isEmpty()) {
            showerrors(inputLoginPass, "Please enter Password..!");
        }else{
            mProgressBar.setTitle("Login");
            mProgressBar.setMessage("Please wait, while your Credential is Checking...");
            mProgressBar.setCanceledOnTouchOutside(false);
            mProgressBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mProgressBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent iMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                        iMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(iMainActivity);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        mProgressBar.dismiss();
                    }
                }
            });
        }
    }

    private void showerrors(EditText field, String errmsg) {
        field.setError(errmsg);
        field.requestFocus();
    }
}