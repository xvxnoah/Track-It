package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Welcome_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("HOME TRACK IT!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
    }
}