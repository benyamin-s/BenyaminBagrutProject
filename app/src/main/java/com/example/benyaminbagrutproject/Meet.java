package com.example.benyaminbagrutproject;


import java.util.ArrayList;
import java.util.Calendar;

public class Meet {
    protected String name;
    protected String meetID;

    protected Long date;

    protected ArrayList<String> activityIDs;

    //

    public Meet() {
    }


    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMeetID() {
        return meetID;
    }

    public void setMeetID(String meetID) {
        this.meetID = meetID;
    }


    //

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }


    //

    public ArrayList<String> getActivityIDs() {
        return activityIDs;
    }

    public void setActivityIDs(ArrayList<String> activityIDs) {
        this.activityIDs = activityIDs;
    }
}
