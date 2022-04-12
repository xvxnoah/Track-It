package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Info_Welcome_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if session already active
        session();

        setContentView(R.layout.activity_info_welcome_page);

        getSupportActionBar().hide();

        Window window = Info_Welcome_Page.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Info_Welcome_Page.this, R.color.softGrey));

        checkClick();
    }

    private void session() {
        SharedPreferences preferences = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);

        String user = preferences.getString(AuthActivity.USER, null);

        if(user != null){
            Toast.makeText(this,"Welcome back!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Info_Welcome_Page.this, Welcome_Page.class));
            finish();
        }
    }

    private void checkClick() {
        Button loginPage = findViewById(R.id.login_welcome);
        Button registerPage = findViewById(R.id.register_welcome);

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Info_Welcome_Page.this, AuthActivity.class));
            }
        });

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Info_Welcome_Page.this, RegisterPage.class));
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }
}