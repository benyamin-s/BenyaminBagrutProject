package com.example.benyaminbagrutproject;

import java.util.Calendar;

/**
 * Abstract base class representing an answer to a request in the  application.
 * This class provides common functionality for different types of answers that users can provide
 * in response to activity suggestions or meeting content requests.
 * 
 * Concrete implementations include TextAnswer, ActivityAnswer, and MeetAnswer.
 * 
 * @author Benyamin
 * @version 1.0
 */
public abstract class Answer {
    /** Timestamp when the answer was created (in milliseconds ) */
    protected Long date;
    
    /** Unique identifier of the user who created this answer */
    protected String creatorID;
    
    /** Display name of the user who created this answer */
    protected String creator;

    /** Type identifier for this answer */
    protected int type;
    
    /** Constant for text-based answers */
    public static final int TYPE_TEXT = -113;
    
    /** Constant for activity-based answers */
    public static final int TYPE_ACTIVITY = -123;
    
    /** Constant for meeting-based answers */
    public static final int TYPE_MEET = -133;

    /**
     * Constructor for creating a new Answer instance.
     * 
     * @param creatorID Unique identifier of the user creating this answer
     * @param creator Display name of the user creating this answer
     * @param type Type of answer (use TYPE_TEXT, TYPE_ACTIVITY, or TYPE_MEET constants)
     */
    public Answer(String creatorID, String creator , int type) {
        this.date = Calendar.getInstance().getTimeInMillis();
        this.creatorID = creatorID;
        this.creator = creator;
        this.type = type;
    }

    /**
     * Gets the creation date of this answer.
     * 
     * @return The creation date as milliseconds
     */
    public Long getDate() {
        return date;
    }

    /**
     * Sets the creation date of this answer.
     * 
     * @param date The creation date as milliseconds
     */
    public void setDate(Long date) {
        this.date = date;
    }

    /**
     * Gets the unique identifier of the answer creator.
     * 
     * @return The creator's unique ID
     */
    public String getCreatorID() {
        return creatorID;
    }

    /**
     * Sets the unique identifier of the answer creator.
     * 
     * @param creatorID The creator's unique ID
     */
    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    /**
     * Gets the display name of the answer creator.
     * 
     * @return The creator's display name
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the display name of the answer creator.
     * 
     * @param creator The creator's display name
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Gets the type of this answer.
     * 
     * @return The answer type (TYPE_TEXT, TYPE_ACTIVITY, or TYPE_MEET)
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type of this answer.
     * 
     * @param type The answer type (use TYPE_TEXT, TYPE_ACTIVITY, or TYPE_MEET constants)
     */
    public void setType(int type) {
        this.type = type;
    }
}

