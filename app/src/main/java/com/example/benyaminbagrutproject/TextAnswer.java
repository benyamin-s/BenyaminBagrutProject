package com.example.benyaminbagrutproject;

/**
 * Concrete implementation of Answer that represents a text-based response to a request.
 * This class is used when guides provide written suggestions or advice in response to
 * activity or meeting content requests from other guides.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class TextAnswer extends Answer {
    /** The textual content of this answer */
    protected String content;

    /**
     * Constructor for creating a new TextAnswer with content.
     * 
     * @param content The text content of the answer
     * @param creatorID Unique identifier of the user creating this answer
     * @param creator Display name of the user creating this answer
     * @param type Type of answer (should be TYPE_TEXT)
     */
    public TextAnswer( String content,String creatorID, String creator, int type) {
        super( creatorID, creator, type);
        this.content = content;
    }

    /**
     * Default constructor for TextAnswer.
     * Creates an empty TextAnswer instance, typically used for Firebase deserialization.
     */
    public TextAnswer() {
        super(null, null, 0);
    }

    /**
     * Gets the text content of this answer.
     * 
     * @return The text content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the text content of this answer.
     * 
     * @param content The text content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
}
