package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Window;

public class CreateBudget extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getSupportActionBar().hide();

        Window window = CreateBudget.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CreateBudget.this, R.color.lila));
    }
}