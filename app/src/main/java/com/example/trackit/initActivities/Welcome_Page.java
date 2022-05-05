package com.example.trackit.initActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Account.NewAccount;
import com.example.trackit.HomePage;
import com.example.trackit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcome_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // check if account already set up
        if(!alreadySetUpLocal()){
            alreadySetUpDatabase();

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
    public void alreadySetUpDatabase(){
        SharedPreferences preferencesUser = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferencesUser.getString(AuthActivity.USER, null);

        DatabaseReference reference = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users");
        reference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
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