package com.example.chatting.Model;

import java.io.Serializable;

public class Message implements Serializable {

    private int id;
    private int userId;
    private int friendId;
    private String context;
    private boolean myself;
    private boolean state;

    public Message() {
    }

    public Message(int userId, int friendId, String context, boolean myself) {
        this.userId = userId;
        this.friendId = friendId;
        this.context = context;
        this.myself = myself;
        this.state = true;
    }

    public Message(int userId, int friendId, String context, boolean myself, boolean state) {
        this.userId = userId;
        this.friendId = friendId;
        this.context = context;
        this.myself = myself;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isMyself() {
        return myself;
    }

    public void setMyself(boolean myself) {
        this.myself = myself;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
