package com.example.trackit.Transactions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Fragments.TransactionsFragment;
import com.example.trackit.HomePage;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.UUID;

public class EditTransaction extends AppCompatActivity {
    private Transaction transaction;
    private UserInfo userInfo;
    private DatabaseReference ref;
    private EditText title, date, ammount;
    private String data;
    private Spinner categoriesEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        getSupportActionBar().hide();

        Window window = EditTransaction.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(EditTransaction.this, R.color.teal_200));

        Gson gson = new Gson();

        String transactionDataObject = getIntent().getStringExtra("Transaction");
        transaction = gson.fromJson(transactionDataObject, Transaction.class);
        userInfo = UserInfo.getInstance();

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        SharedPreferences preferences = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        email = email.replace('.', ',');

        ref = database.getReference("users/"+email);

        title = findViewById(R.id.editTitle);
        title.setText(transaction.getName());

        date = findViewById(R.id.dateEdit);
        date.setText(transaction.getDate());

        ammount = findViewById(R.id.editAmmount);
        Double quantitat = Math.abs(transaction.getQuantity());
        ammount.setText(Double.toString(quantitat));

        setListeners();

        setSpinnerCategories();
    }

    private void setSpinnerCategories() {
        categoriesEdit = findViewById(R.id.spinnerEditCategory);
        String arrayName;

        if(transaction.getQuantity() < 0){
            arrayName = "expense_categories";
        } else{
            arrayName = "income_categories";
        }

        int arrayName_ID = getResources().getIdentifier(arrayName,"array",this.getPackageName());
        String[] categories = getResources().getStringArray(arrayName_ID);

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories){
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

        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesEdit.setAdapter(spinnerAdapter2);

        for(int i = 0; i < categories.length; i++){
            if(transaction.getType().equals(categories[i])){
                categoriesEdit.setSelection(i);
            }
        }
    }

    private void setListeners() {
        ImageButton back = findViewById(R.id.back_edit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button accept = findViewById(R.id.acceptEdit);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title.getText().toString().isEmpty() && !ammount.getText().toString().isEmpty() && !categoriesEdit.equals("Categoria") && !date.getText().toString().isEmpty()){
                    Transaction newOne;
                    String titol = title.getText().toString();
                    BigDecimal bd = new BigDecimal(ammount.getText().toString()).setScale(2, RoundingMode.UNNECESSARY);
                    String category = categoriesEdit.getSelectedItem().toString();
                    double quantity;

                    if(transaction.getQuantity() < 0){
                        quantity = 0 - bd.doubleValue();
                    } else{
                        quantity = bd.doubleValue();
                    }

                    String uniqueID = UUID.randomUUID().toString();
                    newOne = new Transaction(uniqueID, titol, category, quantity, date.getText().toString());

                    if(transaction.getBudget() != null){
                        newOne.setBudget(transaction.getBudget());
                    }

                    if(userInfo.updateTransaction(transaction, newOne)){
                        Toast.makeText(EditTransaction.this, "Canvis guardats correctament!", Toast.LENGTH_SHORT).show();
                        ref.setValue(userInfo);

                        finish();
                    } else{
                        Toast.makeText(EditTransaction.this, "Error en guardar els canvis!", Toast.LENGTH_SHORT).show();
                    }

                } else{
                    Toast.makeText(EditTransaction.this,"S'han d'omplir tots els camps!",Toast.LENGTH_LONG).show();
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTransaction.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String dateNew = day+"/"+month+"/"+year;
                        date.setText(dateNew);
                        data = dateNew;
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