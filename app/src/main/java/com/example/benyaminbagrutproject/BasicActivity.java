package com.example.benyaminbagrutproject;

import java.util.ArrayList;

public class BasicActivity
{
    protected String activityID  ,creatorID , meetID;
    protected String title;

    protected String type;
    protected Long date;

    protected String explanation;
    protected ArrayList<String> equipment;




    public BasicActivity() {
    }




    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getMeetID() {
        return meetID;
    }

    public void setMeetID(String meetID) {
        this.meetID = meetID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    //
    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public ArrayList<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<String> equipment) {
        this.equipment = equipment;
    }
}

