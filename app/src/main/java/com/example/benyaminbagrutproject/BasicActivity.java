package com.example.benyaminbagrutproject;

import java.util.ArrayList;

public class BasicActivity
{
    protected String activityID  ,creatorID , meetID;
    protected String title , creator;

    protected String type;
    protected Long date , time;

    protected String explanation , equipment;

    protected int likes;

    protected ArrayList<String> liked,disliked;
    public static final String[] types = {"משחק","תיאוריה","זמן קשר","זמן תוכן", "אחר"};


    public BasicActivity() {

        this.likes = 0;
        this.date = 0L;
        this.time = 0L;
        liked =new ArrayList<>();
        disliked = new ArrayList<>();
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    //

    public  BasicActivity(BasicActivity other)
    {
        /*
        * returns copy of object
        *  */
        
        this.date = other.date;
        this.creatorID = other.creatorID;
        this.activityID = other.activityID;
        this.equipment = other.equipment;
        this.explanation = other.explanation;
        this.meetID = other.meetID;
        this.title = other.title;
        this.type = other.type;
        this.creator = other.creator;
        this.time = other.time;
        this.likes  = other.likes;

        if (other.liked != null)
            for (String s:other.liked) {
                this.liked.add(s);
            }
        if (other.disliked != null)
            for (String s:other.disliked) {
                this.disliked.add(s);
            }

        
    }

    //


    public ArrayList<String> getLiked() {
        return liked;
    }

    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
    }

    public ArrayList<String> getDisliked() {
        return disliked;
    }

    public void setDisliked(ArrayList<String> disliked) {
        this.disliked = disliked;
    }
}

