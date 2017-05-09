package com.example.deekshabhat.eventmav;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnRegister;
    private EditText etFirstName, etLastName, etMavEmail, etMavID, etPassword, etConfirm;
    private CheckBox chOrganizer;
    private DatabaseReference mDatabase;
    private boolean isOrganizer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etMavEmail = (EditText) findViewById(R.id.etMavEmail);
        etMavID = (EditText) findViewById(R.id.etMavID);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirm = (EditText) findViewById(R.id.etConfirm);
        chOrganizer = (CheckBox) findViewById(R.id.chOrganizer);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidForm = true;
                String FirstName = etFirstName.getText().toString().trim();
                String LastName = etLastName.getText().toString().trim();
                String MavEmail = etMavEmail.getText().toString().trim();
                String MavID = etMavID.getText().toString().trim();
                String Password = etPassword.getText().toString().trim();
                String Confirm = etConfirm.getText().toString().trim();
                // String FirstName = etFirstName.getText().toString().trim();
                if(FirstName.isEmpty()){
                    etFirstName.setError("Enter first name");
                    isValidForm = false;
                }
                if(LastName.isEmpty()){
                    etLastName.setError("Enter last name");
                    isValidForm = false;
                }if(MavEmail.isEmpty() && !MavEmail.endsWith("@mavs.uta.edu")){
                    etMavEmail.setError("Enter valid mav email");
                    isValidForm = false;
                }if(MavID.isEmpty()){
                    etMavID.setError("Enter mav id");
                    isValidForm = false;
                }if(Password.isEmpty()){
                    etPassword.setError("Enter password");
                    isValidForm = false;
                }if(Confirm.isEmpty()){
                    etConfirm.setError("Enter confirm password");
                    isValidForm = false;
                }
                if(!Password.equals(Confirm)){
                    etConfirm.setError("Password and confirm password doesn't match");
                    isValidForm = false;
                }
                if(isValidForm==true && Password.matches("^[a-zA-Z0-9]{6,}$")) {
                    createAccount(MavEmail, Password, FirstName, LastName, MavID);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter valid password(6 digit alpha numeric)", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createAccount(String mail, String password, final String firstname, final String lastname, final String mavid) {
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            String Uid = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user = mDatabase.child(Uid);
                            current_user.child("firstname").setValue(firstname);
                            current_user.child("lastname").setValue(lastname);
                            current_user.child("mavid").setValue(mavid);
                            current_user.child("isOrganizer").setValue(isOrganizer);
                        }
                    }
                });
    }

    public void isChecked(View view) {
        CheckBox checkBox = (CheckBox)view;
        if(checkBox.isChecked()){
            isOrganizer = true;
        }
        else
            isOrganizer = false;
    }

}