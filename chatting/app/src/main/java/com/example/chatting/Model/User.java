package com.example.chatting.Model;

import com.example.chatting.Provider.DateProvider;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class User implements Serializable {

    private int id;
    private String email;
    private String password;
    private String timeOff;
    private String createdDate;
    private boolean state;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.timeOff = DateProvider.getDateTimeNow();
        this.createdDate = DateProvider.getDateTimeNow();
        this.state = true;
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.timeOff = DateProvider.getDateTimeNow();
        this.createdDate = DateProvider.getDateTimeNow();
        this.state = true;
    }

    public User(String email, String password, String timeOff, String createdDate, boolean state) {
        this.email = email;
        this.password = password;
        this.timeOff = timeOff;
        this.createdDate = createdDate;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
