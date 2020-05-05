package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    private String TAG = "ChatActivity";


    String ip ;
    //private static ArrayList<Message> messages = new ArrayList<>();
    private static ArrayList<Message> messages;
    Message message;
    //I think it can generate mistakes
    private static MessageAdapter messageAdapter;
    public static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        ip=MainActivity.dialogue.getIp();
        messages=MainActivity.dialogue.getMessages();


        messageAdapter = new MessageAdapter(this, messages);
        listView = findViewById(R.id.messages_view);
        listView.setAdapter(messageAdapter);

        final ImageButton sendButton = findViewById(R.id.sendButton);
        final EditText myMessageField = findViewById(R.id.sendMessage);
        final ImageButton backButton = findViewById(R.id.arrowBackButton);
        listView.smoothScrollToPosition(messages.size() - 1);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("MainActivity", "Button pressed");
                if (!myMessageField.getText().toString().isEmpty()) {
                    Log.d("MainActivity", "Sending message");
                    Message msg = new Message(myMessageField.getText().toString());
                    msg.setMine(true);
                    message = Message.copy(msg);
                    // message.setMine(false);
                    messages.add(msg);

                    Log.d("MainActivity", "Trying to start client");
                    ClientStarter.execute(ip,message);


                    myMessageField.setText("");

                    messageAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(messages.size() - 1);


                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }



    public static ArrayList<Message> getMessages() {
        return messages;
    }

    public static void updateAdapter(){
        messageAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(messages.size() - 1);
    }




    static class ClientStarter {
        static ExecutorService executorClient = Executors.newFixedThreadPool(1);

        static void execute(String ip, Message message) {
            Log.d("MainActivity", "Starting client");
            Client client = new Client(ip, message);
            executorClient.execute(client);
        }


    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }


    void save()
    {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MainActivity.SAVE_TAG,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        SaveLoader saveLoader= new SaveLoader(MainActivity.dialogues);
        editor.putString(TAG,saveLoader.toString());
        editor.apply();
        Log.d(MainActivity.SAVE_TAG,"Everything saved");
    }
}

