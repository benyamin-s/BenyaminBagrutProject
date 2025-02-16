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

public class EditMeetScreen extends AppCompatActivity implements View.OnClickListener {

    protected ListView lvActivitiesList;
    protected FirebaseHelper firebaseHelper;

    protected ActivitiesListAdapter activitiesListAdapter;
    protected Meet newMeet;

    protected Button btnSave,btnCancel,btnAddActivity;

    protected EditText etTitle;




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
        if (getIntent().getIntExtra("meet type",-1) == Meet.EDIT_MEET) {
            Meet meet = firebaseHelper.getUser().getMeetsList().get(getIntent().getIntExtra("meet position", -1));


            newMeet.setMeetID(meet.getMeetID());
            newMeet.setName(meet.getName());
            newMeet.setDate(meet.getDate());

            for (BasicActivity basicActivity : meet.getActivities()) {
                newMeet.getActivities().add(basicActivity.CopyActivity());
            }

            etTitle = findViewById(R.id.etTitle);
            etTitle.setText(meet.getName());
        }
        else if (getIntent().getIntExtra("meet type",-1) == Meet.NEW_MEET)
        {
            //TODO decide how to create new meets
            ;
        }
        else Log.d("log debugger", "not new meet nor edit meet");
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnExit);
        btnAddActivity = findViewById(R.id.btnAddActivity);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAddActivity.setOnClickListener(this);

        lvActivitiesList = findViewById(R.id.lvListview);

        activitiesListAdapter  = new ActivitiesListAdapter(this,0 , newMeet.getActivities());
        lvActivitiesList.setAdapter(activitiesListAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave)
        {

        }
        else if (view == btnCancel){}
        else if (view ==btnAddActivity){}
    }
}