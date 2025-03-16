package com.example.benyaminbagrutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivitiesScreen extends AppCompatActivity implements View.OnClickListener {

    protected Button btnBack;

    protected ListView lvActivities;
    protected ArrayList<BasicActivity> activitiesArrayList;

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

        activitiesArrayList = new ArrayList<>();

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.arg1 == FirebaseHelper.DONE_RETRIEVE_USER_DATA)
                {
                    for (BasicActivity b:   (ArrayList<BasicActivity>) msg.obj) {
                        activitiesArrayList.add(b);
                    }
                    searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(SearchActivitiesScreen.this,0,activitiesArrayList);
                    lvActivities.setAdapter(searchedActivitiesListAdapter);
                }

                return true;
            }
        });

        firebaseHelper.retrieveActivitiesList(handler);


    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
        }
    }
}