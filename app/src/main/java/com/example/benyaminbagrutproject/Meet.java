package com.example.benyaminbagrutproject;


import java.util.ArrayList;

public class Meet {
    protected String name;
    protected String meetID;

    protected int Date,month,year;

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


    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    //

    public ArrayList<String> getActivityIDs() {
        return activityIDs;
    }

    public void setActivityIDs(ArrayList<String> activityIDs) {
        this.activityIDs = activityIDs;
    }
}
