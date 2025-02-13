package com.example.benyaminbagrutproject;

import java.util.ArrayList;

public class BasicActivity
{
    protected String activityID  ,creatorID , meetID;
    protected String title , creator;

    protected String type;
    protected Long date , time;

    protected String explanation , equipment;




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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    //

    public BasicActivity CopyActivity()
    {
        /*
        * returns copy of object
        *  */
        BasicActivity basicActivity = new BasicActivity();
        basicActivity.date = this.date;
        basicActivity.creatorID = this.creatorID;
        basicActivity.equipment = this.equipment;
        basicActivity.explanation = this.explanation;
        basicActivity.meetID = this.meetID;
        basicActivity.title = this.title;
        basicActivity.type = this.type;
        basicActivity.creator =  this.creator;
        basicActivity.time = this.time;

        return basicActivity;
    }
}

