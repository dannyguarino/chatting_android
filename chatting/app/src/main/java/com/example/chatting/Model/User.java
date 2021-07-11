package com.example.chatting.Model;

import com.example.chatting.Provider.DateProvider;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String avatar;
    private String name;
    private String email;
    private String password;
    private String timeOff;
    private String createdDate;
    private boolean state;

    public User() {
    }

    public User(String name, String email, String password) {
        this.avatar = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.yan.vn%2Ffacebook-cap-nhat-avatar-doi-voi-tai-khoan-khong-su-dung-anh-dai-dien-258357.html&psig=AOvVaw3Yh93VVXrec_hyahnV0fuK&ust=1626019629852000&source=images&cd=vfe&ved=0CAoQjRxqFwoTCNCXg6vx2PECFQAAAAAdAAAAABAJ";
        this.name = name;
        this.email = email;
        this.password = password;
        this.timeOff = DateProvider.getDateTimeNow();
        this.createdDate = DateProvider.getDateTimeNow();
        this.state = true;
    }

    public User(String avatar, String name, String email, String password) {
        this.avatar = avatar;
        this.name = name;
        this.email = email;
        this.password = password;
        this.timeOff = DateProvider.getDateTimeNow();
        this.createdDate = DateProvider.getDateTimeNow();
        this.state = true;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(String timeOff) {
        this.timeOff = timeOff;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
