package com.example.benyaminbagrutproject;

public class ActivityAnswer extends Answer {

    protected BasicActivity basicActivity;
    public ActivityAnswer(BasicActivity basicActivity,String creatorID, String creator, int type) {
        super(creatorID, creator, type);
        this.basicActivity = basicActivity;
    }

    public BasicActivity getBasicActivity() {
        return basicActivity;
    }

    public void setBasicActivity(BasicActivity basicActivity) {
        this.basicActivity = basicActivity;
    }
}
