package com.example.trackit.initActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Account.RegisterPage;
import com.example.trackit.Adapters.SliderAdapter;
import com.example.trackit.HomePage;
import com.example.trackit.R;

public class Info_Welcome_Page extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private Welcome_Page welcome_page;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button loginPage;
    private Button registerPage;
    private boolean out;
    static Info_Welcome_Page info_welcome_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if session already active
        session();
        out = false;
        setContentView(R.layout.activity_info_welcome_page);
        info_welcome_page = this;

        loginPage = findViewById(R.id.login_welcome);
        registerPage = findViewById(R.id.register_welcome);

        loginPage.setVisibility(View.INVISIBLE);
        registerPage.setVisibility(View.INVISIBLE);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        getSupportActionBar().hide();

        Window window = Info_Welcome_Page.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Info_Welcome_Page.this, R.color.softGrey));

        checkClick();
    }

    public void addDotsIndicator(int position){

        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.purple_500));

            mDotLayout.addView(mDots[i]);

        }

        if(mDots.length > 0){

            mDots[position].setTextColor(getResources().getColor(R.color.purple_200));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            visibilityButtons(i);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void visibilityButtons(int i){
        if(i == 2){
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
            fadeIn.setDuration(1000);

            registerPage.startAnimation(fadeIn);
            registerPage.setVisibility(View.VISIBLE);

            loginPage.startAnimation(fadeIn);
            loginPage.setVisibility(View.VISIBLE);

            out = true;
        }else if(out){
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
            fadeOut.setStartOffset(0);
            fadeOut.setDuration(500);

            registerPage.startAnimation(fadeOut);
            registerPage.setVisibility(View.INVISIBLE);

            loginPage.startAnimation(fadeOut);
            loginPage.setVisibility(View.INVISIBLE);

            out = false;
        }
    }

    public static Info_Welcome_Page getInstance(){
        return info_welcome_page;
    }

    private void session() {
        SharedPreferences preferences = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);

        String user = preferences.getString(AuthActivity.USER, null);

        if(user != null){
            Intent intent = new Intent(this, Splash_Screen2.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkClick() {

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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }
}