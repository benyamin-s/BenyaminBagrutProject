package com.example.benyaminbagrutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AnswersScreen extends AppCompatActivity implements View.OnClickListener {

    protected Request request;
    protected FirebaseHelper firebaseHelper;
    protected ListView lvAnswers;

    protected AnswersAdapter answersAdapter;
    protected Button btnTextAnswer , btnActivityAnswer , btnMeetAnswer , btnBack;

    protected TextView tvDate  ,tvRequester , tvRequestTitle , tvRequestContent;

    protected Handler newAnswerHandler;

    protected int selectedMeet;
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
        tvRequestTitle = findViewById(R.id.tvRequestTitle);
        tvRequestContent = findViewById(R.id.tvRequestContent);

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
                            tvRequestTitle.setText(request.getRequestTitle());
                            tvRequestContent.setText(request.getRequestContent());

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
/*
            ActivityAnswer activityAnswer = new ActivityAnswer(firebaseHelper.getUser().meetsList.get(0).getActivities().get(1), firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_ACTIVITY);
            firebaseHelper.SaveAnswer(request,activityAnswer,newAnswerHandler);
*/          createActivityAnswerDialog();
        }

        else if (view == btnMeetAnswer) {
/*
            Meet meet = firebaseHelper.getUser().meetsList.get(1);
            MeetAnswer meetAnswer = new MeetAnswer(meet,"explanation",firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_MEET);
            firebaseHelper.SaveAnswer(request,meetAnswer,newAnswerHandler);
*/          createMeetAnswerDialog();
        } else if (view == btnTextAnswer) {
/*
            TextAnswer textAnswer = new TextAnswer("text",firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_TEXT);
            firebaseHelper.SaveAnswer(request,textAnswer,newAnswerHandler);
*/
            createTextAnswerDialog();
        }
        else if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
            finish();
        }
    }

    public void createTextAnswerDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.text_answer_dialog);
        dialog.setTitle(" dialog screen");

        dialog.setCancelable(false);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        EditText etAnswerText = dialog.findViewById(R.id.etAnswerText);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextAnswer textAnswer = new TextAnswer(etAnswerText.getText().toString(),firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_TEXT);

                firebaseHelper.SaveAnswer(request,textAnswer,newAnswerHandler);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void createActivityAnswerDialog(){
        Dialog dialog=new Dialog(this);
        //dialog.setContentView(/*Todo add the layout*/);
        dialog.setTitle(" dialog screen");

        dialog.setCancelable(false);

        //Todo add the buttons / lists / whatever
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BasicActivity basicActivity = /* TODO */;

                //ActivityAnswer activityAnswer = new ActivityAnswer(basicActivity,firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_ACTIVITY);

                //firebaseHelper.SaveAnswer(request,activityAnswer,newAnswerHandler);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void createMeetAnswerDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.meet_answer_dialog);
        dialog.setTitle(" dialog screen");

        dialog.setCancelable(false);

        selectedMeet = -1;

        //Todo add the buttons / lists / whatever
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        EditText etAnswerExplanation = dialog.findViewById(R.id.etAnswerExplanation);
        ListView lvMeetsList  =dialog.findViewById(R.id.lvMeetsList);

        ArrayList<Meet> meetArrayList = firebaseHelper.getUser().getMeetsList();
        ViewMeetsAdapter viewMeetsAdapter = new ViewMeetsAdapter(this,0,meetArrayList);
        lvMeetsList.setAdapter(viewMeetsAdapter);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMeet = viewMeetsAdapter.getSelectedMeet();
                if (selectedMeet != -1) {
                    Meet meet =  meetArrayList.get(selectedMeet);

                    MeetAnswer activityAnswer = new MeetAnswer(meet,etAnswerExplanation.getText().toString(),firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_MEET);

                    firebaseHelper.SaveAnswer(request,activityAnswer,newAnswerHandler);
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}