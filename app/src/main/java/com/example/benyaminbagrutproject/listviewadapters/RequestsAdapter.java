package com.example.benyaminbagrutproject.listviewadapters;

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

import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.Request;
import com.example.benyaminbagrutproject.screens.AnswersScreen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Custom ArrayAdapter for displaying requests in a ListView.
 * Each request item shows:
 * - Request date
 * - Requester name
 * - Request title
 * 
 * Clicking on a request item opens the AnswersScreen to view
 * and manage answers for that request.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class RequestsAdapter extends ArrayAdapter<Request> {
    /** List of requests to display */
    protected ArrayList<Request> requests;
    
    /** Context for inflating layouts and accessing resources */
    protected Context context;

    /**
     * Constructor for creating a new RequestsAdapter.
     * 
     * @param context The current context for inflating layouts
     * @param resource The resource ID for the layout file (not used)
     * @param Requests List of requests to display
     */
    public RequestsAdapter(@NonNull Context context, int resource, @NonNull List<Request> Requests) {
        super(context, resource, Requests);
        this.requests = new ArrayList<>();

        for (Request r:Requests) {
            this.requests.add(r);
        }
        this.context = context;

    }

    /**
     * Creates or recycles a view for a request at the specified position.
     * Sets up the request date, requester name, title display, and click handling
     * to open the AnswersScreen for the selected request.
     * 
     * @param position The position of the request in the list
     * @param view The recycled view to populate, or null for a new view
     * @param parent The parent ViewGroup that will hold the view
     * @return The view for the request at the specified position
     */
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
