package com.example.benyaminbagrutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuScreen extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvName,tvEmail;
    protected Button btnMyActivties,btnSearchActivities,btnRequests,btnSettings,btnDisconnect;

    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d("log debugger", "onCreate: MenuScreen");

        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);

        btnDisconnect = findViewById(R.id.btnDisconnect);
        btnRequests  = findViewById(R.id.btnRequests);
        btnSettings = findViewById(R.id.btnSettings);
        btnMyActivties = findViewById(R.id.btnMyActivities);
        btnSearchActivities = findViewById(R.id.btnSearchActivities);


        btnDisconnect.setOnClickListener(this);
        btnSearchActivities.setOnClickListener(this);
        btnRequests.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnMyActivties.setOnClickListener(this);



        firebaseHelper = FirebaseHelper.getInstance(this);
        retrieveUserData();
    }

    @Override
    public void onClick(View view) {
        Intent i;
        if(view == btnDisconnect){
            firebaseHelper.SignOut();
            i = new Intent(this, LoginScreen.class);
        }


        else if(view == btnMyActivties){

            i = new Intent(this, MyActivitiesScreen.class);
        }

        else if(view == btnRequests){

            i = new Intent(this, RequestsScreen.class);
        }

        else if(view == btnSettings){
            i = new Intent(this, SettingsScreen.class);
        }

        else
        {
            //btnSearchActivities

            i = new Intent(this, SearchActivitiesScreen.class);
        }

        startActivity(i);
    }


    public void retrieveUserData()
    {
        User user = firebaseHelper.retrieveUserData();
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
    }
}