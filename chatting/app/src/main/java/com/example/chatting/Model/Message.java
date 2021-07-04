package com.example.chatting.Model;

import java.io.Serializable;

public class Message implements Serializable {

    private int id;
    private int userId;
    private int friendId;
    private String context;
    private String time;
    private String type;
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

    public Message(int id, int userId, int friendId, String context, String time, String type, boolean myself, boolean state) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.context = context;
        this.time = time;
        this.type = type;
        this.myself = myself;
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
