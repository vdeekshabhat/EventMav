package com.example.deekshabhat.eventmav;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button sendEmail = (Button) findViewById(R.id.mForgotBtn);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.mForgotEmail);
                initSendEmail(editText.getText().toString());
            }
        });

    }

    private void initSendEmail(String email_text){
        mAuth = FirebaseAuth.getInstance();
        Toast.makeText(ForgotPasswordActivity.this, "Click Successful ", Toast.LENGTH_SHORT).show();
        mAuth.sendPasswordResetEmail(email_text).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email sent to your mail ID ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Email Unsuccessful ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
