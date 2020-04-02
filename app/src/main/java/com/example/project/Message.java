package com.example.project;

import java.security.MessageDigest;
import java.util.Date;

public class Message {
    private Date date;
    private String message;
    public Message()
    {
     message="";
     date=new Date();
    }
    public Message(String a)
    {
        message=a;
        date=new Date();
    }
    public Message(String a, Date d)
    {
        message=a;
        date=d;
    }
    //getters
    public Date getDate(){return date;}
    public String getMessage(){return message;}
    //setters
    public void setDate(Date d){date=d;}
    public void setMessage(String m){message=m;}

    public String toString(){
        return date.toString()+" "+message;
    }

}
