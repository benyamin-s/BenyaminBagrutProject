package com.example.benyaminbagrutproject;

import static org.xmlpull.v1.XmlPullParser.TYPES;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyActivitiesListAdapter extends ArrayAdapter<BasicActivity> {
    protected ArrayList<BasicActivity> activities;

    protected Context context;



    public MyActivitiesListAdapter(@NonNull Context context, int resource, @NonNull List<BasicActivity> objects) {
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
            view = layoutInflater.inflate(R.layout.mybasicactivity_layout, parent,false);
        }
        LinearLayout loActivityInfo = view.findViewById(R.id.loActivityInfo);

        loActivityInfo.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = loActivityInfo.getVisibility();
                if (visibility == view.VISIBLE) {
                    loActivityInfo.setVisibility(view.GONE);

                }
                else{
                    loActivityInfo.setVisibility(view.VISIBLE);

                }
            }
        });


        EditText etDate,etTitle,etType,etTime;
        etTitle = view.findViewById(R.id.etTitle);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);

        BasicActivity basicActivity = activities.get(position);
        if (basicActivity.getTitle() != null)
            etTitle.setText(basicActivity.getTitle());

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
                if (!editable.toString().equals("") )
                    basicActivity.setTime(Long.parseLong(editable.toString()));
                else
                    basicActivity.setTime(Long.parseLong("0"));
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

        Spinner spinType = view.findViewById(R.id.spinType);
        ArrayAdapter<String> ad = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, BasicActivity.types);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(ad);
        if (basicActivity.getType() != null)
            for (int i = 0;i < BasicActivity.types.length;i++) {
                if (basicActivity.getType().equals(BasicActivity.types[i]))
                    spinType.setSelection(i);

            }

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                basicActivity.setType(BasicActivity.types[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText etExplanation,etEquipment;
        Button btnDelete ;

        etExplanation = view.findViewById(R.id.etExplanation);
        etEquipment = view.findViewById(R.id.etEquipment);

        btnDelete = view.findViewById(R.id.btnDelete);



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
                    //TODO delete button
            }
        });


        return view;
    }

}
