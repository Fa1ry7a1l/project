package com.example.project;

import java.util.Date;

public class Message {

    private String TAG = "Message";

     Date date;
     String message;
     boolean mine;

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

    private Message(String a, Date d, boolean is) {
        message = a;
        date = d;
        mine = is;
    }
    Message(Message a)
    {
        message=String.copyValueOf(a.getMessage().toCharArray());
        date=a.date;
        mine=a.isMine();
    }

    // getters
     Date getDate() {
        return date;
    }

     String getMessage() {
        return message;
    }

    // setters
    void setDate(Date d) {
        date = d;
    }

    void setMessage(String m) {
        message = m;
    }

     Message setMine(boolean isMine) {
        this.mine = isMine;
        return this;
    }

     boolean isMine() {
        return this.mine;
    }

     String isString()// date message
    {
        return date.toString() + " " + message;
    }

     static Message copy(Message m) {
        boolean mine = m.isMine();
        Date date = (Date) m.getDate().clone();
        String message = new String(m.getMessage());
        return (new Message(message, date, mine));
    }

}
