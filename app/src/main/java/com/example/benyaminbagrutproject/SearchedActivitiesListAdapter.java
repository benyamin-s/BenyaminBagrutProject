package com.example.benyaminbagrutproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchedActivitiesListAdapter extends ArrayAdapter<BasicActivity> {
    protected ArrayList<BasicActivity> activities;

    protected Context context;


    public SearchedActivitiesListAdapter(@NonNull Context context, int resource, @NonNull List<BasicActivity> objects) {
        super(context, resource, objects);
        activities = new ArrayList<>();
        for (BasicActivity basicActivity : objects) {
            activities.add(basicActivity);
        }
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.searchedactivity_layout, parent,false);
        }
        LinearLayout loActivityInfo = view.findViewById(R.id.loActivityInfo);

        loActivityInfo.setVisibility(View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = loActivityInfo.getVisibility();
                if (visibility == view.GONE) {
                    loActivityInfo.setVisibility(view.VISIBLE);

                }
                else{
                    loActivityInfo.setVisibility(view.GONE);

                }
            }
        });


        TextView tvDate,tvTitle,tvType,tvTime ;
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvType = view.findViewById(R.id.tvType);
        tvTime = view.findViewById(R.id.tvTime);

        BasicActivity basicActivity = activities.get(position);
        if (basicActivity.getTitle() != null)
            tvTitle.setText(basicActivity.getTitle());
        if (basicActivity.getType() != null)
            tvType.setText(basicActivity.getType());
        if (basicActivity.getTime() != null)
            tvTime.setText(basicActivity.getTime().toString());

        Calendar calendar = Calendar.getInstance();

        if (basicActivity.getDate() != null) {
            calendar.setTimeInMillis(basicActivity.getDate());
            tvDate.setText(calendar.DAY_OF_MONTH + "/" + calendar.MONTH + 1 + "/" + calendar.YEAR);
        }

        TextView tvCreator,tvExplanation,tvEquipment  ,tvCreatorID;
        Button btnViewMeet ;

        tvExplanation = view.findViewById(R.id.tvExplanation);
        tvEquipment = view.findViewById(R.id.tvEquipment);
        tvCreator  = view.findViewById(R.id.tvCreator);
        btnViewMeet = view.findViewById(R.id.btnViewMeet);
        tvCreatorID = view.findViewById(R.id.tvCreatorID);


        tvCreator.setText("creator: " + basicActivity.getCreator());
        tvCreatorID.setText(basicActivity.getCreatorID());
        tvEquipment.setText("equipment \n" + basicActivity.getEquipment());
        if (basicActivity.getExplanation() != null)
            tvExplanation.setText( basicActivity.getExplanation());

        btnViewMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context , ViewMeetScreen.class);
                i.putExtra("creatorID",basicActivity.getCreatorID());
                i.putExtra("MeetID",basicActivity.getMeetID());
                context.startActivity(i);

            }
        });

        return view;
    }
}