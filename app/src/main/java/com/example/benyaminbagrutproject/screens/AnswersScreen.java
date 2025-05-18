package com.example.benyaminbagrutproject.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.benyaminbagrutproject.ActivityAnswer;
import com.example.benyaminbagrutproject.Answer;
import com.example.benyaminbagrutproject.listviewadapters.AnswersAdapter;
import com.example.benyaminbagrutproject.BasicActivity;
import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.Meet;
import com.example.benyaminbagrutproject.MeetAnswer;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.Request;
import com.example.benyaminbagrutproject.TextAnswer;
import com.example.benyaminbagrutproject.listviewadapters.ViewMeetsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Calendar;

public class AnswersScreen extends AppCompatActivity implements View.OnClickListener {

    protected Request request;
    protected FirebaseHelper firebaseHelper;
    protected ListView lvAnswers;

    protected AnswersAdapter answersAdapter;
    protected Button  btnBack;

    protected TextView tvDate  ,tvRequester , tvRequestTitle , tvRequestContent;

    protected Handler newAnswerHandler;
    protected BottomNavigationView menuAnswers;

    protected int selectedMeet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_screen);

        firebaseHelper = FirebaseHelper.getInstance(this);
        Intent intent = getIntent();

        lvAnswers = findViewById(R.id.lvAnswers);



        btnBack = findViewById(R.id.btnBack);
        menuAnswers = findViewById(R.id.menuAnswers);

        tvDate = findViewById(R.id.tvDate);
        tvRequester = findViewById(R.id.tvRequester);
        tvRequestTitle = findViewById(R.id.tvRequestTitle);
        tvRequestContent = findViewById(R.id.tvRequestContent);


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

        menuAnswers.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itemMeetAnswer) {
                    createMeetAnswerDialog();
                } else if (id == R.id.itemActivityAnswer) {
                    createActivityAnswerDialog();
                } else if (id == R.id.itemTextAnswer) {
                    createTextAnswerDialog();
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
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
                TextAnswer textAnswer = new TextAnswer(etAnswerText.getText().toString(),firebaseHelper.getUserId(),firebaseHelper.getUser().getName(), Answer.TYPE_TEXT);

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
        BasicActivity basicActivity = new BasicActivity();

        basicActivity.setCreatorID(firebaseHelper.getUserId());

        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.activity_answer_dialog);
        dialog.setTitle(" dialog screen");

        dialog.setCancelable(false);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        EditText etTitle,   etTime, etExplanation,etEquipment;

        etExplanation = dialog.findViewById(R.id.etExplanation);
        etEquipment = dialog.findViewById(R.id.etEquipment);
        etTitle = dialog.findViewById(R.id.etTitle);
        etTime = dialog.findViewById(R.id.etTime);

        Spinner spinType = dialog.findViewById(R.id.spinType);
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BasicActivity.types);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(ad);
        if (basicActivity.getType() != null)
            for (int i = 0;i < BasicActivity.types.length;i++) {
                if (basicActivity.getType().equals(BasicActivity.types[i]))
                    spinType.setSelection(i);

            }

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                basicActivity.setType(BasicActivity.types[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivity.setEquipment(etEquipment.getText().toString());
                basicActivity.setExplanation(etExplanation.getText().toString());
                basicActivity.setTitle(etTitle.getText().toString());
                basicActivity.setTime(Long.parseLong(etTime.getText().toString()));
                basicActivity.setCreator(firebaseHelper.getUser().getName());

                Calendar calendar = Calendar.getInstance();
                basicActivity.setDate(calendar.getTimeInMillis());

                ActivityAnswer activityAnswer = new ActivityAnswer(basicActivity,firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_ACTIVITY);

                firebaseHelper.SaveAnswer(request,activityAnswer,newAnswerHandler);
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

                    MeetAnswer meetAnswer = new MeetAnswer(meet,etAnswerExplanation.getText().toString(),firebaseHelper.getUserId(),firebaseHelper.getUser().getName(),Answer.TYPE_MEET);

                    firebaseHelper.SaveAnswer(request,meetAnswer,newAnswerHandler);
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