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

/**
 * Activity for viewing the details of a specific meeting in the youth movement guide application.
 * This screen displays:
 * - Meeting title
 * - Meeting date
 * - List of activities included in the meeting
 * 
 * The meeting data is loaded from Firebase based on the creator ID and meeting ID
 * passed through the intent extras.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class ViewMeetScreen extends AppCompatActivity {

    /** TextView displaying meeting date */
    protected TextView tvDate;
    
    /** TextView displaying meeting title */
    protected TextView tvTitle;
    
    /** ListView displaying meeting activities */
    protected ListView lvListview;
    
    /** Adapter for displaying activities in the ListView */
    protected SearchedActivitiesListAdapter searchedActivitiesListAdapter;

    /** Meet object being displayed */
    protected Meet meet;

    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /**
     * Initializes the activity, sets up UI components and loads meeting data.
     * Creates a handler to process the retrieved meeting data and update the UI
     * when the data is received from Firebase.
     * 
     * Required intent extras:
     * - "creatorID": String - ID of the user who created the meeting
     * - "MeetID": String - Unique identifier of the meeting
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
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