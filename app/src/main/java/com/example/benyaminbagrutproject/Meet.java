package com.example.benyaminbagrutproject;


import java.util.ArrayList;
import java.util.Calendar;

public class Meet {
    protected String name;
    protected String meetID;

    protected Long date;

    protected ArrayList<BasicActivity> activities;

    //

    public Meet() {
        activities=  new ArrayList<>();
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


    public ArrayList<BasicActivity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<BasicActivity> activities) {
        this.activities = activities;
    }
}
