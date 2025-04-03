package com.example.benyaminbagrutproject;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MeetsAdapter extends ArrayAdapter<Meet> {

    protected ArrayList<Meet> meets;

    protected Context context;

    protected Calendar calendar;
    public MeetsAdapter(@NonNull Context context, int resource, @NonNull List<Meet> Meets) {
        super(context, resource, Meets);
        meets = new ArrayList<>();
        this.context = context;
        for (Meet meet : Meets) {
                 this.meets.add(meet);
        }

        calendar = Calendar.getInstance();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.meet_layout,parent,false);

        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("delete button clicked", "onClick: " + position);
            }
        });

        btnEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("edit button clicked", "onClick: " + position);

                        Intent i = new Intent(context, EditMeetScreen.class);
                        i.putExtra("meet position",position);
                        i.putExtra("meet type",Meet.EDIT_MEET);
                        context.startActivity(i);
                    }
                }
        );

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDate = view.findViewById(R.id.tvDate);

        Meet meet = meets.get(position);
        tvTitle.setText(meet.getName());

        if (meet.getDate() != null) {
            calendar.setTimeInMillis(meet.getDate());
            tvDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.YEAR));
        }
        return view;
    }
}
