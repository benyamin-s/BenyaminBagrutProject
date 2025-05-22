package com.example.benyaminbagrutproject;

/**
 * Concrete implementation of Answer that represents a meeting-based response to a request.
 * This class is used when guides suggest entire meetings (with multiple activities)
 * in response to requests for comprehensive meeting content from other guides.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class MeetAnswer extends Answer {
    /** The meeting being suggested as an answer */
    protected Meet meet;
    
    /** Additional explanation or context about the suggested meeting */
    protected String explanation;

    /**
     * Constructor for creating a new MeetAnswer with a specific meeting and explanation.
     * 
     * @param meet The meeting to suggest as an answer
     * @param explanation Additional explanation or context about the meeting
     * @param creatorID Unique identifier of the user creating this answer
     * @param creator Display name of the user creating this answer
     * @param type Type of answer (should be TYPE_MEET)
     */
    public MeetAnswer( Meet meet,String explanation, String creatorID, String creator, int type) {
        super(creatorID, creator, type);
        this.explanation = explanation;
        this.meet = meet;
    }

    /**
     * Default constructor for MeetAnswer.
     * Creates an empty MeetAnswer instance, typically used for Firebase deserialization.
     */
    public MeetAnswer() {
        super(null, null, 0);
    }

    /**
     * Gets the meeting suggested in this answer.
     * 
     * @return The Meet object representing the suggested meeting
     */
    public Meet getMeet() {
        return meet;
    }

    /**
     * Sets the meeting for this answer.
     * 
     * @param meet The Meet object to set as the suggested meeting
     */
    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    /**
     * Gets the explanation or additional context for this meeting suggestion.
     * 
     * @return The explanation text
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Sets the explanation or additional context for this meeting suggestion.
     * 
     * @param explanation The explanation text to set
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
