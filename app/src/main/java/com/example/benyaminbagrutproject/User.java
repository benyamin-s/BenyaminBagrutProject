package com.example.benyaminbagrutproject;

import java.util.ArrayList;

/**
 * Represents a user in the youth movement guide application.
 * This class stores personal information, settings, and meeting schedules for guides.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class User {
    /** Display name of the user */
    protected String name;
    
    /** Email address of the user */
    protected String email;

    /** Flag indicating whether the user wants notifications before meetings */
    protected Boolean beforeMeetNotification;
    
    /** Time in minutes before a meeting when the notification should be sent */
    protected int TimeBeforeMeetNotif;

    /** List of meetings associated with this user */
    protected ArrayList<Meet> meetsList;

    /** Constant indicating that user data has been updated */
    public static final int USER_UPDATED = -910;

    /**
     * Constructor for creating a new User with basic information.
     * 
     * @param name The display name of the user
     * @param email The email address of the user
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        meetsList = new ArrayList<>();
    }

    /**
     * Default constructor for User.
     * Creates an empty User instance, typically used for Firebase deserialization.
     */
    public User() {
    }

    /**
     * Gets the list of meetings associated with this user.
     * If the list doesn't exist, creates a new empty list.
     * 
     * @return ArrayList of Meet objects
     */
    public ArrayList<Meet> getMeetsList() {
        if (meetsList == null) meetsList = new ArrayList<>();
        return meetsList;
    }

    /**
     * Sets the list of meetings for this user.
     * 
     * @param meetsList ArrayList of Meet objects
     */
    public void setMeetsList(ArrayList<Meet> meetsList) {
        this.meetsList = meetsList;
    }

    /**
     * Gets the display name of the user.
     * 
     * @return The user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of the user.
     * 
     * @param name The new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     * 
     * @return The user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * 
     * @param email The new email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's preference for receiving meeting notifications.
     * 
     * @return Boolean indicating if notifications are enabled
     */
    public Boolean getBeforeMeetNotification() {
        return beforeMeetNotification;
    }

    /**
     * Sets the user's preference for receiving meeting notifications.
     * 
     * @param beforeMeetNotification Boolean to enable/disable notifications
     */
    public void setBeforeMeetNotification(Boolean beforeMeetNotification) {
        this.beforeMeetNotification = beforeMeetNotification;
    }

    /**
     * Gets the notification time before meetings in minutes.
     * 
     * @return Time in minutes before a meeting when notifications should be sent
     */
    public int getTimeBeforeMeetNotif() {
        return TimeBeforeMeetNotif;
    }

    /**
     * Sets the notification time before meetings.
     * 
     * @param timeBeforeMeetNotif Time in minutes before a meeting when notifications should be sent
     */
    public void setTimeBeforeMeetNotif(int timeBeforeMeetNotif) {
        TimeBeforeMeetNotif = timeBeforeMeetNotif;
    }
}
