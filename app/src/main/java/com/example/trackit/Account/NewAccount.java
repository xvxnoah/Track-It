package com.example.trackit.Account;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.example.trackit.HomePage;
import com.example.trackit.R;
import com.example.trackit.Model.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NewAccount extends AppCompatActivity {

    // views for button
    private Button btnSelect, btnUpload;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

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
    private SwitchCompat password;
    private boolean enterWithPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        getSupportActionBar().hide();

        // Input text of user's name and quantity.
        userName = findViewById((R.id.userName));
        userQuantity = findViewById((R.id.userQuantity));
        password = findViewById(R.id.password_switch_compact);

        // Below line is used to get the instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        sendDatabtn = findViewById(R.id.continueBtn);

        // initialise views
        btnSelect = findViewById(R.id.galleryProfile);
        imageView = findViewById(R.id.imageProfile);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance("gs://track-it-86761.appspot.com");
        storageReference = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

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

                enterWithPass = password.isChecked();

                if(!quantity.isEmpty() && !name.isEmpty()){
                    // Below line is used to get reference for our database.
                    databaseReference = firebaseDatabase.getReference("users/"+email);

                    // initializing our object class variable.
                    User = UserInfo.getInstance();

                    uploadImage(email);

                    // below line is for checking weather the edittext fields are empty or not.
                    addDatatoFirebase(name, email, quantity);
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

    public void addDatatoFirebase(String name, String email, String quantity) {
        //  Below this lines of code are used to set data in our object class.
        SharedPreferences preferences = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);

        String type = preferences.getString(AuthActivity.TYPE, null);

        if(type.equals("NORMAL")){
            User.setDriveLogin(false);
        } else{
            User.setDriveLogin(true);
        }

        BigDecimal bd = new BigDecimal(quantity).setScale(2, RoundingMode.UNNECESSARY);

        User.setName(name);
        User.setQuantity(bd.doubleValue());
        User.setEmail(preferences.getString(AuthActivity.USER, null));

        databaseReference.setValue(User);
        // We are use add value event listener method which is called with database reference.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.

                // After adding this data we are showing toast message.
                Toast.makeText(NewAccount.this, "La configuració s'ha realitzat correctament", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewAccount.this, HomePage.class);
                startActivity(intent);

                if(enterWithPass){
                    SharedPreferences.Editor profileCheck = getSharedPreferences("checked", MODE_PRIVATE).edit();
                    profileCheck.putString("check", "true");
                    profileCheck.apply();

                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("email", preferences.getString(AuthActivity.USER, null));
                    editor.apply();
                } else{
                    SharedPreferences.Editor profileCheck = getSharedPreferences("checked", MODE_PRIVATE).edit();
                    profileCheck.putString("check", "false");
                    profileCheck.apply();
                }

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
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Selecciona una imatge..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            Button camera = findViewById(R.id.cameraProfile);
            camera.setVisibility(View.INVISIBLE);

            Button galllery = findViewById(R.id.galleryProfile);
            galllery.setVisibility(View.INVISIBLE);
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage(String email)
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String imageStr = email + UUID.randomUUID().toString();
            User.setImageStr(imageStr) ;

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/" + imageStr);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(NewAccount.this,
                                                    "Imatge de perfil pujada",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(NewAccount.this,
                                            "Error " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Pujat "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

}