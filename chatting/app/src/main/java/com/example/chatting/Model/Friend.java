package com.example.chatting.Model;

import com.example.chatting.Provider.DateProvider;

import java.io.Serializable;

public class Friend implements Serializable {

    private int id;
    private int userId;
    private int friendId;
    private String date;
    private boolean state;

    public Friend() {
    }

    public Friend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
        this.date = DateProvider.getDateTimeNow();
        this.state = true;
    }

    public Friend(int userId, int friendId, String date, boolean state) {
        this.userId = userId;
        this.friendId = friendId;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
