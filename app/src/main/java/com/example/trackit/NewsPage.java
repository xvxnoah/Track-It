package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class NewsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        getSupportActionBar().hide();

        Window window = NewsPage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(NewsPage.this, R.color.softGrey));

        setListeners();
    }

    private void setListeners() {
        ImageButton back = findViewById(R.id.back_news);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}