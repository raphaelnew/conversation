package ru.irafa.conversation.model;

/**
 * POJO model class for Conversation Message.
 * original Author: Gonçalo Silva, edited by: Raphael Gilyazitdinov.
 * original source: https://gist.github.com/goncalossilva/ce557f75d3fe3079531d97ceb068dcc5
 */

public class Message {
    private long id;
    private long userId;
    private long postedTs;
    private String content;

    public Message(long id, long userId, long postedTs, String content) {
        this.id = id;
        this.userId = userId;
        this.postedTs = postedTs;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getPostedTs() {
        return postedTs;
    }

    public String getContent() {
        return content;
    }
}
