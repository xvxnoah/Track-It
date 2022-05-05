package com.example.trackit.Transactions;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trackit.R;

public class Transaction_Done extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_done);

        getSupportActionBar().hide();

        Window window = Transaction_Done.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Transaction_Done.this, R.color.white));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 4000);
    }
}