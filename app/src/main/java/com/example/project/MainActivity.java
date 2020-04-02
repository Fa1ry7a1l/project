package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Message[] messages ={new Message("hi").setMine(false),new Message("how are u?").setMine(true)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

    MessageAdapter messageAdapter=new MessageAdapter(this, messages);
   ListView listView=(ListView) findViewById(R.id.messages_view);
    listView.setAdapter(messageAdapter);

    }


}

