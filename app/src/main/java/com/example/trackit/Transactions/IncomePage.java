package com.example.trackit.Transactions;

import static java.lang.Math.abs;
import static java.lang.Math.round;

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
import com.example.trackit.R;
import com.example.trackit.Model.UserInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

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
import java.util.UUID;

public class IncomePage extends AppCompatActivity {

    private Spinner budgetsSpinner;
    private Spinner incomeCategories;
    private UserInfo userInfo;
    private DatabaseReference ref;
    EditText dateIncome;
    private String data = null;
    Uri selectedImageUri;

    ArrayList Budgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_page);
        getSupportActionBar().hide();

        setListeners();

        setSpinnerCategories();

        Window window = IncomePage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(IncomePage.this, R.color.greenIncome));

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
                System.out.println("La lectura ha fallat: " + databaseError.getCode());
            }
        });
    }

    private void setSpinnerBudget(BottomSheetDialog bottomSheetDialog) {
        budgetsSpinner = bottomSheetDialog.findViewById(R.id.spinnerBudgetExpense);
        Budgets = userInfo.getBudgets();
        Budget actual;
        ArrayList<String> budgetNames = new ArrayList<>();
        Iterator<Budget> iter = null;

        if (Budgets != null) {
            iter = Budgets.iterator();
        }

        budgetNames.add("Selecciona");
        if (Budgets != null) {
            while (iter.hasNext()) {
                actual = iter.next();

                if(!actual.isAlert()){
                    budgetNames.add(actual.getName());
                }
            }
        }

        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,budgetNames){
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

        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetsSpinner.setAdapter(spinnerAdapter1);
    }

    private void setSpinnerCategories() {
        incomeCategories = findViewById(R.id.spinnerIncomeCategory);

        String arrayName = "income_categories";
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
        incomeCategories.setAdapter(spinnerAdapter);
    }

    private void setListeners() {
        ImageButton back = findViewById(R.id.back_income);
        Button imageIncome = findViewById(R.id.imageIncome);
        Button cameraIncome = findViewById(R.id.cameraIncome);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button continueIncome = findViewById(R.id.continueIncome);

        continueIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText enterIncome = (EditText) findViewById(R.id.enterIncome);
                EditText incomeDescription = (EditText) findViewById(R.id.incomeTitle);
                String category = incomeCategories.getSelectedItem().toString();

                if(!enterIncome.getText().toString().isEmpty() && !category.equals("Categoria") && !dateIncome.getText().toString().isEmpty() && !incomeDescription.getText().toString().isEmpty()){
                    // Atributes of the Transaction's class
                    String description = incomeDescription.getText().toString();
                    BigDecimal bd = new BigDecimal(enterIncome.getText().toString()).setScale(2, RoundingMode.UNNECESSARY);

                    double quantity = bd.doubleValue();

                    Transaction transaction;

                    String uniqueID = UUID.randomUUID().toString();
                    transaction = new Transaction(uniqueID, description, category, quantity, dateIncome.getText().toString(), selectedImageUri);
                    userInfo.addTransaction(transaction);
                    userInfo.updateSave(quantity);

                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(IncomePage.this, R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(R.layout.bottom_sheet_budget);

                    bottomSheetDialog.show();

                    setSpinnerBudget(bottomSheetDialog);

                    bottomSheetDialog.findViewById(R.id.positiveButton).setBackgroundColor(getResources().getColor(R.color.greenIncome));
                    bottomSheetDialog.findViewById(R.id.negativeButton).setBackgroundColor(getResources().getColor(R.color.greenIncome));

                    bottomSheetDialog.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String budget = budgetsSpinner.getSelectedItem().toString();
                            if(budget.equals("Selecciona") == false) {
                                userInfo.updateBudget(budget, quantity, true);
                                ref.setValue(userInfo);
                                Intent intent = new Intent(IncomePage.this, Transaction_Done.class);
                                startActivity(intent);
                                finish();
                                bottomSheetDialog.dismiss();
                            } else{
                                Toast.makeText(IncomePage.this,"No has seleccionat pressupost!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    bottomSheetDialog.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ref.setValue(userInfo);
                            Intent intent = new Intent(IncomePage.this, Transaction_Done.class);
                            startActivity(intent);
                            finish();
                            bottomSheetDialog.dismiss();
                        }
                    });

                }else{
                    // If something is empty, display a message to the user.
                    Toast.makeText(IncomePage.this,"S'han d'omplir tots els camps!",Toast.LENGTH_LONG).show();
                }
            }
        });

        dateIncome = findViewById(R.id.dateIncome);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        dateIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateIncome.clearFocus();
                DatePickerDialog datePickerDialog = new DatePickerDialog(IncomePage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dateIncome.setText(date);
                        data = date;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        imageIncome.setOnClickListener(new View.OnClickListener() {
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
                Intent.createChooser(intent, "Selecciona una imatge"),
                1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        selectedImageUri = null;
        Uri selectedImage;

        String filePath = null;
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    String selectedPath=selectedImage.getPath();
                    if (requestCode == 1) {

                        if (selectedPath != null) {
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                            // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                            ImageView mImg = (ImageView) findViewById(R.id.image);
                            mImg.setImageBitmap(bmp);

                        }
                    }
                }
                break;
        }
    }
}