package com.example.benyaminbagrutproject;

import java.util.ArrayList;

public class User {
    //personal info
    protected String name;
    protected String email;

    protected String userID;


    //settings info
    protected Boolean requestAnsweredNotification, beforeMeetupNotification;
    protected int TimeBeforeMeetupNotif;

    protected ArrayList<Meet> meetsList;


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

    public Boolean getRequestAnsweredNotification() {
        return requestAnsweredNotification;
    }

    public void setRequestAnsweredNotification(Boolean requestAnsweredNotification) {
        this.requestAnsweredNotification = requestAnsweredNotification;
    }

    public Boolean getBeforeMeetupNotification() {
        return beforeMeetupNotification;
    }

    public void setBeforeMeetupNotification(Boolean beforeMeetupNotification) {
        this.beforeMeetupNotification = beforeMeetupNotification;
    }

    public int getTimeBeforeMeetupNotif() {
        return TimeBeforeMeetupNotif;
    }

    public void setTimeBeforeMeetupNotif(int timeBeforeMeetupNotif) {
        TimeBeforeMeetupNotif = timeBeforeMeetupNotif;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
