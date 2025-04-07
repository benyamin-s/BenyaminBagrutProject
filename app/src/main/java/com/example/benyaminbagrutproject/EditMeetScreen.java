package com.example.benyaminbagrutproject;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditMeetScreen extends AppCompatActivity implements View.OnClickListener {

    protected ListView lvActivitiesList;
    protected FirebaseHelper firebaseHelper;

    protected MyActivitiesListAdapter activitiesListAdapter;
    protected Meet newMeet;

    protected Button btnSave,btnCancel,btnAddActivity;

    protected EditText etTitle;

    protected TextView tvDate,tvTime;
    protected int position , meetType;

    protected TimePickerDialog timePickerDialog;
    protected DatePickerDialog datePickerDialog;

    protected Calendar calendar;




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
                newMeet.getActivities().add(basicActivity.CopyActivity());
            }

            etTitle.setText(meet.getName());


            if (newMeet.getDate() != null) {
                calendar.setTimeInMillis(newMeet.getDate());
                tvDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                if (calendar.get(Calendar.MINUTE) > 9)
                    tvTime.setText(calendar.get(Calendar.HOUR_OF_DAY) +":" + calendar.get(Calendar.MINUTE));
                else
                    tvTime.setText(calendar.get(Calendar.HOUR_OF_DAY) +":0" + calendar.get(Calendar.MINUTE));

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),hour,minute);
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
                        newMeet.setDate(calendar.getTimeInMillis());
                        tvDate.setText(day + "/" + month+1  + "/" + year);
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
                    newMeet.setDate(calendar.getTimeInMillis());
                    tvTime.setText(hour +":" + minute);
                }
            };
            timePickerDialog = new TimePickerDialog(this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);


            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(year,month,day);
                    newMeet.setDate(calendar.getTimeInMillis());
                    tvDate.setText(day + "/" + month+1  + "/" + year);
                }
            };

            datePickerDialog = new DatePickerDialog(this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
        else if(view == tvDate)
        {
            datePickerDialog.show();
        }
        else if(view == tvTime)
        {
            timePickerDialog.show();
        }

    }
}