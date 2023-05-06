package com.example.amoremanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText txtUserEmail, txtPassword;
    Button btnLogin;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hide action bar
        getSupportActionBar().hide();

        txtUserEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        //load app if current is still signed in
        if(firebaseAuth.getCurrentUser()!=null){
            Toast.makeText(this, "Welcome back.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtUserEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    txtUserEmail.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    txtPassword.setError("Password is required!");
                    return;
                }
                //validate password length
                if(password.length()<6){
                    txtPassword.setError("Password is too short.");
                    return;
                }

                //show progress bar
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            //navigate to home page
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(Login.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //make progress bar disappear
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}