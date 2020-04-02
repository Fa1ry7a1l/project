package com.example.project;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Dialogue extends ListActivity {
    private String name="";
    private ArrayList<Message>messages;

    //constructors
    public Dialogue()
    {
        name="";
        messages.clear();
        messages=new ArrayList<Message>();
    }
    public Dialogue(String s)
    {
        this.setName(s);
        messages.clear();

        messages=new ArrayList<Message>();
    }
    public Dialogue(String s, ArrayList<Message> m)
    {
        this.setName(s).setMessages(m);
    }

    //setters
    public Dialogue setName(String s)
    {
        try {
            char[] mass = new char[s.length()];
            s.getChars(0, s.length(), mass, 0);
            name = new String(mass);
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Ошибка в Создании строки. Диалог.",Toast.LENGTH_LONG).show();
        }
        return this;
    }
    public Dialogue setMessages(ArrayList<Message> m)
    {
        messages= (ArrayList<Message>)m.clone();
        return this;

    }

    //getters
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    //ArrayAdapter<String> dialogueAdapter=new ArrayAdapter<String>(this,android.R.layout.)






}
