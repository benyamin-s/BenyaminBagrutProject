package com.example.benyaminbagrutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

public class AnswersActivity extends AppCompatActivity {

    protected Request request;
    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        firebaseHelper = FirebaseHelper.getInstance(this);
        Intent intent = getIntent();


        Handler handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if (msg.arg1 == FirebaseHelper.DONE_RETRIEVE_REQUESTS){
                            request = firebaseHelper.getRequestsList().get(intent.getIntExtra("Index" , -1));
                        }
                        return true;
                    }
                }
        );
        firebaseHelper.retrieveRequests(handler);

    }
}