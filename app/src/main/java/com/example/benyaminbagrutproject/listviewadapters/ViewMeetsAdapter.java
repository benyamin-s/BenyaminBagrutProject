package com.example.benyaminbagrutproject.listviewadapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.Meet;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.screens.ViewMeetScreen;

import java.util.ArrayList;
import java.util.List;

public class ViewMeetsAdapter extends ArrayAdapter<Meet> {

    protected ArrayList<Meet> meets;

    protected Context context;

    protected Calendar calendar;
    protected FirebaseHelper firebaseHelper;

    protected int selectedMeet;
    public ViewMeetsAdapter(@NonNull Context context, int resource, @NonNull List<Meet> objects) {
        super(context, resource, objects);
        meets = new ArrayList<>();
        this.context = context;
        for (Meet meet : objects) {
            this.meets.add(meet);
        }

        calendar = Calendar.getInstance();
        firebaseHelper= FirebaseHelper.getInstance(context);
        selectedMeet = -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.meet_layout,parent,false);


        Button btnViewMeet = view.findViewById(R.id.btnEdit);


        btnViewMeet.setText("view meet");

        Meet meet = meets.get(position);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMeet = position;
            }
        });

        btnViewMeet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), ViewMeetScreen.class);
                        i.putExtra("creatorID",firebaseHelper.getUserId());
                        i.putExtra("MeetID",meet.getMeetID());
                        context.startActivity(i);
                    }
                }
        );

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDate = view.findViewById(R.id.tvDate);

        tvTitle.setText(meet.getName());

        if (meet.getDate() != null) {
            calendar.setTimeInMillis(meet.getDate());
            tvDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        }
        return view;
    }

    public int getSelectedMeet() {
        return selectedMeet;
    }
}
