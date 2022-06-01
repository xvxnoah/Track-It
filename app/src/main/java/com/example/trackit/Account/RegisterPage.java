package com.example.trackit.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.R;
import com.example.trackit.initActivities.Info_Welcome_Page;
import com.example.trackit.initActivities.Welcome_Page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    boolean show = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        Window window = RegisterPage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(RegisterPage.this, R.color.softGrey));

        setup();

        Button btn = findViewById(R.id.buttonPSW);
        EditText psw = findViewById((R.id.passwordRegister));
        EditText email = findViewById((R.id.emailRegister));
        psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        email.setInputType(InputType.TYPE_CLASS_TEXT);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show){
                    btn.setForeground(getResources().getDrawable(R.drawable.ojo1));
                    show = true;
                    psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    psw.setSelection(psw.getText().length());
                }else{
                    btn.setForeground(getResources().getDrawable(R.drawable.ojo2));
                    show = false;
                    psw.setInputType(InputType.TYPE_CLASS_TEXT);
                    psw.setSelection(psw.getText().length());
                }
            }
        });
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
                                        String userEmail = email.getText().toString();

                                        Intent intent = new Intent(RegisterPage.this, Welcome_Page.class);
                                        saveSession(userEmail);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Log.e("===>", task.getException().toString());
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterPage.this,"Hi ha hagut un error!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        ImageButton back = findViewById(R.id.back_register);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(RegisterPage.this, Info_Welcome_Page.class));
        finish();
    }

    // Save log in status
    private void saveSession(String user){
        SharedPreferences saveSession = getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveSession.edit();

        editor.putString(AuthActivity.USER, user);
        editor.putString(AuthActivity.TYPE, "NORMAL");
        
        editor.commit();
    }
}