package com.example.benyaminbagrutproject;

/**
 * Concrete implementation of Answer that represents an activity-based response to a request.
 * This class is used when users suggest specific activities in response to requests for
 * meeting content or activity ideas from other users.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class ActivityAnswer extends Answer {

    /** The activity being suggested as an answer */
    protected BasicActivity basicActivity;
    
    /**
     * Constructor for creating a new ActivityAnswer with a specific activity.
     * 
     * @param basicActivity The activity to suggest as an answer
     * @param creatorID Unique identifier of the user creating this answer
     * @param creator Display name of the user creating this answer
     * @param type Type of answer (should be TYPE_ACTIVITY)
     */
    public ActivityAnswer(BasicActivity basicActivity,String creatorID, String creator, int type) {
        super(creatorID, creator, type);
        this.basicActivity = basicActivity;
    }

    /**
     * Default constructor for ActivityAnswer.
     * Creates an empty ActivityAnswer instance, typically used for Firebase deserialization.
     */
    public ActivityAnswer() {
        super(null, null, 0);
    }

    /**
     * Gets the activity suggested in this answer.
     * 
     * @return The BasicActivity object representing the suggested activity
     */
    public BasicActivity getBasicActivity() {
        return basicActivity;
    }

    /**
     * Sets the activity for this answer.
     * 
     * @param basicActivity The BasicActivity object to set as the suggested activity
     */
    public void setBasicActivity(BasicActivity basicActivity) {
        this.basicActivity = basicActivity;
    }
}
