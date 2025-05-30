package com.example.benyaminbagrutproject.screens;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.benyaminbagrutproject.BasicActivity;
import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.listviewadapters.SearchedActivitiesListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Activity for searching and filtering activities in the  application.
 * This screen allows guides to:
 * - Search for activities by text
 * - Filter activities by type
 * - Filter activities by creator ID
 * - Sort activities by likes or creation time
 * - View detailed activity information
 * 
 * The screen provides various filtering and sorting options through a collapsible
 * filter panel.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class SearchActivitiesScreen extends AppCompatActivity implements View.OnClickListener {

    /** Button to return to previous screen */
    protected Button btnBack;
    
    /** Button to sort activities by likes */
    protected Button btnSortLikes;
    
    /** Button to sort activities by time */
    protected Button btnSortTime;
    
    /** Button to apply selected sort/filter options */
    protected Button btnActivateSort;
    
    /** Button to reset all filters */
    protected Button btnReset;

    /** Spinner for selecting activity type filter */
    protected Spinner spinFilterType;

    /** ListView displaying filtered activities */
    protected ListView lvActivities;

    /** EditText for filtering by creator ID */
    protected EditText etFilterID;
    
    /** EditText for text search */
    protected EditText etSearchBar;
    
    /** Original list of all activities */
    protected ArrayList<BasicActivity> baseArrayList;
    
    /** Filtered list of activities */
    protected ArrayList<BasicActivity> filteredArrayList;

    /** Adapter for displaying activities in the ListView */
    protected SearchedActivitiesListAdapter searchedActivitiesListAdapter;

    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /** Layout containing filter options */
    protected ConstraintLayout constraintLayout;

    /** Currently selected activity type filter */
    protected String typeFilter;

    /** Button to show/hide filter options */
    protected Button btnFilters;

    /**
     * Initializes the activity, sets up UI components and loads activities.
     * Sets up filter panel visibility controls and populates type filter spinner.
     * 
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_search_screen);

        constraintLayout = findViewById(R.id.conlayoutFilters);

        btnFilters = findViewById(R.id.btnFilters);

        btnFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayout.setVisibility(View.VISIBLE);
                btnFilters.setVisibility(View.GONE);
            }
        });
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayout.setVisibility(View.GONE);
                btnFilters.setVisibility(View.VISIBLE);
            }
        });
        constraintLayout.setVisibility(View.GONE);

        typeFilter = "";
        firebaseHelper = FirebaseHelper.getInstance(this);

        etSearchBar = findViewById(R.id.etSearchBar);



        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnSortTime = findViewById(R.id.btnFilterTime);
        btnSortLikes = findViewById(R.id.btnFilterLikes);
        btnActivateSort = findViewById(R.id.btnActivateSort);
        btnReset = findViewById(R.id.btnReset);

        etFilterID = findViewById(R.id.etFilterID);

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

        btnSortLikes.setOnClickListener(this);
        btnSortTime.setOnClickListener(this);
        btnActivateSort.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        lvActivities= findViewById(R.id.lvActivities);

        baseArrayList = new ArrayList<>();
        filteredArrayList = new ArrayList<>();

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.arg1 == FirebaseHelper.DONE_RETRIEVE_USER_DATA)
                {

                    for (BasicActivity b:   (ArrayList<BasicActivity>) msg.obj) {
                        baseArrayList.add(b);
                        filteredArrayList.add(b);
                    }
                    searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(SearchActivitiesScreen.this,0,baseArrayList);
                    lvActivities.setAdapter(searchedActivitiesListAdapter);

                }

                return true;
            }
        });

        firebaseHelper.retrieveActivitiesList(handler);
    }

    /**
     * Handles click events for all interactive UI elements.
     * - Back button returns to menu screen
     * - Sort Likes button sorts activities by number of likes
     * - Sort Time button sorts activities by duration
     * - Activate Sort button applies all current filter settings
     * - Reset button clears all filters and shows all activities
     * 
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            startActivity(new Intent(this,MenuScreen.class));
        }
        else if(view == btnSortLikes)
        {
            Collections.sort(filteredArrayList, new Comparator<BasicActivity>() {
                @Override
                public int compare(BasicActivity activity1, BasicActivity activity2) {
                    return Integer.compare(activity1.getLikes(), activity2.getLikes());
                }
            });

            searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(SearchActivitiesScreen.this,0,filteredArrayList);
            lvActivities.setAdapter(searchedActivitiesListAdapter);
        }
        else if (view == btnSortTime) {
            Collections.sort(filteredArrayList, new Comparator<BasicActivity>() {
                @Override
                public int compare(BasicActivity activity1, BasicActivity activity2) {
                    return Long.compare(activity1.getTime(), activity2.getTime());
                }
            });

            searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(SearchActivitiesScreen.this,0,filteredArrayList);
            lvActivities.setAdapter(searchedActivitiesListAdapter);
        }
        else if (view == btnActivateSort)
        {
            filteredArrayList = new ArrayList<>();



            //filter by type
            for (BasicActivity b:baseArrayList) {
                if (typeFilter.equals("") || b.getType().equals(typeFilter))
                {
                    filteredArrayList.add(b);
                }
            }

            ArrayList<BasicActivity> temp = new ArrayList<>();
            //filter by ID
            for (BasicActivity b:filteredArrayList) {
                if (etFilterID.getText().toString().equals("")|| b.getCreatorID().equals(etFilterID.getText().toString()) )
                {
                    temp.add(b);
                }
            }
            filteredArrayList = temp;

            //filter by searchbar

            if (!etSearchBar.getText().toString().equals(""))
                filteredArrayList = filterByTitle(etSearchBar.getText().toString(),filteredArrayList);

            //
            searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(SearchActivitiesScreen.this,0,filteredArrayList);
            lvActivities.setAdapter(searchedActivitiesListAdapter);
        }
        else if (view == btnReset) {
            etFilterID.setText("");
            etSearchBar.setText("");
            filteredArrayList = new ArrayList<>();
            for (BasicActivity b:baseArrayList) {
                filteredArrayList.add(b);
            }

            searchedActivitiesListAdapter = new SearchedActivitiesListAdapter(SearchActivitiesScreen.this,0,filteredArrayList);
            lvActivities.setAdapter(searchedActivitiesListAdapter);
        }

    }

    /**
     * Filters a list of activities by matching the activity title with search text.
     * 
     * @param text The search text to filter by
     * @param activities The list of activities to filter
     * @return A new ArrayList containing only activities whose titles contain the search text
     */
    public ArrayList<BasicActivity> filterByTitle(String text , ArrayList<BasicActivity> activities)
    {
        ArrayList<BasicActivity> temp = new ArrayList<>();

        for (BasicActivity basicActivity:activities) {
            if (basicActivity.getTitle().contains(text))
            {
                temp.add(basicActivity);
            }
        }

        return temp;
    }


}

