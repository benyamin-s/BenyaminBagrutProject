package com.example.benyaminbagrutproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.snapshot.Index;

import java.util.ArrayList;

public class MyMeetsScreen extends AppCompatActivity implements View.OnClickListener {
    protected FirebaseHelper firebaseHelper;

    protected MeetsAdapter meetsAdapter;

    protected Button btnNewMeet , btnBack;

    protected ListView lvMeets;

    protected int index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meets_screen);
        Log.d("log debugger", "onCreate: mymeetsscreen");


        btnNewMeet = findViewById(R.id.btnNewMeet);
        lvMeets = findViewById(R.id.lvMeets);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);



        firebaseHelper = FirebaseHelper.getInstance(this);

        if (firebaseHelper.getUser().getMeetsList() != null){
            meetsAdapter = new MeetsAdapter(this,0,firebaseHelper.getUser().getMeetsList());
            lvMeets.setAdapter(meetsAdapter);
        }



        btnNewMeet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnNewMeet)
        {
            Intent i = new Intent(this, EditMeetScreen.class);
            i.putExtra("meet type",Meet.NEW_MEET);
            startActivity(i);
            finish();
        }
        else if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
            finish();
        }
    }
}