package ru.irafa.conversation.model;

/**
 * POJO model class for Conversation Message author.
 * original Author: Gonçalo Silva, edited by: Raphael Gilyazitdinov.
 * original source: https://gist.github.com/goncalossilva/ce557f75d3fe3079531d97ceb068dcc5
 */

public class User {

    private long id;

    private String name;

    private String avatarUrl;

    public User(long id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
