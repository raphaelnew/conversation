package ru.irafa.conversation.model;

import java.util.List;

import ru.irafa.conversation.dao.DaoSession;

/**
 * POJO model class for Conversation.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class Conversation {

    private List<User> users;

    private List<Message> messages;

    public Conversation() {

    }

    public Conversation(List<User> users, List<Message> messages) {
        this.users = users;
        this.messages = messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void saveToDB(DaoSession daoSession) {
        daoSession.getUserDao().insertOrReplaceInTx(users);
        daoSession.getMessageDao().insertOrReplaceInTx(messages);
    }
}
