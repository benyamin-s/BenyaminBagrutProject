package com.example.benyaminbagrutproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnswersAdapter extends ArrayAdapter<Answer> {
    protected ArrayList<Answer> answers;

    protected Context context;



    public AnswersAdapter(@NonNull Context context, int resource, @NonNull List<Answer> answers) {
        super(context, resource, answers);
        this.answers = new ArrayList<>();
        for (Answer a:answers) {
            this.answers.add(a);
        }
        this.context = context;
    }

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

                TextView tvDate,tvTitle,tvType,tvTime ;
                tvTitle = view.findViewById(R.id.tvTitle);
                tvDate = view.findViewById(R.id.tvDate);
                tvType = view.findViewById(R.id.tvType);
                tvTime = view.findViewById(R.id.tvTime);

                ActivityAnswer activityAnswer = (ActivityAnswer) answer;
                BasicActivity basicActivity = activityAnswer.getBasicActivity();
                if (basicActivity.getTitle() != null)
                    tvTitle.setText(basicActivity.getTitle());
                if (basicActivity.getType() != null)
                    tvType.setText(basicActivity.getType());
                if (basicActivity.getTime() != null)
                    tvTime.setText(basicActivity.getTime().toString());

                Calendar calendar = Calendar.getInstance();

                if (basicActivity.getDate() != null) {
                    calendar.setTimeInMillis(basicActivity.getDate());
                    tvDate.setText(calendar.get(android.icu.util.Calendar.DAY_OF_MONTH) + "/" +( calendar.get(android.icu.util.Calendar.MONTH) + 1) + "/" + calendar.get(android.icu.util.Calendar.YEAR));
                }

                TextView tvCreator,tvExplanation,tvEquipment  ,tvCreatorID;
                Button btnViewMeet ;

                tvExplanation = addedView.findViewById(R.id.tvExplanation);
                tvEquipment = addedView.findViewById(R.id.tvEquipment);
                tvCreator  = addedView.findViewById(R.id.tvCreator);
                btnViewMeet = addedView.findViewById(R.id.btnViewMeet);
                tvCreatorID = addedView.findViewById(R.id.tvCreatorID);


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

                btnLike = addedView.findViewById(R.id.btnLike);
                btnDislike = addedView.findViewById(R.id.btnDislike);
                tvLikes = addedView.findViewById(R.id.tvLikes);


                //TODO figure out how to likes
                btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        firebaseHelper.UpdateLikes(basicActivity , "liked");

                        tvLikes.setText(basicActivity.getLiked().size() - basicActivity.getDisliked().size());
                    }
                });

                btnDislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseHelper.UpdateLikes(basicActivity , "liked");
                        tvLikes.setText(basicActivity.getLiked().size() - basicActivity.getDisliked().size());
                    }
                });
                //

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
