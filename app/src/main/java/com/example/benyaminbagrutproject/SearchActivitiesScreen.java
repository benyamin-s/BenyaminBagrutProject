package com.example.benyaminbagrutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SearchActivitiesScreen extends AppCompatActivity implements View.OnClickListener {

    protected Button btnBack;

    protected ListView lvActivities;

    protected SearchedActivitiesListAdapter searchedActivitiesListAdapter;

    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_search_screen);

        firebaseHelper = FirebaseHelper.getInstance(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        lvActivities= findViewById(R.id.lvActivities);

        searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(this,0,/*TODO */   firebaseHelper.getUser().getMeetsList().get(0).getActivities());
        lvActivities.setAdapter(searchedActivitiesListAdapter);

    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
        }
    }
}