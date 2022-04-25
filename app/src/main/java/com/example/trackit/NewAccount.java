package com.example.trackit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewAccount extends AppCompatActivity {

    private Spinner expenseCategories;

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for our object class
    UserInfo UserInfo;

    // EditText and buttons.
    private EditText userName, userQuantity;
    private Button sendDatabtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        getSupportActionBar().hide();

        // Input text of user's name and quantity.
        userName = findViewById((R.id.userName));
        userQuantity = findViewById((R.id.userQuantity));

        // Below line is used to get the instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        // Below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserInfo");

        // initializing our object class variable.
        UserInfo = new UserInfo();

        sendDatabtn = findViewById(R.id.continueBtn);

        databaseReference.setValue("HOLA");
        // adding on click listener for our button.
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String name = userName.getText().toString();
                String quantity = userQuantity.getText().toString();

                // below line is for checking weather the edittext fields are empty or not.
                if (TextUtils.isEmpty(name)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(NewAccount.this, "Afegeix les dades.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add data to our database.
                    addDatatoFirebase(name, quantity);
                }
            }
        });

        Window window = NewAccount.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(NewAccount.this, R.color.lila));
    }

    public void addDatatoFirebase(String name, String quantity) {
        //  Below this lines of code are used to set data in our object class.
        UserInfo.setName(name);
        UserInfo.setQuantity(50);
        databaseReference.setValue(UserInfo);
        // We are use add value event listener method which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(UserInfo);

                // After adding this data we are showing toast message.
                Toast.makeText(NewAccount.this, "La configuració s'ha realitzat correctament", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewAccount.this, HomePage.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // If the data is not added or it is cancelled then we are displaying a failure toast message.
                Toast.makeText(NewAccount.this, "Error en configurar el compte " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}