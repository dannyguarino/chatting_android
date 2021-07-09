package com.example.chatting.Model;

import com.example.chatting.Provider.DateProvider;

import java.io.Serializable;

public class Friend implements Serializable {

    private String id;
    private String userId;
    private String friendId;
    private boolean myself;
    private String date;
    private boolean state;

    public Friend() {
    }

    public Friend(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
        this.myself = true;
        this.date = DateProvider.getDateTimeNow();
        this.state = false;
    }

    public Friend(String userId, String friendId, String date, boolean state) {
        this.userId = userId;
        this.friendId = friendId;
        this.date = date;
        this.state = state;
    }

    public Friend(String id, String userId, String friendId) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.myself = true;
        this.date = DateProvider.getDateTimeNow();
        this.state = true;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public boolean isMyself() {
        return myself;
    }

    public void setMyself(boolean myself) {
        this.myself = myself;
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
