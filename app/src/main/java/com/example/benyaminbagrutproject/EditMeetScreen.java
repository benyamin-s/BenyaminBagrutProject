package com.example.benyaminbagrutproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class EditMeetScreen extends AppCompatActivity {

    protected ExpandableListView elvActivitiesList;
    protected Meet meet;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_edit_screen);
    }
}