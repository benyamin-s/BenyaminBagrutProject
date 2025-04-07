package com.example.benyaminbagrutproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuScreen extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvName,tvEmail;
    protected Button btnMyMeets,btnSearchActivities,btnRequests,btnSettings,btnDisconnect;

    protected FirebaseHelper firebaseHelper;


    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK)
                    {
                        firebaseHelper.getUser().setName(o.getData().getStringExtra("name"));
                        firebaseHelper.getUser().setBeforeMeetNotification(o.getData().getBooleanExtra("notifications",false));
                        firebaseHelper.getUser().setTimeBeforeMeetNotif(o.getData().getIntExtra("time before",0));
                        Calendar calendar = Calendar.getInstance();

                        Handler handler = new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull Message msg) {
                                if (msg.arg1 == User.USER_UPDATED)
                                {
                                    tvName.setText(o.getData().getStringExtra("name"));

                                    //TODO update alarms

                                    for (int i = 0; i < firebaseHelper.getUser().getMeetsList().size();i++) {
                                        Meet m = firebaseHelper.getUser().getMeetsList().get(i);
                                        AlarmReciever.cancelAlarm(MenuScreen.this,m.getDate());
                                        if (firebaseHelper.getUser().beforeMeetNotification && m.getDate() > calendar.getTimeInMillis() + firebaseHelper.getUser().TimeBeforeMeetNotif * 1000)
                                        {
                                            AlarmReciever.ScheduleMeetAlarm(MenuScreen.this,i , m.getDate()-firebaseHelper.getUser().TimeBeforeMeetNotif * 60000);
                                        }
                                    }

                                }
                                return true;
                            }
                        });

                        firebaseHelper.UpdateSettings(handler);

                    }
                }
            }
    );





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d("log debugger", "onCreate: MenuScreen");

        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);

        btnDisconnect = findViewById(R.id.btnDisconnect);
        btnRequests  = findViewById(R.id.btnRequests);
        btnSettings = findViewById(R.id.btnSettings);
        btnMyMeets = findViewById(R.id.btnMyMeets);
        btnSearchActivities = findViewById(R.id.btnSearchActivities);


        btnDisconnect.setOnClickListener(this);
        btnSearchActivities.setOnClickListener(this);
        btnRequests.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnMyMeets.setOnClickListener(this);


        Handler handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        if (message.arg1 == FirebaseHelper.DONE_RETRIEVE_USER_DATA)
                        {
                            tvName.setText(firebaseHelper.getUser().getName());
                            tvEmail.setText(firebaseHelper.getUser().getEmail());
                        }
                        return true;
                    }
                }
        );


        firebaseHelper = FirebaseHelper.getInstance(this);

        firebaseHelper.retrieveUserData(handler);

    }

    @Override
    public void onClick(View view) {
        Intent i;
        if(view == btnDisconnect){
            firebaseHelper.SignOut();
            i = new Intent(this, LoginScreen.class);
            startActivity(i);

        }


        else if(view == btnMyMeets){

            i = new Intent(this, MyMeetsScreen.class);
            startActivity(i);

        }

        else if(view == btnRequests){

            i = new Intent(this, RequestsScreen.class);
            startActivity(i);

        }

        else if(view == btnSettings){
            i = new Intent(this, SettingsScreen.class);
            settingsLauncher.launch(i);
        }

        else
        {
            //btnSearchActivities

            i = new Intent(this, SearchActivitiesScreen.class);
            startActivity(i);

        }

    }



}