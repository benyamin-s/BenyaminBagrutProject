package com.example.benyaminbagrutproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RequestsAdapter extends ArrayAdapter<Request> {
    protected ArrayList<Request> requests;
    protected Context context;

    public RequestsAdapter(@NonNull Context context, int resource, @NonNull List<Request> Requests) {
        super(context, resource, Requests);
        this.requests = new ArrayList<>();

        for (Request r:Requests) {
            this.requests.add(r);
        }
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.request_layout, parent,false);
        }

        TextView tvDate , tvRequester , tvRequestTitle ;
        tvDate = view.findViewById(R.id.tvDate);
        tvRequester = view.findViewById(R.id.tvRequester);
        tvRequestTitle = view.findViewById(R.id.tvRequest);

        Request request = requests.get(position);

        Calendar calendar = Calendar.getInstance();

        if (request.getDate() != null) {
            calendar.setTimeInMillis(request.getDate());
            tvDate.setText(calendar.get(android.icu.util.Calendar.DAY_OF_MONTH) + "/" + (calendar.get(android.icu.util.Calendar.MONTH) + 1) + "/" + calendar.get(android.icu.util.Calendar.YEAR));
        }

        tvRequester.setText(request.getRequesterName());
        tvRequestTitle.setText(request.getRequestTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , AnswersScreen.class);
                i.putExtra("Index" , request.getIndex());
                context.startActivity(i);
            }
        });


        return view;
    }
}
