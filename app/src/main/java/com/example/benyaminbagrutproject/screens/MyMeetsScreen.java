package com.example.benyaminbagrutproject.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.Meet;
import com.example.benyaminbagrutproject.listviewadapters.MeetsAdapter;
import com.example.benyaminbagrutproject.R;

/**
 * Activity for displaying and managing a guide's meetings in the youth movement guide application.
 * This screen shows a list of all meetings created or managed by the current user and
 * provides options to create new meetings or edit existing ones.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class MyMeetsScreen extends AppCompatActivity implements View.OnClickListener {
    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /** Adapter for displaying meetings in the ListView */
    protected MeetsAdapter meetsAdapter;

    /** Button to create a new meeting */
    protected Button btnNewMeet;
    
    /** Button to return to previous screen */
    protected Button btnBack;

    /** ListView displaying all meetings */
    protected ListView lvMeets;

    /** Current selected meeting index */
    protected int index;

    /**
     * Initializes the activity, sets up UI components and loads user's meetings.
     * Creates and sets up the meetings list adapter if the user has any meetings.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meets_screen);
        Log.d("log debugger", "onCreate: mymeetsscreen");


        btnNewMeet = findViewById(R.id.btnNewMeet);
        lvMeets = findViewById(R.id.lvMeets);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);



        firebaseHelper = FirebaseHelper.getInstance(this);

        if (firebaseHelper.getUser().getMeetsList() != null){
            meetsAdapter = new MeetsAdapter(this,0,firebaseHelper.getUser().getMeetsList());
            lvMeets.setAdapter(meetsAdapter);
        }



        btnNewMeet.setOnClickListener(this);
    }

    /**
     * Handles click events for buttons.
     * - New Meet button starts the EditMeetScreen in creation mode
     * - Back button returns to the menu screen
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        if (view == btnNewMeet)
        {
            Intent i = new Intent(this, EditMeetScreen.class);
            i.putExtra("meet type", Meet.NEW_MEET);
            startActivity(i);
            finish();
        }
        else if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
            finish();
        }
    }
}