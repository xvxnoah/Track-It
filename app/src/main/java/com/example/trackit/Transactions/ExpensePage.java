package com.example.trackit.Transactions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Model.Budget;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class ExpensePage extends AppCompatActivity{

    private Spinner budgetsSpinner;
    private Spinner expenseCategories;
    private UserInfo userInfo;
    private DatabaseReference ref;
    EditText dateExpense;
    private String data;
    Uri selectedImageUri;

    ArrayList Budgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_page);
        getSupportActionBar().hide();

        setListeners();

        setSpinnerBudget();

        setSpinnerCategories();

        Window window = ExpensePage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ExpensePage.this, R.color.redExpense));

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void setSpinnerBudget() {
        budgetsSpinner = findViewById(R.id.spinnerIncomeCategory);
        Budgets = userInfo.getBudgets();
        Budget actual;
        ArrayList<String> budgetNames = new ArrayList<>();
        Iterator<Budget> iter = null;

        if (Budgets != null) {
            iter = Budgets.iterator();
        }

        if (Budgets != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                budgetNames.add(actual.getName());
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,budgetNames){
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
        budgetsSpinner.setAdapter(spinnerAdapter);
    }

    private void setSpinnerCategories() {
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
        Button imageExpense = findViewById(R.id.imageExpense);
        Button cameraExpense = findViewById(R.id.cameraExpense);

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
                EditText enterExpense = (EditText) findViewById(R.id.enterExpense);
                EditText incomeDescription = (EditText) findViewById(R.id.expenseDescription);
                String category = expenseCategories.getSelectedItem().toString();
                String budget = budgetsSpinner.getSelectedItem().toString();
                if(!enterExpense.getText().toString().isEmpty() && !incomeDescription.getText().toString().isEmpty() && !category.equals("Categoria") && !dateExpense.getText().toString().isEmpty()){
                    // Atributes of the Transaction's class
                    String description = incomeDescription.getText().toString();
                    BigDecimal bd = new BigDecimal(enterExpense.getText().toString()).setScale(2, RoundingMode.UNNECESSARY);
                    double quantity = 0 - bd.doubleValue();

                    Transaction transaction;

                    transaction = new Transaction(description, category, quantity, dateExpense.getText().toString(), selectedImageUri);
                    userInfo.addTransaction(transaction);
                    userInfo.updateWasted(bd.doubleValue());

                    if(budget != null){
                        userInfo.updateBudget(budget, quantity);
                    }
                    ref.setValue(userInfo);
                    Intent intent = new Intent(ExpensePage.this, Transaction_Done.class);
                    startActivity(intent);
                    finish();

                }else{
                    // If something is empty, display a message to the user.
                    Toast.makeText(ExpensePage.this,"S'han d'omplir tots els camps!",Toast.LENGTH_LONG).show();
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
                        data = date;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        imageExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalley();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    public void openGalley(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        selectedImageUri = null;

        String filePath = null;
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = imageReturnedIntent.getData();
                    String selectedPath=selectedImageUri.getPath();
                    if (requestCode == 1) {

                        if (selectedPath != null) {
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        selectedImageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                            // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                            ImageView mImg = (ImageView) findViewById(R.id.image2);
                            mImg.setImageBitmap(bmp);

                        }
                    }
                }
                break;
        }
    }
}