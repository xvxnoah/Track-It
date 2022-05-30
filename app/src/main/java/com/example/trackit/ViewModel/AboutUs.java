package com.example.trackit.ViewModel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.example.trackit.Transactions.Transaction_Done;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AboutUs extends AppCompatActivity{

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().hide();


        Window window = AboutUs.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AboutUs.this, R.color.redExpense));
    }


    private void setListeners() {
        ImageButton back = findViewById(R.id.back_about_us);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button continueExpense= findViewById(R.id.continueToWeb);

        continueExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(AboutUs.this, Transaction_Done.class);
                    startActivity(intent);
                    finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}