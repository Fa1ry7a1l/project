package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   // public Message[] messages ={new Message("hi").setMine(false),new Message("how are u?").setMine(true)};
    ArrayList<Message> messages = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        messages.add(new Message("hi").setMine(false));
        messages.add(new Message("how are u?").setMine(true));

    final MessageAdapter messageAdapter=new MessageAdapter(this, messages);
   final ListView listView=(ListView) findViewById(R.id.messages_view);
     listView.setAdapter(messageAdapter);

        final ImageButton sendButton=(ImageButton) findViewById(R.id.sendButton);
        final EditText myMessageField=(EditText)findViewById(R.id.sendMessage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!myMessageField.getText().toString().isEmpty())
                {
                    Message msg= new Message(myMessageField.getText().toString());
                    msg.setMine(true);
                    messages.add(msg);
                    myMessageField.setText("");

                    //MessageAdapter messageAdapter=new MessageAdapter(getApplicationContext(), messages);
                    messageAdapter.notifyDataSetChanged();
                    //listView.setAdapter(messageAdapter);
                    listView.smoothScrollToPosition(messages.size()-1);
                }
            }
        });
    }




}

