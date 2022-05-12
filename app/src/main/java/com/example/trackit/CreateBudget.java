package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AndroidException;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.Fragments.BudgetFragment;
import com.example.trackit.Transactions.Transaction_Done;
import com.google.firebase.database.FirebaseDatabase;

public class CreateBudget extends AppCompatActivity {

    private Spinner budgetCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getSupportActionBar().hide();

        Window window = CreateBudget.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CreateBudget.this, R.color.lila));

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        setListeners();

        setSpinner();
    }

    private void setSpinner(){
        budgetCategories = findViewById(R.id.spinnerCreateBudget);

        String arrayName = "budget_categories";
        int arrayName_ID = getResources().getIdentifier(arrayName, "array", this.getPackageName());
        String[] categories = getResources().getStringArray(arrayName_ID);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories){
            @Override
            public boolean isEnabled(int position){
                if (position == 0){
                    //Desactivem primer item
                    return false;
                }
                else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                } else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetCategories.setAdapter(spinnerAdapter);

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

        Button continueBudget = findViewById(R.id.continueBudget);

        continueBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText enterBudget = (EditText) findViewById(R.id.enterBudget);
                String category = budgetCategories.getSelectedItem().toString();

                if(!enterBudget.getText().toString().isEmpty() && category != null){
                    Intent intent = new Intent(CreateBudget.this, Transaction_Done.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    //If any field is empty, notify it to the user
                    Toast.makeText(CreateBudget.this, "Tots els camps s'han d'omplir", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}