package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Setup
        mAuth = FirebaseAuth.getInstance();
        setup();
    }

    private void setup(){
        setTitle("Sign in");

        Button singUp = (Button) findViewById(R.id.signUpButton);

        // CHECK IF DATA IS CORRECT
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.emailEditText);
                EditText pass = (EditText) findViewById(R.id.passwordEditText);

                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(AuthActivity.this, Welcome_Page.class);

                                        startActivity(i);

                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(AuthActivity.this, "Hi ha hagut un error!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        Button singIn = (Button) findViewById(R.id.loginButton);

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.emailEditText);
                EditText pass = (EditText) findViewById(R.id.passwordEditText);

                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(AuthActivity.this, Welcome_Page.class);

                                        startActivity(i);

                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(AuthActivity.this, "Hi ha hagut un error!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}