package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {

    FloatingActionButton fab, fab_in, fab_out, fab_news;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;

    boolean isOpen = false; // per defecte es fals
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().hide();

        // Floating Action Buttons
        fab = findViewById(R.id.fab);
        fab_in = findViewById(R.id.fab_in);
        fab_out = findViewById(R.id.fab_out);
        fab_news = findViewById(R.id.fab_news);

        // Animations
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        // Start listeners
        startListeners();

    }

    private void startListeners() {
        // Set the click listener on the main FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Toast.makeText(HomePage.this, "MONEY IN", Toast.LENGTH_SHORT).show();
            }
        });

        fab_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Toast.makeText(HomePage.this, "MONEY OUT", Toast.LENGTH_SHORT).show();
            }
        });

        fab_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Toast.makeText(HomePage.this, "NEWS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateFab(){
        if(isOpen){
            fab_in.startAnimation(fabClose);
            fab_out.startAnimation(fabClose);
            fab_news.startAnimation(fabClose);
            fab_in.setClickable(false);
            fab_out.setClickable(false);
            fab_news.setClickable(false);
            isOpen = false;
        } else{
            fab_in.startAnimation(fabOpen);
            fab_out.startAnimation(fabOpen);
            fab_news.startAnimation(fabOpen);
            fab_in.setClickable(true);
            fab_out.setClickable(true);
            fab_news.setClickable(true);
            isOpen = true;
        }
    }
}