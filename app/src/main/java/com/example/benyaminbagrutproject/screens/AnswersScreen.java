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

/**
 * Activity for viewing and managing answers to requests in the  application.
 * This screen displays:
 * - Request details (title, content, requester, date)
 * - List of answers provided by other guides
 * - Options to add different types of answers (text, activity, meeting)
 * 
 * The screen provides a bottom navigation menu for selecting answer types
 * and handles the creation and submission of new answers.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class AnswersScreen extends AppCompatActivity implements View.OnClickListener {

    /** Request being viewed */
    protected Request request;
    
    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;
    
    /** ListView displaying answers */
    protected ListView lvAnswers;

    /** Adapter for displaying answers in the ListView */
    protected AnswersAdapter answersAdapter;
    
    /** Button to return to previous screen */
    protected Button btnBack;

    /** TextView displaying request date */
    protected TextView tvDate;
    
    /** TextView displaying requester name */
    protected TextView tvRequester;
    
    /** TextView displaying request title */
    protected TextView tvRequestTitle;
    
    /** TextView displaying request content */
    protected TextView tvRequestContent;

    /** Handler for processing new answer submission */
    protected Handler newAnswerHandler;
    
    /** Bottom navigation menu for answer types */
    protected BottomNavigationView menuAnswers;

    /** Index of selected meeting for meet answers */
    protected int selectedMeet;

    /**
     * Initializes the activity, sets up UI components and loads request data.
     * Creates handlers for processing request data and new answer submissions.
     * Sets up the bottom navigation menu for different answer types.
     * 
     * Required intent extras:
     * - "Index": int - Index of the request in the requests list
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
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

                            if (request.getAnswers() != null){
                                answersAdapter = new AnswersAdapter(AnswersScreen.this,0,request.getAnswers());
                                lvAnswers.setAdapter(answersAdapter);
                            }
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

    /**
     * Handles click events for the back button.
     * Returns to the menu screen when back button is clicked.
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
            finish();
        }
    }

    /**
     * Creates and displays a dialog for submitting a text answer.
     * The dialog allows the user to enter a text response to the request.
     * When submitted, creates a new TextAnswer and saves it to Firebase.
     */
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

    /**
     * Creates and displays a dialog for submitting an activity answer.
     * The dialog allows the user to enter activity details including:
     * - Title
     * - Type
     * - Duration
     * - Explanation
     * - Required equipment
     * 
     * When submitted, creates a new ActivityAnswer with a BasicActivity
     * and saves it to Firebase.
     */
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

    /**
     * Creates and displays a dialog for submitting a meeting answer.
     * The dialog allows the user to select an existing meeting and add
     * an explanation before submitting it as an answer.
     * When submitted, creates a new MeetAnswer and saves it to Firebase.
     */
    public void createMeetAnswerDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.meet_answer_dialog);
        dialog.setTitle(" dialog screen");

        dialog.setCancelable(false);

        selectedMeet = -1;

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