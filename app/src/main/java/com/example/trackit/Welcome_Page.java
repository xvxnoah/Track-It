package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        getSupportActionBar().hide();

        // check if account already set up
        alreadySetUp();

        // som-hi
        letsgo();

        Window window = Welcome_Page.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Welcome_Page.this, R.color.white));
    }

    private void alreadySetUp() {
        SharedPreferences preferencesUser = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferencesUser.getString(AuthActivity.USER, null);

        SharedPreferences preferencesSetUp = getSharedPreferences(NewAccount.SETUP_USER, Context.MODE_PRIVATE);
        String user = preferencesSetUp.getString(NewAccount.SETUP_EMAIL, null);

        if(email.equals(user)){
            startActivity(new Intent(Welcome_Page.this, HomePage.class));
            finish();
        }
    }

    private void letsgo() {
        Button lets = findViewById(R.id.somhi);

        lets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome_Page.this, NewAccount.class));
            }
        });
    }
}