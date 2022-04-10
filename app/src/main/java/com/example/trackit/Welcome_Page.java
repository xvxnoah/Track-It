package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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

        // test log-out
        logout();

        // som-hi
        letsgo();
    }

    private void letsgo() {
        Button lets = findViewById(R.id.somhi);

        lets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome_Page.this, HomePage.class));
            }
        });
    }

    private void logout() {
        SharedPreferences saveSession = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveSession.edit();

        Button logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(Welcome_Page.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}