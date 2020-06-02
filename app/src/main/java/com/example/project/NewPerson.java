package com.example.project;

import java.util.Random;

public class NewPerson {

    protected int num;
    protected String friendName;
    protected String ourName;
    protected String code;

    protected byte[] myPublicKey = null;
    protected byte[] friendsPublicKey = null;


    public byte[] getMyPublicKey() {
        return myPublicKey;
    }

    public NewPerson setMyPublicKey(byte[] myPublicKey) {
        this.myPublicKey = myPublicKey;
        return this;
    }

    public byte[] getFriendsPublicKey() {
        return friendsPublicKey;
    }

    public NewPerson setFriendsPublicKey(byte[] friendsPublicKey) {
        this.friendsPublicKey = friendsPublicKey;
        return this;
    }


    public int getNum() {
        return num;
    }

    public NewPerson setNum(int num) {
        this.num = num;
        return this;
    }

    public String getFriendName() {
        return friendName;
    }

    public NewPerson setFriendName(String friendName) {
        this.friendName = friendName;
        return this;
    }

    public String getOurName() {
        return ourName;
    }

    public NewPerson setOurName(String ourName) {
        this.ourName = ourName;
        return this;
    }

    public String getCode() {
        return code;
    }

    public NewPerson setCode(String code) {
        this.code = code;
        return this;
    }


    public NewPerson(String friendName, String ourName) {
        this(friendName, ourName, ++InvitesHead.invitesHead.counter, generateCode());
    }

    public NewPerson(String friendName, String ourName, int num, String code, byte[] myPublicKey, byte[] friendsPublicKey) {
        this(friendName, ourName, num, code);
        this.myPublicKey = myPublicKey;
        this.friendsPublicKey = friendsPublicKey;
    }

    public NewPerson(String friendName, String ourName, int num, String code) {
        this.friendName = friendName;
        this.ourName = ourName;
        this.num = num;
        this.code = code;
        InvitesHead.invitesHead.newPeople.add(this);
    }



    protected static String generateCode() {
        Random rnd = new Random(System.currentTimeMillis());

        //creating new connecting code
        char[] mass = new char[20];
        for (int i = 0; i < mass.length; i++) {

            mass[i] = (char) ((rnd.nextInt(2) == 1 ? (int) ('a') : (int) ('A')) + rnd.nextInt(((int) 'z' - (int) 'a')));
        }
        return new String(mass);
    }

}
