package com.example.project;

public class SendAbleMessage {
     String ipFrom;

    public String getIpFor() {
        return ipFor;
    }

    public SendAbleMessage setIpFor(String ipFor) {
        this.ipFor = ipFor;
        return this;
    }

    String ipFor;
     Message message;

    //constructors
    SendAbleMessage()
    {
        this("localhost",new Message());
    }
    SendAbleMessage(String ip,Message message)
    {
        this.setIpFrom(ip).setMessage(message);
    }


    //setters
    SendAbleMessage setMessage(Message message) {
        this.message = message;
        return this;
    }

     SendAbleMessage setIpFrom(String ipFrom) {
        this.ipFrom = ipFrom;
        return this;
    }

    //getters
    Message getMessage() {
        return message;
    }

    String getIpFrom() {
        return ipFrom;
    }
}
