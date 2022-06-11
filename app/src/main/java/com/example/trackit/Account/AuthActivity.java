package com.example.trackit.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.initActivities.Info_Welcome_Page;
import com.example.trackit.R;
import com.example.trackit.initActivities.Splash_Screen2;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

// ADD POSSIBILITY TO SEE PASSWORD
public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final int GOOGLE_SIGN_IN = 100;

    public static final String CREDENTIALS = "credentials";
    public static final String USER = "user";
    public static final String TYPE = "type";

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Setup
        mAuth = FirebaseAuth.getInstance();

        Window window = AuthActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AuthActivity.this, R.color.softGrey));

        setup();
        biometricSetUp();

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout auth = (LinearLayout) findViewById(R.id.authLayout);
        auth.setVisibility(View.VISIBLE);
    }

    private void setup(){
        setTitle("Sign in");

        Button singIn = (Button) findViewById(R.id.loginButton);

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.emailEditText);
                EditText pass = (EditText) findViewById(R.id.passEditText);

                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                        editor.putString("email", email.getText().toString());
                                        editor.putString("password", pass.getText().toString());
                                        editor.apply();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(AuthActivity.this, ((FirebaseAuthException) task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else if(email.getText().toString().isEmpty() && pass.getText().toString().isEmpty()){
                    Toast.makeText(AuthActivity.this, "S'han d'omplir tots els camps!", Toast.LENGTH_SHORT).show();
                } else if(email.getText().toString().isEmpty()){
                    Toast.makeText(AuthActivity.this, "Has d'indicar l'email!", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(AuthActivity.this, "Has d'indicar la contrassenya!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button fingerprint = findViewById(R.id.fingerprint_login);

        fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        ImageButton google = findViewById(R.id.loginGoogle);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions signInRequest = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("903243164998-qkemilemkmmithmgt64tjv0b6vjhe27a.apps.googleusercontent.com").requestEmail().build();

                GoogleSignInClient googleClient = GoogleSignIn.getClient(AuthActivity.this, signInRequest);

                googleClient.signOut();

                startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SIGN_IN);
            }
        });

        TextView forgotClick = findViewById(R.id.forgotTextView);

        forgotClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //biometricPrompt.authenticate(promptInfo);
                Intent intent = new Intent(AuthActivity.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        TextView registerClick = findViewById(R.id.registerInit);

        registerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthActivity.this, RegisterPage.class);
                startActivity(intent);
                finish();
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(AuthActivity.this, Info_Welcome_Page.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account != null){
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> it) {
                            if(it.isSuccessful()){
                                showHome(account.getEmail());
                                Log.d("frag", "Email of account is " + account.getEmail());
                            }
                        }
                    });
                }

            } catch (ApiException e){
                Log.w("ytsignin", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            EditText email = (EditText) findViewById(R.id.emailEditText);

            String emailUser = email.getText().toString();

            Toast.makeText(this,"Inici de sessió correcte!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Splash_Screen2.class);

            // Save log in status
            saveSession(emailUser, false);

            startActivity(intent);
            finish();
        } else{
            Toast.makeText(this,"Hi ha hagut un error!",Toast.LENGTH_LONG).show();
        }
    }

    private void showHome(String user){
        Toast.makeText(this,"Inici de sessió correcte!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Splash_Screen2.class);

        // Save log in status
        saveSession(user, true);

        startActivity(intent);
        finish();
    }

    // Save log in status
    private void saveSession(String user, boolean drive){
        SharedPreferences saveSession = getSharedPreferences(CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveSession.edit();

        editor.putString(USER, user);

        if(drive){
            editor.putString(TYPE, "DRIVE");
        }else{
            editor.putString(TYPE, "NORMAL");
        }

        editor.commit();
    }

    private void biometricSetUp(){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(),
                        "Device Doesn't have fingerprint", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(),
                        "Not Working", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(),
                        "No FingerPrint Assigned", Toast.LENGTH_SHORT).show();
                break;
        }

        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(AuthActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Login Succes", Toast.LENGTH_SHORT).show();
                String email = sharedPreferences.getString("email", "");
                String password = sharedPreferences.getString("password", "");

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(AuthActivity.this, Splash_Screen2.class);

                                    // Save log in status
                                    saveSession(email, false);

                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(AuthActivity.this, ((FirebaseAuthException) task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(),
                        "Login Fail", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric")
                .setDescription("Use FingerPrint To Login")
                .setDeviceCredentialAllowed(true).build();
    }
}