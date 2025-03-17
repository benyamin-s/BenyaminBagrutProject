package com.example.benyaminbagrutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SearchActivitiesScreen extends AppCompatActivity implements View.OnClickListener {

    protected Button btnBack , btnSearchLikes , btnSearchID;

    protected Spinner spinFilterType;

    protected ListView lvActivities;
    protected ArrayList<BasicActivity> activitiesArrayList;

    protected SearchedActivitiesListAdapter searchedActivitiesListAdapter;

    protected FirebaseHelper firebaseHelper;

    protected ConstraintLayout constraintLayout;

    protected String typeFilter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_search_screen);

        constraintLayout = findViewById(R.id.conlayoutFilters);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = constraintLayout.getVisibility();
                if (visibility == view.GONE) {
                    constraintLayout.setVisibility(view.VISIBLE);

                }
                else{
                    constraintLayout.setVisibility(view.GONE);

                }
            }
        });

        typeFilter = "";
        firebaseHelper = FirebaseHelper.getInstance(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnSearchID = findViewById(R.id.btnFilterID);
        btnSearchLikes = findViewById(R.id.btnFilterLikes);

        spinFilterType = findViewById(R.id.spinFilterType);
        String[] types = new String[BasicActivity.types.length + 1];
        types[0] = "הכל";
        for (int i = 1;i < BasicActivity.types.length + 1;i++)
        {
            types[i] = BasicActivity.types[i-1];
        }

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFilterType.setAdapter(ad);
        spinFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i   == 0)
                {
                    typeFilter = "";
                }
                else
                {
                    typeFilter = BasicActivity.types[i - 1];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSearchLikes.setOnClickListener(this);
        btnSearchID.setOnClickListener(this);

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
        else if (view == btnSearchID)
        {

        } else if (view == btnSearchLikes) {

        }
    }
}