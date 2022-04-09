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
        setTitle("HOME TRACK IT!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        // Save log in status
        SharedPreferences saveSession = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveSession.edit();

        Intent intent = getIntent();
        String email = intent.getStringExtra(AuthActivity.EMAIL);
        editor.putString("email", email);
        editor.apply();

        // test log-out
        logout(editor);
    }

    private void logout(SharedPreferences.Editor editor) {
        Button logOut = (Button) findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(Welcome_Page.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}