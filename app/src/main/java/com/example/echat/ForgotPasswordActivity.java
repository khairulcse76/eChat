package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth myAuth;
    EditText inputEmail;
    Button btnForgot;
    TextView confirmMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        confirmMsg=findViewById(R.id.txtViewPassSend);
        inputEmail=findViewById(R.id.inputEmail);
        btnForgot=findViewById(R.id.btnForgot);

        myAuth = FirebaseAuth.getInstance();

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=inputEmail.getText().toString();
                if (email.isEmpty()){
                    inputEmail.setError("Please Enter Your Email Address");
                }else {
                    myAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                confirmMsg.setText("Please Check your email for Password reset link \n"+" Click for Login");
                                inputEmail.setText("");
                            }else {
                                confirmMsg.setText("Email Not send");
                                confirmMsg.setTextColor(Color.RED);
                            }
                        }
                    });
                }
            }
        });
        confirmMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                intent.putExtra("checkMail","if you send password reset link, please check email for reset password...");
                startActivity(intent);
            }
        });

    }
}