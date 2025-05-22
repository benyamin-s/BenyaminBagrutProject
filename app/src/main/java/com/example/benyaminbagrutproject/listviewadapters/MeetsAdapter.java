package com.example.benyaminbagrutproject.listviewadapters;

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

import com.example.benyaminbagrutproject.Meet;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.screens.EditMeetScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom ArrayAdapter for displaying meetings in a ListView.
 * Each meeting item shows:
 * - Meeting title
 * - Meeting date
 * - Edit button to modify meeting details
 * 
 * This adapter is used in screens that display lists of meetings,
 * such as MyMeetsScreen.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class MeetsAdapter extends ArrayAdapter<Meet> {

    /** List of meetings to display */
    protected ArrayList<Meet> meets;

    /** Context for inflating layouts and accessing resources */
    protected Context context;

    /** Calendar instance for date formatting */
    protected Calendar calendar;

    /**
     * Constructor for creating a new MeetsAdapter.
     * 
     * @param context The current context for inflating layouts
     * @param resource The resource ID for the layout file (not used)
     * @param Meets List of meetings to display
     */
    public MeetsAdapter(@NonNull Context context, int resource, @NonNull List<Meet> Meets) {
        super(context, resource, Meets);
        meets = new ArrayList<>();
        this.context = context;
        for (Meet meet : Meets) {
                 this.meets.add(meet);
        }

        calendar = Calendar.getInstance();
    }

    /**
     * Creates or recycles a view for a meeting at the specified position.
     * Sets up the meeting title, date display, and edit button functionality.
     * The edit button launches EditMeetScreen in edit mode for the selected meeting.
     * 
     * @param position The position of the meeting in the list
     * @param convertView The recycled view to populate, or null for a new view
     * @param parent The parent ViewGroup that will hold the view
     * @return The view for the meeting at the specified position
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.meet_layout,parent,false);

        Button btnEdit = view.findViewById(R.id.btnEdit);


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
            tvDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        }
        return view;
    }
}
