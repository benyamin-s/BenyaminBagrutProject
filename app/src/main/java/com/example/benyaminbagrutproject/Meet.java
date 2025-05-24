package com.example.benyaminbagrutproject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents a meeting in the  application.
 * A meeting consists of a collection of activities planned for a specific date.
 * users can create, edit, and share meetings with other users.
 * 
 * This class serves as a container for organizing multiple BasicActivity objects
 * into a cohesive meeting structure with scheduling information.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class Meet {
    /** Name/title of the meeting */
    protected String name;
    
    /** Unique identifier for this meeting */
    protected String meetID;

    /** Scheduled date for this meeting (milliseconds) */
    protected Long date;

    /** List of activities planned for this meeting */
    protected ArrayList<BasicActivity> activities;

    /** Constant indicating a new meeting is being created */
    public final static int NEW_MEET = -100;
    
    /** Constant indicating an existing meeting is being edited */
    public final static int EDIT_MEET = -200;
    
    /** Constant indicating a meeting has been successfully saved */
    public final static int MEET_SAVED = -300;
    
    /** Constant indicating a meeting has been successfully retrieved */
    public final static int MEET_OBTAINED = -400;
    
    /** Constant indicating meeting activities have been successfully saved */
    public final static int ACTIVITIES_SAVED = -500;

    /**
     * Default constructor for Meet.
     * Initializes an empty meeting with an empty activities list.
     */
    public Meet() {
        activities = new ArrayList<>();
    }

    /**
     * Gets the name of this meeting.
     * 
     * @return The meeting name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this meeting.
     * 
     * @param name The meeting name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of this meeting.
     * 
     * @return The meeting ID
     */
    public String getMeetID() {
        return meetID;
    }

    /**
     * Sets the unique identifier of this meeting.
     * 
     * @param meetID The meeting ID to set
     */
    public void setMeetID(String meetID) {
        this.meetID = meetID;
    }

    /**
     * Gets the scheduled date for this meeting.
     * 
     * @return The meeting date as milliseconds since epoch
     */
    public Long getDate() {
        return date;
    }

    /**
     * Sets the scheduled date for this meeting.
     * 
     * @param date The meeting date as milliseconds since epoch
     */
    public void setDate(Long date) {
        this.date = date;
    }

    /**
     * Gets the list of activities planned for this meeting.
     * 
     * @return ArrayList of BasicActivity objects representing the meeting activities
     */
    public ArrayList<BasicActivity> getActivities() {
        return activities;
    }

    /**
     * Sets the list of activities for this meeting.
     * 
     * @param activities ArrayList of BasicActivity objects to set as meeting activities
     */
    public void setActivities(ArrayList<BasicActivity> activities) {
        this.activities = activities;
    }
}
