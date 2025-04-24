package com.example.benyaminbagrutproject;

import java.util.Calendar;

public abstract class Answer {
    protected Long date;
    protected String creatorID , creator;

    protected int type;
    protected static final int TYPE_TEXT = -113,TYPE_ACTIVITY = -123,TYPE_MEET = -133;

    public Answer(String creatorID, String creator , int type) {
        this.date = Calendar.getInstance().getTimeInMillis();
        this.creatorID = creatorID;
        this.creator = creator;
        this.type = type;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

