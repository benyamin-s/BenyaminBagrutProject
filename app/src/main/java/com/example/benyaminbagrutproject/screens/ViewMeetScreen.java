package com.example.benyaminbagrutproject.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.Meet;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.listviewadapters.SearchedActivitiesListAdapter;

import java.util.Calendar;

public class ViewMeetScreen extends AppCompatActivity {

    protected TextView tvDate , tvTitle;
    protected ListView lvListview;
    protected SearchedActivitiesListAdapter searchedActivitiesListAdapter;

    protected Meet meet;

    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meet_screen);
        firebaseHelper = FirebaseHelper.getInstance(this);

        Handler handler  = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.arg1 == Meet.MEET_OBTAINED)
                {
                    meet = (Meet) message.obj;
                    searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(ViewMeetScreen.this,0,meet.getActivities());
                    lvListview.setAdapter(searchedActivitiesListAdapter);
                    tvTitle.setText(meet.getName());


                    Calendar calendar = Calendar.getInstance();
                    if (meet.getDate() != null) {
                        calendar.setTimeInMillis(meet.getDate());
                        tvDate.setText(calendar.get(android.icu.util.Calendar.DAY_OF_MONTH) + "/" + (calendar.get(android.icu.util.Calendar.MONTH) + 1) + "/" + calendar.get(android.icu.util.Calendar.YEAR));
                    }
                }
                return true;
            }
        });

        firebaseHelper.FindMeet(getIntent().getStringExtra("creatorID"),getIntent().getStringExtra("MeetID"),handler);


        tvDate = findViewById(R.id.tvDate);
        tvTitle = findViewById(R.id.tvTitle);

        lvListview = findViewById(R.id.lvListview);


    }
}