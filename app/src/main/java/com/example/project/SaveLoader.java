package com.example.project;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SaveLoader {
    ArrayList<Dialogue> dialogues;

    SaveLoader()
    {
        dialogues = new ArrayList<>();
    }
    SaveLoader(ArrayList<Dialogue> dialogues)
    {
        this.dialogues=dialogues;
    }

     ArrayList<Dialogue> getDialogues() {
        return dialogues;
    }

     SaveLoader setDialogues(ArrayList<Dialogue> dialogues) {
        this.dialogues = dialogues;
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
