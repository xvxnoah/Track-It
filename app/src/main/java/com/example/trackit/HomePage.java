package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.trackit.databinding.ActivityHomePageBinding;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {

    FloatingActionButton fab, fab_in, fab_out, fab_news;
    Animation fabOpen, fabClose;
    ActivityHomePageBinding binding;

    boolean isOpen = false; // per defecte es fals
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        getSupportActionBar().hide();

        Window window = HomePage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(HomePage.this, R.color.white));

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
                startActivity(new Intent(HomePage.this, IncomePage.class));
            }
        });

        fab_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                startActivity(new Intent(HomePage.this, ExpensePage.class));
            }
        });

        fab_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                startActivity(new Intent(HomePage.this, NewsPage.class));
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.principal_menu:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.transactions_menu:
                    replaceFragment(new TransactionsFragment());
                    break;

                case R.id.budget_menu:
                    replaceFragment(new BudgetFragment());
                    break;

                case R.id.profile_menu:
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutHome, fragment);
        fragmentTransaction.commit();
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