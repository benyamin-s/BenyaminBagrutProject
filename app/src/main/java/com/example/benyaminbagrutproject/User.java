package com.example.benyaminbagrutproject;

public class User {
    //personal info
    protected String name;
    protected String email;

    //settings info
    protected Boolean requestAnsweredNotification, beforeMeetupNotification;
    protected int TimeBeforeMeetupNotif;


    //constructors
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User() {
    }


    //


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




}
