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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class EditMeetScreen extends AppCompatActivity implements View.OnClickListener {

    protected ListView lvActivitiesList;
    protected FirebaseHelper firebaseHelper;

    protected ActivitiesListAdapter activitiesListAdapter;
    protected Meet newMeet;

    protected Button btnSave,btnCancel,btnAddActivity;

    protected EditText etTitle,etDate;

    protected int position , meetType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_edit_screen);

        Log.d("log debugger", "onCreate: meet_edit_screen - meet Index: " + getIntent().getIntExtra("meet position",-1));

        Handler handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        return false;
                    }
                }
        );
        firebaseHelper = FirebaseHelper.getInstance(this);
        newMeet = new Meet();

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnExit);
        btnAddActivity = findViewById(R.id.btnAddActivity);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAddActivity.setOnClickListener(this);

        lvActivitiesList = findViewById(R.id.lvListview);
        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);

        //check if new meet or existing one
        meetType = getIntent().getIntExtra("meet type",-1);
        if (meetType == Meet.EDIT_MEET) {
            position = getIntent().getIntExtra("meet position", -1);
            Meet meet;
            if (position != -1)
                meet = firebaseHelper.getUser().getMeetsList().get(position);
            else
            {
                meet = new Meet();
                Log.d("log debugger", "position error");
            }

            newMeet.setMeetID(meet.getMeetID());
            newMeet.setName(meet.getName());
            newMeet.setDate(meet.getDate());

            for (BasicActivity basicActivity : meet.getActivities()) {
                newMeet.getActivities().add(basicActivity.CopyActivity());
            }

            etTitle.setText(meet.getName());
            if (newMeet.getDate() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(newMeet.getDate());
                etDate.setText(calendar.DAY_OF_MONTH + "/" + calendar.MONTH + 1 + "/" + calendar.YEAR);
            }
        }

        else if (meetType == Meet.NEW_MEET)
        {
            ;
        }
        else Log.d("log debugger", "not new meet nor edit meet");


        activitiesListAdapter  = new ActivitiesListAdapter(this,0 , newMeet.getActivities());
        lvActivitiesList.setAdapter(activitiesListAdapter);
    }

    @Override
    public void onClick(View view) {

        if (view == btnSave)
        {
            newMeet.setName(etTitle.getText().toString());
            //TODO save the date
            String date = etDate.getText().toString();


            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    startActivity(new Intent(EditMeetScreen.this, MyMeetsScreen.class));
                    finish();
                    return true;
                }
            });

            firebaseHelper.SaveMeet(position,newMeet,meetType,handler);

        }
        else if (view == btnCancel)
        {
            startActivity(new Intent(this, MyMeetsScreen.class));
            finish();
        }
        else if (view ==btnAddActivity)
        {
            BasicActivity basicActivity = new BasicActivity();
            //TODO find if better way exists
            newMeet.getActivities().add(basicActivity);
            activitiesListAdapter  = new ActivitiesListAdapter(this,0 , newMeet.getActivities());
            lvActivitiesList.setAdapter(activitiesListAdapter);


        }
    }
}