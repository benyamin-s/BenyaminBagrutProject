package com.example.benyaminbagrutproject;

import java.util.ArrayList;

public class User {
    //personal info
    protected String name;
    protected String email;

    protected String userID;


    //settings info
    protected Boolean  beforeMeetNotification;
    protected int TimeBeforeMeetNotif;

    protected ArrayList<Meet> meetsList;

    public static final int USER_UPDATED = -910;


    //constructors
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        meetsList = new ArrayList<>();

    }

    public User() {
    }


    //


    public ArrayList<Meet> getMeetsList() {
        if (meetsList == null) meetsList = new ArrayList<>();
        return meetsList;
    }

    public void setMeetsList(ArrayList<Meet> meetsList) {
        this.meetsList = meetsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean getBeforeMeetNotification() {
        return beforeMeetNotification;
    }

    public void setBeforeMeetNotification(Boolean beforeMeetNotification) {
        this.beforeMeetNotification = beforeMeetNotification;
    }

    public int getTimeBeforeMeetNotif() {
        return TimeBeforeMeetNotif;
    }

    public void setTimeBeforeMeetNotif(int timeBeforeMeetNotif) {
        TimeBeforeMeetNotif = timeBeforeMeetNotif;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
