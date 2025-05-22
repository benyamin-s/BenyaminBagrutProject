package com.example.benyaminbagrutproject;

import java.util.ArrayList;

/**
 * Represents a request for activity suggestions or meeting content in the youth movement guide application.
 * Guides can create requests to seek advice or suggestions from other guides, and collect multiple answers.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class Request {
    /** Timestamp when the request was created (in milliseconds since epoch) */
    protected Long date;
    
    /** Unique identifier of the user who created the request */
    protected String requesterID;
    
    /** Display name of the user who created the request */
    protected String requesterName;
    
    /** Title/subject of the request */
    protected String requestTitle;
    
    /** Detailed content/description of the request */
    protected String requestContent;

    /** Index number for ordering/identifying the request */
    protected int Index;

    /** List of answers provided by other guides */
    protected ArrayList<Answer> answers;

    /**
     * Default constructor for Request.
     * Creates an empty Request instance with an initialized answers list.
     */
    public Request() {
        answers = new ArrayList<>();
    }

    /**
     * Constructor for creating a new Request with all fields.
     * 
     * @param date Timestamp when the request was created
     * @param requesterID Unique identifier of the requesting user
     * @param requesterName Display name of the requesting user
     * @param requestTitle Title of the request
     * @param requestContent Detailed content/description of the request
     * @param index Index number for the request
     */
    public Request(Long date, String requesterID, String requesterName, String requestTitle, String requestContent, int index) {
        this.date = date;
        this.requesterID = requesterID;
        this.requesterName = requesterName;
        this.requestTitle = requestTitle;
        this.requestContent = requestContent;
        Index = index;
        this.answers = new ArrayList<>();
    }

    /**
     * Gets the creation date of this request.
     * 
     * @return The creation date as milliseconds since epoch
     */
    public Long getDate() {
        return date;
    }

    /**
     * Sets the creation date of this request.
     * 
     * @param date The creation date as milliseconds since epoch
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * Gets the unique identifier of the requesting user.
     * 
     * @return The requester's unique ID
     */
    public String getRequesterID() {
        return requesterID;
    }

    /**
     * Sets the unique identifier of the requesting user.
     * 
     * @param requesterID The requester's unique ID
     */
    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }

    /**
     * Gets the display name of the requesting user.
     * 
     * @return The requester's display name
     */
    public String getRequesterName() {
        return requesterName;
    }

    /**
     * Sets the display name of the requesting user.
     * 
     * @param requesterName The requester's display name
     */
    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    /**
     * Gets the title of the request.
     * 
     * @return The request title
     */
    public String getRequestTitle() {
        return requestTitle;
    }

    /**
     * Sets the title of the request.
     * 
     * @param requestTitle The new request title
     */
    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    /**
     * Gets the list of answers to this request.
     * 
     * @return ArrayList of Answer objects
     */
    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    /**
     * Sets the list of answers for this request.
     * 
     * @param answers ArrayList of Answer objects
     */
    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    /**
     * Gets the index number of this request.
     * 
     * @return The request index
     */
    public int getIndex() {
        return Index;
    }

    /**
     * Sets the index number of this request.
     * 
     * @param index The new request index
     */
    public void setIndex(int index) {
        Index = index;
    }

    /**
     * Gets the detailed content/description of this request.
     * 
     * @return The request content
     */
    public String getRequestContent() {
        return requestContent;
    }

    /**
     * Sets the detailed content/description of this request.
     * 
     * @param requestContent The new request content
     */
    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }
}
