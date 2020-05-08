package com.example.project;

import java.util.ArrayList;

public class Dialogue {
    private String TAG = "Dialogue";

    protected String ip = "";
    protected String name = "";
    protected String recoverCode = null;
    protected ArrayList<Message> messages;


    //constructors
    public Dialogue() {
        this("localhost","",new ArrayList<Message>());
    }



    public Dialogue(String ip, String name, ArrayList<Message> messages)
    {
        this.setIp(ip).setName(name).setMessages(messages).setMainId(mainId+1).setId(mainId);
    }


    public static int getMainId() {
        return mainId;
    }

    public Dialogue setMainId(int mainId) {
        Dialogue.mainId = mainId;
        return this;
    }

    public int getId() {
        return id;
    }

    public Dialogue setId(int id) {
        this.id = id;
        return this;
    }

    protected static int mainId = 0;
    protected int id;








    //setters
    public Dialogue setName(String s)
    {
        this.name = new String(s);
        return this;
    }
    public Dialogue setMessages(ArrayList<Message> m)
    {
        //it generates warnings in committing but if i change it  app do no work
        messages= (ArrayList<Message>)m.clone();
        return this;

    }

    public Dialogue setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Dialogue setRecoverCode(String recoverCode) {
        this.recoverCode = recoverCode;
        return this;
    }

    //getters
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getRecoverCode() {
        return recoverCode;
    }

    //it uses in server class
    public boolean equals(SendAbleMessage sendAbleMessage)
    {
        /*if(sendAbleMessage.getIpFrom().equals(this.ip))
        {
            return true;
        }
        return false;*/

        return (sendAbleMessage.getIpFrom().equals(this.ip)?true:false);
    }

}
