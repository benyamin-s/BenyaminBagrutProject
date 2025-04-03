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
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditMeetScreen extends AppCompatActivity implements View.OnClickListener {

    protected ListView lvActivitiesList;
    protected FirebaseHelper firebaseHelper;

    protected MyActivitiesListAdapter activitiesListAdapter;
    protected Meet newMeet;

    protected Button btnSave,btnCancel,btnAddActivity;

    protected EditText etTitle,etDate;

    protected int position , meetType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_edit_screen);

        Log.d("log debugger", "onCreate: meet_edit_screen - meet Index: " + getIntent().getIntExtra("meet position",-1));

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


        activitiesListAdapter  = new MyActivitiesListAdapter(this,0 , newMeet.getActivities());
        lvActivitiesList.setAdapter(activitiesListAdapter);

    }

    @Override
    public void onClick(View view) {

        if (view == btnSave)
        {
            newMeet.setName(etTitle.getText().toString());
            //TODO save the date - check if works
            try {
                // Define the date string
                String dateString = etDate.getText().toString();

                // Define the format of the input string
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                // Parse the date string into a Date object
                Date date = formatter.parse(dateString);

                // Convert the Date object to a long (milliseconds)
                long timeInMillis = date.getTime();

                newMeet.setDate(timeInMillis);

            } catch (Exception e) {
                e.printStackTrace();
            }



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
            basicActivity.setCreatorID(firebaseHelper.getUserId());
            basicActivity.setMeetID(newMeet.getMeetID());

            //TODO find if better way exists
            newMeet.getActivities().add(basicActivity);
            activitiesListAdapter  = new MyActivitiesListAdapter(this,0 , newMeet.getActivities());
            lvActivitiesList.setAdapter(activitiesListAdapter);


        }
    }
}