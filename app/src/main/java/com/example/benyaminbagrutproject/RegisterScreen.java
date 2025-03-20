package com.example.benyaminbagrutproject;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {


    protected EditText etUsername,etPassword,etPasswordConfirm , etName;

    protected Button btnRegister;
    private FirebaseAuth auth;
    protected DatabaseReference dbRef;

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


                            User user  =new User(etName.getText().toString(), etUsername.getText().toString());
                            user.setUserID(uRef);



                            //for meetslist testing purposes
/*
                            ArrayList<Meet> arr = new ArrayList<>();
                            Meet m =  new Meet();
                            m.setName("meet 1");
                            BasicActivity basicActivity1 = new BasicActivity();
                            basicActivity1.setTitle("activity1");
                            m.getActivities().add(basicActivity1);

                            BasicActivity basicActivity2 = new BasicActivity();
                            basicActivity2.setTitle("activity2");
                            m.getActivities().add(basicActivity2);

                            arr.add(m);
                            m =  new Meet();
                            m.setName("meet 2");
                            arr.add(m);
                            user.setMeetsList(arr);
*/

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