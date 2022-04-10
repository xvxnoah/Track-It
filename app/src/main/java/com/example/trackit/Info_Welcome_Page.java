package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Info_Welcome_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_welcome_page);

        getSupportActionBar().hide();

        checkClick();
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
}