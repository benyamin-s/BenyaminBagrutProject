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
import com.example.benyaminbagrutproject.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity for handling new user registration in the  application.
 * This screen allows users to create a new account by providing their email, password,
 * and display name. The user data is stored in Firebase Authentication and Realtime Database.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {

    /** EditText field for entering username/email */
    protected EditText etUsername;
    
    /** EditText field for entering password */
    protected EditText etPassword;
    
    /** EditText field for confirming password */
    protected EditText etPasswordConfirm;
    
    /** EditText field for entering display name */
    protected EditText etName;

    /** Button to trigger registration process */
    protected Button btnRegister;
    
    /** Firebase Authentication instance */
    private FirebaseAuth auth;
    
    /** Firebase Database reference */
    protected DatabaseReference dbRef;

    /**
     * Initializes the activity, sets up UI components and Firebase instances.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);


        Log.d("log debugger", "onCreate: RegisterScreen");

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        etName = findViewById(R.id.etName);

        btnRegister= findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(this);

        auth= FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

    }

    /**
     * Handles click events for the register button.
     * Validates input fields and initiates account creation if validation passes.
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        if (etPassword.getText().toString().equals(etPasswordConfirm.getText().toString()) && !etUsername.getText().toString().equals("") && !etPassword.getText().toString().equals("")){
            creatAccount(etUsername.getText().toString(),etPassword.getText().toString());

        }
        else if(!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString()))
            Toast.makeText(RegisterScreen.this, "confirm password",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(RegisterScreen.this, "username and password can't be empty",Toast.LENGTH_SHORT).show();

    }

    /**
     * Creates a new user account with the provided credentials.
     * If successful, creates a user profile in Firebase Database and navigates to the menu screen.
     * Shows error messages if account creation fails.
     * 
     * @param email User's email address
     * @param passw User's password
     */
    private void creatAccount(String email,String passw){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("signing in");
        progressDialog.setMessage("please wait");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,passw).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(RegisterScreen.this, "Authentication successful",Toast.LENGTH_SHORT).show();

                            String uRef=auth.getCurrentUser().getUid();


                            User user  = new User(etName.getText().toString(), etUsername.getText().toString());



                            dbRef.child("Users").child(uRef).setValue(user);

                            progressDialog.dismiss();

                            startActivity(new Intent(RegisterScreen.this, MenuScreen.class));

                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(RegisterScreen.this, "Authentication fail "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();

                        }


                    }
                }
        );
    }


}