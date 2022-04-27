package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Transaction_Done extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_done);

        getSupportActionBar().hide();

        Window window = Transaction_Done.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Transaction_Done.this, R.color.black));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 4000);
    }
}