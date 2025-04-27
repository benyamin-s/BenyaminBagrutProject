package com.example.benyaminbagrutproject;

import java.util.ArrayList;

public class Request {
    protected Long date;
    protected String requesterID , requesterName , requestTitle , requestContent;

    protected int Index;

    protected ArrayList<Answer> answers;

    public Request() {
        answers = new ArrayList<>();
    }

    public Request(Long date, String requesterID, String requesterName, String requestTitle,String requestContent, int index) {
        this.date = date;
        this.requesterID = requesterID;
        this.requesterName = requesterName;
        this.requestTitle = requestTitle;
        this.requestContent = requestContent;
        Index = index;
        this.answers = new ArrayList<>();
    }

    public Long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }


    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }
}
