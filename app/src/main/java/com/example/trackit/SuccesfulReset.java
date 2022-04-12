package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SuccesfulReset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succesful_reset);

        getSupportActionBar().hide();

        Window window = SuccesfulReset.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(SuccesfulReset.this, R.color.softGrey));

        setListener();
    }

    private void setListener() {
        Button backToSignIn  = findViewById(R.id.backSignIn);

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccesfulReset.this, AuthActivity.class));
                finish();
            }
        });
    }
}