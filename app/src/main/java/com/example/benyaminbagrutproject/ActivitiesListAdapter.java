package com.example.benyaminbagrutproject;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivitiesListAdapter extends ArrayAdapter<BasicActivity> {
    protected ArrayList<BasicActivity> activities;

    protected Context context;



    public ActivitiesListAdapter(@NonNull Context context, int resource, @NonNull List<BasicActivity> objects) {
        super(context, resource, objects);
        activities = new ArrayList<>();
        for (BasicActivity basicActivity:objects)
        {
            activities.add(basicActivity);
        }
        this.context = context;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.basicactivity_layout, parent,false);
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


        EditText etDate,etTitle,etType,etTime;
        etTitle = view.findViewById(R.id.etTitle);
        etDate = view.findViewById(R.id.etDate);
        etType = view.findViewById(R.id.etType);
        etTime = view.findViewById(R.id.etTime);

        BasicActivity basicActivity = activities.get(position);
        if (basicActivity.getTitle() != null)
            etTitle.setText(basicActivity.getTitle());
        if (basicActivity.getType() != null)
            etType.setText("type: "+ basicActivity.getType());
        if (basicActivity.getTime() != null)
            etTime.setText(basicActivity.getTime().toString());

        Calendar calendar = Calendar.getInstance();

        if (basicActivity.getDate() != null) {
            calendar.setTimeInMillis(basicActivity.getDate());
            etDate.setText(calendar.DAY_OF_MONTH + "/" + calendar.MONTH + 1 + "/" + calendar.YEAR);
        }

        etTitle.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                basicActivity.setTitle(editable.toString());
            }
        });

        etTime.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.toString() != "")
                    basicActivity.setTime(Long.parseLong(editable.toString()));
                else
                    basicActivity.setTime(Long.parseLong("0"));
            }
        });

        etType.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                basicActivity.setType(editable.toString());
            }
        });

        //TODO date - check if works
        etDate.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                try {
                    // Define the date string
                    String dateString = editable.toString();

                    // Define the format of the input string
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                    // Parse the date string into a Date object
                    Date date = formatter.parse(dateString);

                    // Convert the Date object to a long (milliseconds)
                    long timeInMillis = date.getTime();

                    basicActivity.setDate(timeInMillis);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        EditText etCreator,etExplanation,etEquipment;
        Button btnDelete ;

        etExplanation = view.findViewById(R.id.etExplanation);
        etEquipment = view.findViewById(R.id.etEquipment);
        etCreator  = view.findViewById(R.id.etCreator);
        btnDelete = view.findViewById(R.id.btnDelete);



        etCreator.setText("creator: " + basicActivity.getCreator());
        etEquipment.setText("equipment \n" + basicActivity.getEquipment());
        if (basicActivity.getExplanation() != null)
            etExplanation.setText( basicActivity.getExplanation());

        etEquipment.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                basicActivity.setEquipment(editable.toString());
            }
        });

        etExplanation.addTextChangedListener(new TextWatcher() {@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                basicActivity.setExplanation(editable.toString());
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

}
