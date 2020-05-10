package com.example.project;

import java.util.Date;

public class Message {

    private String TAG = "Message";

     Date date;
     String message;
     int mine;

    Message() {
        message = "";
        date = new Date();
        mine = 0;
    }

     Message(String a) {
        message = a;
        date = new Date();
        mine = 0;
    }



    private Message(String a, Date d, boolean is) {
        message = a;
        date = d;
        mine = is?1:0;
    }
    Message(Message a)
    {
        message=new String(a.getMessage());
        date=a.date;
        mine=a.mine;
    }


     Date getDate() {
        return date;
    }

     String getMessage() {
        return message;
    }

    Message setStatus(int a)//1 - mine  0- not mine  // 2 - reconnect
    {
        this.mine = a;
        return this;
    }

    int getStatus()
    {
        return mine;
    }


    void setDate(Date d) {
        date = d;
    }

    Message setMessage(String m) {
        message = m;
        return this;
    }

     Message setMine(boolean isMine) {
        this.mine = isMine?1:0;
        return this;
    }

     boolean isMine() {
        return this.mine>0?true:false;
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
