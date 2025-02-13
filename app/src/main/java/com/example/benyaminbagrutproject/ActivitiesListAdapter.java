package com.example.benyaminbagrutproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivitiesListAdapter extends BaseExpandableListAdapter {
    protected ArrayList<BasicActivity> activities;

    protected Context context;

    public ActivitiesListAdapter(Context context,ArrayList<BasicActivity> activities)
    {
        this.activities = activities;
        this.context = context;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.title_basicactivity_layout, viewGroup,false);
        }
        TextView tvDate,tvTitle,tvType,tvTime;
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvType = view.findViewById(R.id.tvType);
        tvTime = view.findViewById(R.id.tvTime);

        BasicActivity basicActivity = activities.get(i);
        if (basicActivity.getTitle() != null)
            tvTitle.setText(basicActivity.getTitle());
        if (basicActivity.getType() != null)
            tvType.setText("type: "+ basicActivity.getType());
        if (basicActivity.getTime() != null)
            tvTime.setText("Time: " +  basicActivity.getTime().toString() + " minutes");

        Calendar calendar = Calendar.getInstance();

        if (basicActivity.getDate() != null) {
            calendar.setTimeInMillis(basicActivity.getDate());
            tvDate.setText(calendar.DAY_OF_MONTH + "/" + calendar.MONTH + 1 + "/" + calendar.YEAR);
        }


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.info_basicactivity_layout, viewGroup,false);
        }

        TextView tvCreator,tvExplanation,tvEquipment;
        Button btnDelete ;

        tvExplanation = view.findViewById(R.id.tvExplanation);
        tvEquipment = view.findViewById(R.id.tvEquipment);
        tvCreator  = view.findViewById(R.id.tvCreator);
        btnDelete = view.findViewById(R.id.btnDelete);

        BasicActivity basicActivity = activities.get(i);

        tvCreator.setText("creator: " + basicActivity.getCreator());
        tvEquipment.setText("equipment \n" + basicActivity.getEquipment());
        tvExplanation.setText(basicActivity.getExplanation());
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }






    @Override
    public int getGroupCount() {
        return activities.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return activities.size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
