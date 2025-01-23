package com.example.benyaminbagrutproject;


public class Meet {
    protected String name;
    protected String meetID;

    /* add date variable here , with getter and setter */
    protected int num_of_activities;



    public Meet() {
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum_of_activities() {
        return num_of_activities;
    }

    public void setNum_of_activities(int num_of_activities) {
        this.num_of_activities = num_of_activities;
    }

    public String getMeetID() {
        return meetID;
    }

    public void setMeetID(String meetID) {
        this.meetID = meetID;
    }
}
