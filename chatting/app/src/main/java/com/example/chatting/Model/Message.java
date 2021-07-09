package com.example.chatting.Model;

import com.example.chatting.Provider.DateProvider;

import java.io.Serializable;

public class Message implements Serializable {

    private String id;
    private String userId;
    private String friendId;
    private String context;
    private long time;
    private String type;
    private boolean myself;
    private boolean state;

    public Message() {
    }

    public Message(String userId, String friendId, String context, boolean myself) {
        this.userId = userId;
        this.friendId = friendId;
        this.context = context;
        this.myself = myself;
        this.time = DateProvider.getTime();
        this.type = "message";
        this.state = true;
    }

    public Message(String userId, String friendId, String context, String type, boolean myself) {
        this.userId = userId;
        this.friendId = friendId;
        this.context = context;
        this.type = type;
        this.myself = myself;
        this.time = DateProvider.getTime();
        this.state = true;
    }

    public Message(String id, String userId, String friendId, String context, long time, String type, boolean myself, boolean state) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.context = context;
        this.time = time;
        this.type = type;
        this.myself = myself;
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
