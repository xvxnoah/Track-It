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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcome_Page extends AppCompatActivity {

    private Boolean setUp = false;
    private Integer timer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // check if account already set up
        if(!alreadySetUpLocal()){
            alreadySetUpDatabase();

            while(!setUp && timer < 200000){
                timer++;
            }

            super.onCreate(savedInstanceState);
            getSupportActionBar().hide();

            setContentView(R.layout.activity_welcome_page);

            Window window = Welcome_Page.this.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(Welcome_Page.this, R.color.white));

            // som-hi
            letsgo();
        }
    }

    private boolean alreadySetUpLocal() {
        /* Same device */
        SharedPreferences preferencesUser = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferencesUser.getString(AuthActivity.USER, null);

        SharedPreferences preferencesSetUp = getSharedPreferences(NewAccount.SETUP_USER, Context.MODE_PRIVATE);
        String user = preferencesSetUp.getString(NewAccount.SETUP_EMAIL, null);

        if(email.equals(user)){
            startActivity(new Intent(Welcome_Page.this, HomePage.class));
            finish();
        }

        return false;
    }

    private void alreadySetUpDatabase(){
        SharedPreferences preferencesUser = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferencesUser.getString(AuthActivity.USER, null);

        DatabaseReference reference = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users");
        reference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    setUp = true;
                    Intent intent = new Intent(Welcome_Page.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ;
            }
        });
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