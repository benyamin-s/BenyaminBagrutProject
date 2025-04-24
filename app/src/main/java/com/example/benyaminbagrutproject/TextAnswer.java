package com.example.benyaminbagrutproject;

public class TextAnswer extends Answer {
    protected String content;

    public TextAnswer( String content,String creatorID, String creator, int type) {
        super( creatorID, creator, type);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
