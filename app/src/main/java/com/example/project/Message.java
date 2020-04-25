package com.example.project;

import java.util.Date;

public class Message {
    protected Date date;
    protected String message;
    protected boolean mine;

    Message() {
        message = "";
        date = new Date();
        mine = false;
    }

    Message(String a) {
        message = a;
        date = new Date();
        mine = false;
    }

    Message(String a, Date d) {
        message = a;
        date = d;
        mine = false;
    }

    Message(String a, Date d, boolean is) {
        message = a;
        date = d;
        mine = is;
    }

    // getters
    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    // setters
    void setDate(Date d) {
        date = d;
    }

    void setMessage(String m) {
        message = m;
    }

    public Message setMine(boolean isMine) {
        this.mine = isMine;
        return this;
    }

    public boolean isMine() {
        return this.mine;
    }

    public String isString()// date message
    {
        return date.toString() + " " + message;
    }

}
