package com.example.trackit.Transactions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.Data.UserInfo;
import com.example.trackit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class ExpensePage extends AppCompatActivity {

    private Spinner expenseCategories;
    private UserInfo userInfo;
    private DatabaseReference ref;
    EditText dateExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_page);
        getSupportActionBar().hide();

        setListeners();

        setSpinner();

        Window window = ExpensePage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ExpensePage.this, R.color.redExpense));

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");


        ref = database.getReference("users/Pedrito__");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void setSpinner() {
        expenseCategories = findViewById(R.id.spinnerExpenseCategory);

        String arrayName = "expense_categories";
        int arrayName_ID = getResources().getIdentifier(arrayName,"array",this.getPackageName());
        String[] categories = getResources().getStringArray(arrayName_ID);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0){
                    // Desactivem el primer item
                    return false;
                } else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        expenseCategories.setAdapter(spinnerAdapter);

    }

    private void setListeners() {
        ImageButton back = findViewById(R.id.back_expense);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button continueExpense= findViewById(R.id.continueExpense);

        continueExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText enterIncome = (EditText) findViewById(R.id.enterIncome);
                EditText incomeDescription = (EditText) findViewById(R.id.incomeTitle);
                String category = expenseCategories.getSelectedItem().toString();

                if(!enterIncome.getText().toString().isEmpty() && !incomeDescription.getText().toString().isEmpty() && category!=null){
                    // Atributes of the Transaction's class
                    String description = incomeDescription.getText().toString();
                    Date avui = new Date();
                    double quantity = Double.valueOf(enterIncome.getText().toString());
                    Transaction transaction = new Transaction(description, category, quantity, avui);
                    userInfo.addTransaction(transaction);
                    ref.setValue(userInfo);
                    Intent intent = new Intent(ExpensePage.this, Transaction_Done.class);
                    startActivity(intent);
                    finish();

                }else{
                    // If something is empty, display a message to the user.
                    Toast.makeText(ExpensePage.this,"Tots els camps s'han d'omplir!",Toast.LENGTH_LONG).show();
                }
            }
        });

        dateExpense = findViewById(R.id.dateExpense);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ExpensePage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dateExpense.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}