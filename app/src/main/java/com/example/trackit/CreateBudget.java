package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Adapters.AdapterTransactions;
import com.example.trackit.Fragments.BudgetFragment;
import com.example.trackit.Model.Budget;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.Transactions.IncomePage;
import com.example.trackit.Transactions.Transaction_Done;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateBudget extends AppCompatActivity {

    private Spinner budgetCategories;
    private UserInfo userInfo;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getSupportActionBar().hide();

        setListeners();

        setSpinner();

        Window window = CreateBudget.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CreateBudget.this, R.color.lila));

        userInfo = UserInfo.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        SharedPreferences preferences = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        email = email.replace('.', ',');

        ref = database.getReference("users/"+email);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                UserInfo.setUniqueInstance(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("La lectura ha fallat: " + databaseError.getCode());
            }
        });
    }

    private void setSpinner(){
        budgetCategories = findViewById(R.id.spinnerCreateBudget);

        String arrayName = "budget_categories";
        int arrayName_ID = getResources().getIdentifier(arrayName,"array",this.getPackageName());
        String[] categories = getResources().getStringArray(arrayName_ID);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Desactivem el primer item
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
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
                boolean alert = ((SwitchMaterial) findViewById(R.id.switchBudget)).isChecked();

                if(!enterBudget.getText().toString().isEmpty() && !category.equals("Categoria")){
                    // Atributes of the Transaction's class
                    double quantity = Double.valueOf(enterBudget.getText().toString());

                    Budget budget;

                    budget = new Budget(category, quantity, alert);
                    userInfo.addBudget(budget);
                    ref.setValue(userInfo);
                    Intent intent = new Intent(CreateBudget.this, Transaction_Done.class);
                    startActivity(intent);
                    finish();

                }else{
                    // If something is empty, display a message to the user.
                    Toast.makeText(CreateBudget.this,"S'han d'omplir tots els camps!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}