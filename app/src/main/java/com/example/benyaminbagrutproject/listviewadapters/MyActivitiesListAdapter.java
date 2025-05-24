package com.example.benyaminbagrutproject.listviewadapters;

import static org.xmlpull.v1.XmlPullParser.TYPES;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.benyaminbagrutproject.BasicActivity;
import com.example.benyaminbagrutproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Custom ArrayAdapter for editing activities in the youth movement guide application.
 * This adapter provides an editable view for BasicActivity items, allowing users to:
 * - Edit activity titles and durations
 * - Select activity types from a predefined list
 * - Add/edit activity explanations and required equipment
 * - Toggle visibility of detailed activity information
 *
 * Each activity in the list is displayed with an expandable view that shows
 * all editable fields when clicked.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class MyActivitiesListAdapter extends ArrayAdapter<BasicActivity> {
    /** List of activities being edited */
    protected ArrayList<BasicActivity> activities;

    /** Context for inflating layouts and accessing resources */
    protected Context context;

    /**
     * Constructor for creating a new MyActivitiesListAdapter.
     * Creates a copy of the provided list of activities to manage edits.
     * 
     * @param context The current context for inflating layouts
     * @param resource The resource ID for the layout file (not used)
     * @param objects List of BasicActivity objects to display and edit
     */
    public MyActivitiesListAdapter(@NonNull Context context, int resource, @NonNull List<BasicActivity> objects) {
        super(context, resource, objects);
        activities = new ArrayList<>();
        for (BasicActivity basicActivity:objects)
        {
            activities.add(basicActivity);
        }
        this.context = context;

    }

    /**
     * Creates or recycles a view for an activity at the specified position.
     * Sets up all edit fields and their listeners for real-time updates to the activity data.
     * 
     * The view includes:
     * - Title EditText
     * - Duration EditText
     * - Type Spinner with predefined options
     * - Explanation EditText
     * - Equipment EditText
     * 
     * All fields are connected to their respective activity properties through TextWatchers
     * or item selection listeners to ensure immediate data updates.
     * 
     * @param position The position of the activity in the list
     * @param view The recycled view to populate, or null for a new view
     * @param parent The parent ViewGroup that will hold the view
     * @return The configured view for editing the activity at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.mybasicactivity_layout, parent,false);
        }
        LinearLayout loActivityInfo = view.findViewById(R.id.loActivityInfo);

        loActivityInfo.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = loActivityInfo.getVisibility();
                if (visibility == view.VISIBLE) {
                    loActivityInfo.setVisibility(view.GONE);

                }
                else{
                    loActivityInfo.setVisibility(view.VISIBLE);

                }
            }
        });


        EditText etTitle,etTime;
        etTitle = view.findViewById(R.id.etTitle);
        etTime = view.findViewById(R.id.etTime);

        BasicActivity basicActivity = activities.get(position);
        if (basicActivity.getTitle() != null)
            etTitle.setText(basicActivity.getTitle());

        if (basicActivity.getTime() != null)
            etTime.setText(basicActivity.getTime().toString());



        etTitle.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                basicActivity.setTitle(editable.toString());
            }
        });

        etTime.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                if (!editable.toString().equals("") )
                    basicActivity.setTime(Long.parseLong(editable.toString()));
                else
                    basicActivity.setTime(Long.parseLong("0"));
            }
        });




        Spinner spinType = view.findViewById(R.id.spinType);
        ArrayAdapter<String> ad = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, BasicActivity.types);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(ad);
        if (basicActivity.getType() != null)
            for (int i = 0;i < BasicActivity.types.length;i++) {
                if (basicActivity.getType().equals(BasicActivity.types[i]))
                    spinType.setSelection(i);

            }

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                basicActivity.setType(BasicActivity.types[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText etExplanation,etEquipment;


        etExplanation = view.findViewById(R.id.etExplanation);
        etEquipment = view.findViewById(R.id.etEquipment);




        etEquipment.setText(basicActivity.getEquipment());
        if (basicActivity.getExplanation() != null)
            etExplanation.setText( basicActivity.getExplanation());

        etEquipment.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                basicActivity.setEquipment(editable.toString());
            }
        });

        etExplanation.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                basicActivity.setExplanation(editable.toString());
            }
        });





        return view;
    }

}
