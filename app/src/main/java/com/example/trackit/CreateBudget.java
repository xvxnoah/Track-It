package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class CreateBudget extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getSupportActionBar().hide();

        Window window = CreateBudget.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CreateBudget.this, R.color.lila));

        setListeners();
    }

    private void setListeners() {
        ImageButton back = findViewById(R.id.back_create_budget);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }
}