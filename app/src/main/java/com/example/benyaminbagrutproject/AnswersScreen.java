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

import java.util.Calendar;

public class AnswersScreen extends AppCompatActivity implements View.OnClickListener {

    protected Request request;
    protected FirebaseHelper firebaseHelper;
    protected ListView lvAnswers;

    protected AnswersAdapter answersAdapter;
    protected Button btnTextAnswer , btnActivityAnswer , btnMeetAnswer , btnBack;

    protected TextView tvDate  ,tvRequester , tvRequest;

    protected Handler newAnswerHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_screen);

        firebaseHelper = FirebaseHelper.getInstance(this);
        Intent intent = getIntent();

        lvAnswers = findViewById(R.id.lvAnswers);

        btnActivityAnswer = findViewById(R.id.btnActivityAnswer);
        btnMeetAnswer = findViewById(R.id.btnMeetAnswer);
        btnTextAnswer = findViewById(R.id.btnTextAnswer);

        btnBack = findViewById(R.id.btnBack);


        tvDate = findViewById(R.id.tvDate);
        tvRequester = findViewById(R.id.tvRequester);
        tvRequest = findViewById(R.id.tvRequest);

        btnActivityAnswer.setOnClickListener(this);
        btnTextAnswer.setOnClickListener(this);
        btnMeetAnswer.setOnClickListener(this);
        btnBack.setOnClickListener(this);




        Handler requestHandler = new Handler(
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

                            answersAdapter = new AnswersAdapter(AnswersScreen.this,0,request.getAnswers());
                            lvAnswers.setAdapter(answersAdapter);
                        }
                        firebaseHelper.getDbRequestsRef().removeEventListener(firebaseHelper.requestsValueEventListener);
                        return true;
                    }
                }
        );
        firebaseHelper.retrieveRequests(requestHandler);

        newAnswerHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.arg1 == FirebaseHelper.DONE_SAVE_ANSWER)
                {
                    answersAdapter = new AnswersAdapter(AnswersScreen.this,0,request.getAnswers());
                    lvAnswers.setAdapter(answersAdapter);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnActivityAnswer)
        {
            ActivityAnswer activityAnswer = new ActivityAnswer(firebaseHelper.getUser().meetsList.get(0).getActivities().get(0), firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_ACTIVITY);
            firebaseHelper.SaveAnswer(request,activityAnswer,newAnswerHandler);
        }
        else if (view == btnMeetAnswer) {
            Meet meet = firebaseHelper.getUser().meetsList.get(0);
            MeetAnswer meetAnswer = new MeetAnswer(meet,"explanation",firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_MEET);
            firebaseHelper.SaveAnswer(request,meetAnswer,newAnswerHandler);

        } else if (view == btnTextAnswer) {
            TextAnswer textAnswer = new TextAnswer("text",firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_TEXT);
            firebaseHelper.SaveAnswer(request,textAnswer,newAnswerHandler);

        }
        else if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
            finish();
        }
    }
}