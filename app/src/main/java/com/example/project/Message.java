package com.example.project;

import java.security.MessageDigest;
import java.util.Date;

public class Message {
    private Date date;
    private String message;
    private boolean isMine;

    public Message()

    {
     message="";
     date=new Date();
     isMine=false;
    }
    Message(String a)
    {
        message=a;
        date=new Date();
        isMine=false;
    }
     Message(String a, Date d)
    {
        message=a;
        date=d;
        isMine=false;
    }

     Message(String a,Date d, boolean is)
    {
        message=a;
        date=d;
        isMine=is;
    }

    //getters
     Date getDate(){return date;}
     String getMessage(){return message;}
    //setters
     void setDate(Date d){date=d;}
     void setMessage(String m)
    {
        message=m;
    }

    public Message setMine(boolean isMine)
    {
        this.isMine=isMine;
     return this;
    }


    public boolean isMine()
    {
        return isMine;
    }

    public String toString()//date message
    {
        return date.toString()+" "+message;
    }

}
