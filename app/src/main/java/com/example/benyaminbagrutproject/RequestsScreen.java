package com.example.benyaminbagrutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RequestsScreen extends AppCompatActivity implements View.OnClickListener {
    protected Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_screen);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
        }
    }
}