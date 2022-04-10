package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static final String EMAIL = "com.example.trackit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        setup();
    }

    private void setup() {
        TextView initSession = findViewById(R.id.initSessionRegister);

        initSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, AuthActivity.class);
                startActivity(intent);
            }
        });

        Button singUp = (Button) findViewById(R.id.signUpBtn);

        // CHECK IF DATA IS CORRECT
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.emailRegister);
                EditText pass = (EditText) findViewById(R.id.passwordRegister);

                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String message = email.getText().toString();

                                        //Toast.makeText(RegisterPage.this,"Signed In successfully",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterPage.this, Welcome_Page.class);
                                        intent.putExtra(EMAIL, message);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterPage.this,"Hi ha hagut un error!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        ImageButton back = findViewById(R.id.back_signin);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}