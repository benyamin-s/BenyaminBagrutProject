package com.example.benyaminbagrutproject.listviewadapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.benyaminbagrutproject.BasicActivity;
import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.screens.SearchActivitiesScreen;
import com.example.benyaminbagrutproject.screens.ViewMeetScreen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Custom ArrayAdapter for displaying activities in a searchable/filterable list.
 * Each activity item shows:
 * - Title
 * - Type
 * - Duration
 * - Creation date
 * 
 * Activities can be expanded to show additional information:
 * - Detailed explanation
 * - Required equipment
 * - Creator information
 * - Like/dislike functionality
 * 
 * Used in screens that display searchable activities like SearchActivitiesScreen.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class SearchedActivitiesListAdapter extends ArrayAdapter<BasicActivity> {
    /** List of activities to display */
    protected ArrayList<BasicActivity> activities;

    /** Context for inflating layouts and accessing resources */
    protected Context context;

    /** Helper class for Firebase operations */
    protected FirebaseHelper firebaseHelper;

    /**
     * Constructor for creating a new SearchedActivitiesListAdapter.
     * 
     * @param context The current context for inflating layouts
     * @param resource The resource ID for the layout file (not used)
     * @param objects List of activities to display
     */
    public SearchedActivitiesListAdapter(@NonNull Context context, int resource, @NonNull List<BasicActivity> objects) {
        super(context, resource, objects);
        activities = new ArrayList<>();
        for (BasicActivity basicActivity : objects) {
            activities.add(basicActivity);
        }
        this.context = context;
        firebaseHelper = FirebaseHelper.getInstance(context);
    }

    /**
     * Creates or recycles a view for an activity at the specified position.
     * Sets up the expandable activity view with:
     * - Basic information (title, type, duration, date)
     * - Expandable detailed information
     * - Like/dislike buttons if shown in search results
     * - Meet viewing button if part of a meet
     * 
     * @param position The position of the activity in the list
     * @param view The recycled view to populate, or null for a new view
     * @param parent The parent ViewGroup that will hold the view
     * @return The view for the activity at the specified position
     */
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
                if (visibility == View.GONE) {
                    loActivityInfo.setVisibility(View.VISIBLE);

                }
                else{
                    loActivityInfo.setVisibility(View.GONE);

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
            tvTime.setText(basicActivity.getTime().toString()+" minutes");

        Calendar calendar = Calendar.getInstance();

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



        ImageButton btnLike , btnDislike;
        TextView tvLikes;

        btnLike = view.findViewById(R.id.btnLike);
        btnDislike = view.findViewById(R.id.btnDislike);
        tvLikes = view.findViewById(R.id.tvLikes);


        Log.d("class_checker",  context.getClass().getName());


        if (context.getClass().getName().equals(ViewMeetScreen.class.getName()))
        {
            LinearLayout loVotingLayout = view.findViewById(R.id.votingLayout);
            loVotingLayout.setVisibility(View.GONE);
        }
        else {
            tvLikes.setText(basicActivity.getLikes()+"");

            Handler likesHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {

                    if (msg.arg1 == firebaseHelper.DONE_UPDATE_LIKES) {
                        tvLikes.setText(basicActivity.getLikes() + "");
                    }
                    return true;
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    firebaseHelper.UpdateLikes(basicActivity, "liked", likesHandler);

                }
            });

            btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseHelper.UpdateLikes(basicActivity, "disliked", likesHandler);

                }
            });

        }
        return view;
    }
}