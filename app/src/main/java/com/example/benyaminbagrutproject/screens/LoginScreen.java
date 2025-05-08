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

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {


    protected EditText etUsername,etPassword;
    protected Button btnLogin,btnRegister;

    private FirebaseAuth auth;


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