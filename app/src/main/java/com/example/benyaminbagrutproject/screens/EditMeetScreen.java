package com.example.benyaminbagrutproject.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.benyaminbagrutproject.BasicActivity;
import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.Meet;
import com.example.benyaminbagrutproject.listviewadapters.MyActivitiesListAdapter;
import com.example.benyaminbagrutproject.R;

import java.util.Calendar;

/**
 * Activity for creating and editing meetings in the youth movement guide application.
 * This screen allows guides to:
 * - Create new meetings or edit existing ones
 * - Set meeting title, date, and time
 * - Add and manage activities within the meeting
 * - Save meeting changes to Firebase
 * 
 * The screen handles both new meeting creation and editing of existing meetings
 * based on the intent extras passed to it.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class EditMeetScreen extends AppCompatActivity implements View.OnClickListener {

    /** ListView displaying activities in the meeting */
    protected ListView lvActivitiesList;
    
    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /** Adapter for displaying activities in the ListView */
    protected MyActivitiesListAdapter activitiesListAdapter;
    
    /** Meet object being created or edited */
    protected Meet newMeet;

    /** Button to save meeting changes */
    protected Button btnSave;
    
    /** Button to cancel editing */
    protected Button btnCancel;
    
    /** Button to add new activity to meeting */
    protected Button btnAddActivity;

    /** EditText for meeting title */
    protected EditText etTitle;

    /** TextView displaying selected date */
    protected TextView tvDate;
    
    /** TextView displaying selected time */
    protected TextView tvTime;
    
    /** Position of meeting in the user's meetings list */
    protected int position;
    
    /** Type of operation (new meeting or edit existing) */
    protected int meetType;

    /** Dialog for picking time */
    protected TimePickerDialog timePickerDialog;
    
    /** Dialog for picking date */
    protected DatePickerDialog datePickerDialog;

    /** Calendar instance for date/time operations */
    protected Calendar calendar;

    /**
     * Initializes the activity, sets up UI components and loads meeting data if editing.
     * Handles both new meeting creation and loading existing meeting data based on
     * intent extras.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
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

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        calendar = Calendar.getInstance();

        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);

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
                newMeet.getActivities().add(new BasicActivity(basicActivity) );
            }

            etTitle.setText(meet.getName());


            if (newMeet.getDate() != null) {
                calendar.setTimeInMillis(newMeet.getDate());
                calendar.set(Calendar.SECOND,0);
                tvDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                if (calendar.get(Calendar.MINUTE) > 9)
                    tvTime.setText(calendar.get(Calendar.HOUR_OF_DAY) +":" + calendar.get(Calendar.MINUTE));
                else
                    tvTime.setText(calendar.get(Calendar.HOUR_OF_DAY) +":0" + calendar.get(Calendar.MINUTE));

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),hour,minute);
                        calendar.set(Calendar.SECOND,0);
                        newMeet.setDate(calendar.getTimeInMillis());
                        if (minute > 9)
                            tvTime.setText(hour +":" + minute);
                        else
                            tvTime.setText(hour +":0" + minute);
                    }
                };
                timePickerDialog = new TimePickerDialog(this ,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year,month,day);
                        calendar.set(Calendar.SECOND,0);

                        newMeet.setDate(calendar.getTimeInMillis());
                        tvDate.setText(day + "/" + (month+1)  + "/" + year);
                    }
                };

                datePickerDialog = new DatePickerDialog(this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
            else
            {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),hour,minute);
                        calendar.set(Calendar.SECOND,0);
                        newMeet.setDate(calendar.getTimeInMillis());
                        if (minute > 9)
                            tvTime.setText(hour +":" + minute);
                        else
                            tvTime.setText(hour +":0" + minute);
                    }
                };
                timePickerDialog = new TimePickerDialog(this ,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year,month,day);
                        calendar.set(Calendar.SECOND,0);

                        newMeet.setDate(calendar.getTimeInMillis());
                        tvDate.setText(day + "/" + (month+1)  + "/" + year);
                    }
                };

                datePickerDialog = new DatePickerDialog(this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        }

        else if (meetType == Meet.NEW_MEET)
        {
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),hour,minute);
                    calendar.set(Calendar.SECOND,0);
                    newMeet.setDate(calendar.getTimeInMillis());
                    tvTime.setText(hour +":" + minute);
                }
            };
            timePickerDialog = new TimePickerDialog(this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);


            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(year,month,day);
                    calendar.set(Calendar.SECOND,0);
                    newMeet.setDate(calendar.getTimeInMillis());
                    tvDate.setText(day + "/" +  (month+1)  + "/" + year);
                }
            };

            datePickerDialog = new DatePickerDialog(this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        else Log.d("log debugger", "not new meet nor edit meet");


        activitiesListAdapter  = new MyActivitiesListAdapter(this,0 , newMeet.getActivities());
        lvActivitiesList.setAdapter(activitiesListAdapter);

    }



    /**
     * Handles click events for all interactive UI elements.
     * - Save button saves meeting changes and returns to MyMeetsScreen
     * - Cancel button discards changes and returns to MyMeetsScreen
     * - Add Activity button adds a new blank activity to the meeting
     * - Date TextView shows date picker dialog
     * - Time TextView shows time picker dialog
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {

        if (view == btnSave)
        {
            newMeet.setName(etTitle.getText().toString());

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

            newMeet.getActivities().add(basicActivity);
            activitiesListAdapter  = new MyActivitiesListAdapter(this,0 , newMeet.getActivities());
            lvActivitiesList.setAdapter(activitiesListAdapter);


        }
        else if(view == tvDate)
        {
            datePickerDialog.show();
        }
        else if(view == tvTime)
        {
            timePickerDialog.show();
        }

    }

    /**
     * TimeSetListener for handling time selection in the time picker dialog.
     * Updates the calendar and UI with the selected time.
     */
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH), hour, minute);
            calendar.set(Calendar.SECOND, 0);
            newMeet.setDate(calendar.getTimeInMillis());
            if (minute > 9)
                tvTime.setText(hour + ":" + minute);
            else
                tvTime.setText(hour + ":0" + minute);
        }
    };

    /**
     * DateSetListener for handling date selection in the date picker dialog.
     * Updates the calendar and UI with the selected date.
     */
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
            calendar.set(Calendar.SECOND, 0);
            newMeet.setDate(calendar.getTimeInMillis());
            tvDate.setText(day + "/" + (month + 1) + "/" + year);
        }
    };
}