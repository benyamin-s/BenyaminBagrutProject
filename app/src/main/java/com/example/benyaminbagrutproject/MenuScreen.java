package com.example.benyaminbagrutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuScreen extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvName,tvEmail;
    protected Button btnMyMeets,btnSearchActivities,btnRequests,btnSettings,btnDisconnect;

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
        btnMyMeets = findViewById(R.id.btnMyMeets);
        btnSearchActivities = findViewById(R.id.btnSearchActivities);


        btnDisconnect.setOnClickListener(this);
        btnSearchActivities.setOnClickListener(this);
        btnRequests.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnMyMeets.setOnClickListener(this);


        Handler handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        if (message.arg1 == 1)
                        {
                            tvName.setText(firebaseHelper.getUser().getName());
                            tvEmail.setText(firebaseHelper.getUser().getEmail());
                        }
                        return true;
                    }
                }
        );


        firebaseHelper = FirebaseHelper.getInstance(this, handler);


    }

    @Override
    public void onClick(View view) {
        Intent i;
        if(view == btnDisconnect){
            firebaseHelper.SignOut();
            i = new Intent(this, LoginScreen.class);
        }


        else if(view == btnMyMeets){

            i = new Intent(this, MyMeetsScreen.class);
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



}