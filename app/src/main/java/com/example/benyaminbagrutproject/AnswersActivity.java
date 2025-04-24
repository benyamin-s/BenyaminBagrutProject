package com.example.benyaminbagrutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AnswersActivity extends AppCompatActivity implements View.OnClickListener {

    protected Request request;
    protected FirebaseHelper firebaseHelper;
    protected ListView lvAnswers;
    protected Button btnTextAnswer , btnActivityAnswer , btnMeetAnswer;

    protected TextView tvDate  ,tvRequester , tvRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        firebaseHelper = FirebaseHelper.getInstance(this);
        Intent intent = getIntent();

        lvAnswers = findViewById(R.id.lvAnswers);

        btnActivityAnswer = findViewById(R.id.btnActivityAnswer);
        btnMeetAnswer = findViewById(R.id.btnMeetAnswer);
        btnTextAnswer = findViewById(R.id.btnTextAnswer);

        tvDate = findViewById(R.id.tvDate);
        tvRequester = findViewById(R.id.tvRequester);
        tvRequest = findViewById(R.id.tvRequest);

        btnActivityAnswer.setOnClickListener(this);
        btnTextAnswer.setOnClickListener(this);
        btnMeetAnswer.setOnClickListener(this);




        Handler handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if (msg.arg1 == FirebaseHelper.DONE_RETRIEVE_REQUESTS){
                            request = firebaseHelper.getRequestsList().get(intent.getIntExtra("Index" , -1));

                            Calendar calendar = Calendar.getInstance();
                            if (request.getDate() != null) {
                                calendar.setTimeInMillis(request.getDate());
                                tvDate.setText(calendar.get(android.icu.util.Calendar.DAY_OF_MONTH) + "/" + (calendar.get(android.icu.util.Calendar.MONTH) + 1) + "/" + calendar.get(android.icu.util.Calendar.YEAR));
                            }

                            tvRequester.setText(request.getRequesterName());
                            tvRequest.setText(request.getRequest());
                        }
                        firebaseHelper.getDbRequestsRef().removeEventListener(firebaseHelper.requestsValueEventListener);
                        return true;
                    }
                }
        );
        firebaseHelper.retrieveRequests(handler);
    }

    @Override
    public void onClick(View view) {
        if (view == btnActivityAnswer)
        {

        } else if (view == btnMeetAnswer) {

        } else if (view == btnTextAnswer) {

        }
    }
}