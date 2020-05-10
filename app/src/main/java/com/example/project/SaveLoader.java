package com.example.project;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SaveLoader {
    ArrayList<Dialogue> dialogues;

    InvitesHead invitesHead;

    SaveLoader()
    {
        this(new ArrayList<Dialogue>(), new InvitesHead());
    }
    SaveLoader(ArrayList<Dialogue> dialogues,InvitesHead invitesHead )
    {
        this.dialogues=dialogues;
        this.invitesHead = invitesHead;
    }

     ArrayList<Dialogue> getDialogues() {
        return dialogues;
    }

     SaveLoader setDialogues(ArrayList<Dialogue> dialogues) {
        this.dialogues = dialogues;
        return this;
    }

    public InvitesHead getInvitesHead() {
        return invitesHead;
    }

    public SaveLoader setInvitesHead(InvitesHead invitesHead) {
        this.invitesHead = invitesHead;
        return this;
    }

    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static SaveLoader fromString(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json,SaveLoader.class);
    }


}
