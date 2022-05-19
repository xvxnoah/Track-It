package com.example.trackit.initActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Account.NewAccount;
import com.example.trackit.HomePage;
import com.example.trackit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash_Screen2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);

        getSupportActionBar().hide();

        Window window = Splash_Screen2.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Splash_Screen2.this, R.color.softBlue));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!alreadySetUpLocal()) {
                    alreadySetUpDatabase();
                }
            }
        }, 1500);

    }

    private boolean alreadySetUpLocal() {
        /* Same device */
        SharedPreferences preferencesUser = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferencesUser.getString(AuthActivity.USER, null);

        SharedPreferences preferencesSetUp = getSharedPreferences(NewAccount.SETUP_USER, Context.MODE_PRIVATE);
        String user = preferencesSetUp.getString(NewAccount.SETUP_EMAIL, null);

        if(email.equals(user)){
            startActivity(new Intent(Splash_Screen2.this, HomePage.class));
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
                    Log.d("==== CheckDB", "Already configured!");
                    Intent intent = new Intent(Splash_Screen2.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("==== CheckDB", "Not configured!");
                    Intent intent = new Intent(Splash_Screen2.this, Welcome_Page.class);
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




}