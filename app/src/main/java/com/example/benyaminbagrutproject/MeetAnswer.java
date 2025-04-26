package com.example.benyaminbagrutproject;

public class MeetAnswer extends Answer {
    protected Meet meet;
    protected String explanation;

    public MeetAnswer( Meet meet,String explanation, String creatorID, String creator, int type) {
        super(creatorID, creator, type);

        this.explanation = explanation;
        this.meet = meet;
    }

    public MeetAnswer() {
        super(null, null, 0);
    }

    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
