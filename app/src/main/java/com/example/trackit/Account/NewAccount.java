package com.example.trackit.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trackit.HomePage;
import com.example.trackit.R;
import com.example.trackit.Model.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewAccount extends AppCompatActivity {

    public static final String SETUP_USER = "";
    public static final String SETUP_EMAIL = "";

    private Spinner expenseCategories;

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for our object class
    com.example.trackit.Model.UserInfo User;

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

        sendDatabtn = findViewById(R.id.continueBtn);

        // adding on click listener for our button.
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting text from our edittext fields.
                SharedPreferences preferences = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
                String email = preferences.getString(AuthActivity.USER, null);
                email = email.replace('.', ',');

                String name = userName.getText().toString();

                String quantity = userQuantity.getText().toString();

                if(!quantity.isEmpty() && !name.isEmpty()){
                    // Below line is used to get reference for our database.
                    databaseReference = firebaseDatabase.getReference("users/"+email);

                    // initializing our object class variable.
                    User = UserInfo.getInstance();

                    // below line is for checking weather the edittext fields are empty or not.
                    addDatatoFirebase(name, email, Double.valueOf(quantity));
                } else if(quantity.isEmpty() && name.isEmpty()){
                    Toast.makeText(NewAccount.this, "S'han d'omplir tots els camps!", Toast.LENGTH_LONG).show();
                } else if(name.isEmpty()){
                    Toast.makeText(NewAccount.this, "Has d'introduir un nom d'usuari!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(NewAccount.this, "Has d'introduir una quantitat!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Window window = NewAccount.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(NewAccount.this, R.color.lila));

        ImageButton back = findViewById(R.id.back_setup);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    public void addDatatoFirebase(String name, String email, Double quantity) {
        //  Below this lines of code are used to set data in our object class.
        SharedPreferences preferences = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);

        String type = preferences.getString(AuthActivity.TYPE, null);

        if(type.equals("NORMAL")){
            User.setDriveLogin(false);
        } else{
            User.setDriveLogin(true);
        }

        User.setName(name);
        User.setQuantity(quantity);
        User.setEmail(preferences.getString(AuthActivity.USER, null));

        databaseReference.setValue(User);
        // We are use add value event listener method which is called with database reference.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.

                //databaseReference.setValue(User);

                // After adding this data we are showing toast message.
                Toast.makeText(NewAccount.this, "La configuració s'ha realitzat correctament", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewAccount.this, HomePage.class);
                startActivity(intent);

                SharedPreferences saveUser = getSharedPreferences(SETUP_USER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveUser.edit();

                editor.putString(SETUP_EMAIL, email);
                editor.commit();

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // If the data is not added or it is cancelled then we are displaying a failure toast message.
                Toast.makeText(NewAccount.this, "Error en configurar el compte " + error, Toast.LENGTH_SHORT).show();
            }
        });

        /* Save to sharedPreferences
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(User);
        prefsEditor.putString("UserInfo", json);
        prefsEditor.commit();
        */
    }

}