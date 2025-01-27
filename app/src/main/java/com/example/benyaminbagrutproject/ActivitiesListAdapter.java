package com.example.benyaminbagrutproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ActivitiesListAdapter extends BaseExpandableListAdapter {
    protected List<BasicActivity> activities;

    protected Context context;

}
