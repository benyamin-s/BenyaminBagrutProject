package com.example.benyaminbagrutproject;

import java.util.ArrayList;

/**
 * Represents a basic activity that can be conducted during  meetings.
 * This class encapsulates all the information about an activity including its type,
 * timing, required equipment, and user engagement metrics through likes/dislikes.
 * 
 * Activities can be shared between users and used as building blocks for meetings.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class BasicActivity
{
    /** Unique identifier for this activity */
    protected String activityID;
    
    /** Unique identifier of the user who created this activity */
    protected String creatorID;
    
    /** Identifier of the meeting this activity belongs to (if any) */
    protected String meetID;
    
    /** Title/name of the activity */
    protected String title;
    
    /** Display name of the activity creator */
    protected String creator;

    /** Type/category of the activity (game, theory, bonding time, etc.) */
    protected String type;
    
    /** Date when the activity was created (milliseconds ) */
    protected Long date;
    
    /** Duration or time allocation for this activity (in minutes) */
    protected Long time;

    /** Detailed explanation of how to conduct this activity */
    protected String explanation;
    
    /** Equipment or materials needed for this activity */
    protected String equipment;

    /** Number of likes this activity has received */
    protected int likes;

    /** List of user IDs who liked this activity */
    protected ArrayList<String> liked;
    
    /** List of user IDs who disliked this activity */
    protected ArrayList<String> disliked;
    
    /** Array of predefined activity types in Hebrew */
    public static final String[] types = {"משחק","תיאוריה","זמן קשר","זמן תוכן", "אחר"};

    /**
     * Default constructor for BasicActivity.
     * Initializes the activity with default values and empty lists for likes/dislikes.
     */
    public BasicActivity() {
        this.likes = 0;
        this.date = 0L;
        this.time = 0L;
        liked = new ArrayList<>();
        disliked = new ArrayList<>();
    }

    /**
     * Copy constructor that creates a new BasicActivity from an existing one.
     * 
     * @param other The BasicActivity to copy from
     */
    public BasicActivity(BasicActivity other)
    {
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

        this.liked = new ArrayList<>();
        this.disliked = new ArrayList<>();
        
        if (other.liked != null)
            for (String s:other.liked) {
                this.liked.add(s);
            }
        if (other.disliked != null)
            for (String s:other.disliked) {
                this.disliked.add(s);
            }
    }

    /**
     * Gets the unique identifier of this activity.
     * 
     * @return The activity ID
     */
    public String getActivityID() {
        return activityID;
    }

    /**
     * Sets the unique identifier of this activity.
     * 
     * @param activityID The activity ID to set
     */
    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    /**
     * Gets the unique identifier of the activity creator.
     * 
     * @return The creator's user ID
     */
    public String getCreatorID() {
        return creatorID;
    }

    /**
     * Sets the unique identifier of the activity creator.
     * 
     * @param creatorID The creator's user ID to set
     */
    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    /**
     * Gets the identifier of the meeting this activity belongs to.
     * 
     * @return The meeting ID, or null if not part of a specific meeting
     */
    public String getMeetID() {
        return meetID;
    }

    /**
     * Sets the identifier of the meeting this activity belongs to.
     * 
     * @param meetID The meeting ID to set
     */
    public void setMeetID(String meetID) {
        this.meetID = meetID;
    }

    /**
     * Gets the title of this activity.
     * 
     * @return The activity title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this activity.
     * 
     * @param title The activity title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the type/category of this activity.
     * 
     * @return The activity type (game, theory, bonding time, etc.)
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type/category of this activity.
     * 
     * @param type The activity type to set (should be one of the predefined types)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the time allocation for this activity.
     * 
     * @return The time in minutes, or duration for the activity
     */
    public Long getTime() {
        return time;
    }

    /**
     * Sets the time allocation for this activity.
     * 
     * @param time The time in minutes to allocate for this activity
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * Gets the creation date of this activity.
     * 
     * @return The creation date as milliseconds since epoch
     */
    public Long getDate() {
        return date;
    }

    /**
     * Sets the creation date of this activity.
     * 
     * @param date The creation date as milliseconds since epoch
     */
    public void setDate(Long date) {
        this.date = date;
    }

    /**
     * Gets the detailed explanation of how to conduct this activity.
     * 
     * @return The activity explanation/instructions
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Sets the detailed explanation of how to conduct this activity.
     * 
     * @param explanation The activity explanation/instructions to set
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * Gets the equipment or materials needed for this activity.
     * 
     * @return The equipment description
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment or materials needed for this activity.
     * 
     * @param equipment The equipment description to set
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets the display name of the activity creator.
     * 
     * @return The creator's display name
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the display name of the activity creator.
     * 
     * @param creator The creator's display name to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Gets the total number of likes this activity has received.
     * 
     * @return The number of likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Sets the total number of likes this activity has received.
     * 
     * @param likes The number of likes to set
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Gets the list of user IDs who have liked this activity.
     * 
     * @return ArrayList of user IDs who liked this activity
     */
    public ArrayList<String> getLiked() {
        return liked;
    }

    /**
     * Sets the list of user IDs who have liked this activity.
     * 
     * @param liked ArrayList of user IDs who liked this activity
     */
    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
    }

    /**
     * Gets the list of user IDs who have disliked this activity.
     * 
     * @return ArrayList of user IDs who disliked this activity
     */
    public ArrayList<String> getDisliked() {
        return disliked;
    }

    /**
     * Sets the list of user IDs who have disliked this activity.
     * 
     * @param disliked ArrayList of user IDs who disliked this activity
     */
    public void setDisliked(ArrayList<String> disliked) {
        this.disliked = disliked;
    }
}

