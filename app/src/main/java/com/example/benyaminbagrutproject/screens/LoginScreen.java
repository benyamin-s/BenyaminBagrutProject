package com.example.benyaminbagrutproject.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benyaminbagrutproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity handling user authentication in the  application.
 * This screen allows users to sign in with their email and password, or navigate
 * to the registration screen to create a new account.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    /** EditText field for entering username/email */
    protected EditText etUsername;
    
    /** EditText field for entering password */
    protected EditText etPassword;
    
    /** Button to trigger login process */
    protected Button btnLogin;
    
    /** Button to navigate to registration screen */
    protected Button btnRegister;

    /** Firebase Authentication instance */
    private FirebaseAuth auth;

    /**
     * Initializes the activity, sets up UI components and Firebase authentication.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        Log.d("log debugger", "onCreate: lOGINSCREEN");

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        auth= FirebaseAuth.getInstance();

    }

    /**
     * Handles click events for login and register buttons.
     * - Login button validates input and attempts authentication
     * - Register button navigates to registration screen
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            if (!etUsername.getText().toString().equals("") && !etPassword.getText().toString().equals(""))
                signIn(etUsername.getText().toString(), etPassword.getText().toString());
            else if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals(""))
                Toast.makeText(LoginScreen.this, "username and password can't be empty", Toast.LENGTH_SHORT).show();
        }
        else if (view == btnRegister){
            startActivity(new Intent(this, RegisterScreen.class));
            finish();
        }
    }

    /**
     * Firebase authentication state listener.
     * Handles transitions between signed-in and signed-out states.
     */
    private FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener()
    {
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user=firebaseAuth.getCurrentUser();

            if(user!=null)
            {
                Toast.makeText(LoginScreen.this, "user is signed in.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginScreen.this, MenuScreen.class);
                startActivity(intent);
                finish();
            }
            else
            {

                //user signed out
                Toast.makeText(LoginScreen.this, "user signed out.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Attempts to sign in the user with provided credentials.
     * Shows a progress dialog during authentication and handles the result.
     * 
     * @param email User's email address
     * @param passw User's password
     */
    private void signIn(String email, String passw) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("signing in");
        progressDialog.setMessage("please wait");
        progressDialog.show();
        auth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginScreen.this, "signIn Successful.", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(LoginScreen.this, MenuScreen.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                    finish();
                }
                else
                {
                    Toast.makeText(LoginScreen.this, "signIn failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    task.getException().printStackTrace();

                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }
}