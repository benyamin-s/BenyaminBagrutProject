package com.example.benyaminbagrutproject.listviewadapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

import com.example.benyaminbagrutproject.ActivityAnswer;
import com.example.benyaminbagrutproject.Answer;
import com.example.benyaminbagrutproject.BasicActivity;
import com.example.benyaminbagrutproject.FirebaseHelper;
import com.example.benyaminbagrutproject.MeetAnswer;
import com.example.benyaminbagrutproject.R;
import com.example.benyaminbagrutproject.TextAnswer;
import com.example.benyaminbagrutproject.screens.ViewMeetScreen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Custom ArrayAdapter for displaying different types of answers in a ListView.
 * Handles three types of answers:
 * - Text answers: Simple text responses
 * - Activity answers: Detailed activity suggestions with expandable information
 * - Meet answers: Meeting suggestions with view option
 * 
 * Each answer type has its own layout and display format, while maintaining
 * common elements like creation date and creator information.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class AnswersAdapter extends ArrayAdapter<Answer> {
    /** List of answers to display */
    protected ArrayList<Answer> answers;

    /** Context for inflating layouts and accessing resources */
    protected Context context;

    /**
     * Constructor for creating a new AnswersAdapter.
     * 
     * @param context The current context for inflating layouts
     * @param resource The resource ID for the layout file (not used)
     * @param answers List of answers to display
     */
    public AnswersAdapter(@NonNull Context context, int resource, @NonNull List<Answer> answers) {
        super(context, resource, answers);
        this.answers = new ArrayList<>();
        for (Answer a:answers) {
            this.answers.add(a);
        }
        this.context = context;
    }

    /**
     * Creates or recycles a view for an answer at the specified position.
     * Handles different answer types (text, activity, meet) by inflating appropriate layouts.
     * 
     * For each answer type:
     * - Text answers: Displays simple text content
     * - Activity answers: Shows activity details with expandable information
     * - Meet answers: Provides a view option to see full meeting details
     * 
     * Common elements displayed for all types:
     * - Creation date
     * - Creator name
     * 
     * @param position The position of the answer in the list
     * @param view The recycled view to populate, or null for a new view
     * @param parent The parent ViewGroup that will hold the view
     * @return The configured view for the answer at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
        FirebaseHelper firebaseHelper = FirebaseHelper.getInstance(context);

        Answer answer = answers.get(position);

        switch (answer.getType())
        {
            case Answer.TYPE_TEXT:
                view = layoutInflater.inflate(R.layout.text_answer, parent,false);

                TextView tvContent = view.findViewById(R.id.tvContent);
                TextAnswer textAnswer = (TextAnswer)answer;
                tvContent.setText(textAnswer.getContent());

                break;

            case Answer.TYPE_ACTIVITY:
                view = layoutInflater.inflate(R.layout.activity_answer, parent,false);

                LinearLayout loActivity = view.findViewById(R.id.loActivity);
                LayoutInflater activityInflater = ((Activity)view.getContext()).getLayoutInflater();
                View addedView = activityInflater.inflate(R.layout.searchedactivity_layout, loActivity, true);


                LinearLayout loActivityInfo = addedView.findViewById(R.id.loActivityInfo);

                loActivityInfo.setVisibility(View.GONE);
                addedView.setOnClickListener(new View.OnClickListener() {
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

                TextView tvTitle,tvType,tvTime ;
                tvTitle = addedView.findViewById(R.id.tvTitle);
                tvType = addedView.findViewById(R.id.tvType);
                tvTime = addedView.findViewById(R.id.tvTime);

                ActivityAnswer activityAnswer = (ActivityAnswer) answer;
                BasicActivity basicActivity = activityAnswer.getBasicActivity();
                if (basicActivity.getTitle() != null)
                    tvTitle.setText(basicActivity.getTitle());
                if (basicActivity.getType() != null)
                    tvType.setText(basicActivity.getType());
                if (basicActivity.getTime() != null)
                    tvTime.setText(basicActivity.getTime().toString() + " minutes");

                Calendar calendar = Calendar.getInstance();


                TextView tvCreator,tvExplanation,tvEquipment  ,tvCreatorID ;
                Button btnViewMeet ;

                tvExplanation = addedView.findViewById(R.id.tvExplanation);
                tvEquipment = addedView.findViewById(R.id.tvEquipment);
                tvCreator  = addedView.findViewById(R.id.tvCreator);
                btnViewMeet = addedView.findViewById(R.id.btnViewMeet);
                tvCreatorID = addedView.findViewById(R.id.tvCreatorID);


                tvCreator.setText("creator: " + basicActivity.getCreator());
                tvCreatorID.setText(basicActivity.getCreatorID());
                tvEquipment.setText(basicActivity.getEquipment());
                if (basicActivity.getExplanation() != null)
                    tvExplanation.setText( basicActivity.getExplanation());

                btnViewMeet.setVisibility(View.GONE);


                LinearLayout loVotingLayout = addedView.findViewById(R.id.votingLayout);
                loVotingLayout.setVisibility(View.GONE);


                break;


            case Answer.TYPE_MEET:
                view = layoutInflater.inflate(R.layout.meet_answer, parent,false);
                MeetAnswer meetAnswer  = (MeetAnswer)answer;
                btnViewMeet = view.findViewById(R.id.btnViewMeet);
                TextView tvEplanation = view.findViewById(R.id.tvExplanation);

                tvEplanation.setText(meetAnswer.getExplanation());
                btnViewMeet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context , ViewMeetScreen.class);
                        i.putExtra("creatorID",meetAnswer.getCreatorID());
                        i.putExtra("MeetID",meetAnswer.getMeet().getMeetID());
                        context.startActivity(i);
                    }
                });
                break;
        }

        TextView tvDate , tvCreator;
        tvDate = view.findViewById(R.id.tvDate);
        tvCreator = view.findViewById(R.id.tvCreator);

        Calendar calendar = Calendar.getInstance();

        if (answer.getDate() != null) {
            calendar.setTimeInMillis(answer.getDate());
            tvDate.setText(calendar.get(android.icu.util.Calendar.DAY_OF_MONTH) + "/" + (calendar.get(android.icu.util.Calendar.MONTH) + 1 )+ "/" + calendar.get(android.icu.util.Calendar.YEAR));
        }

        tvCreator.setText(answer.getCreator());

        return view;
    }
}
