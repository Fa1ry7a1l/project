package com.example.project;

import java.security.MessageDigest;
import java.util.Date;

public class Message {
    private Date date;
    private String message;
    private boolean isMine=false;

    public Message()

    {
     message="";
     date=new Date();
     isMine=false;
    }
    public Message(String a)
    {
        message=a;
        date=new Date();
        isMine=false;
    }
    public Message(String a, Date d)
    {
        message=a;
        date=d;
        isMine=false;
    }

    public Message(String a,Date d, boolean is)
    {
        message=a;
        date=d;
        isMine=is;
    }

    //getters
    public Date getDate(){return date;}
    public String getMessage(){return message;}
    //setters
    public void setDate(Date d){date=d;}
    public void setMessage(String m){message=m;}
    public Message setMine(boolean isMine){this.isMine=isMine; return this;}


    public boolean isMine()
    {
        return isMine;
    }

    public String toString(){
        return date.toString()+" "+message;
    }

}
