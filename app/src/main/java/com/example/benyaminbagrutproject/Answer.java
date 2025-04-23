package com.example.benyaminbagrutproject;

public abstract class Answer {
    protected Long date;
    protected String creatorID , creator;

    public Answer(Long date, String creatorID, String creator) {
        this.date = date;
        this.creatorID = creatorID;
        this.creator = creator;
    }
}

