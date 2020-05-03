package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    String ip = "";
    private static ArrayList<Message> messages = new ArrayList<>();
    public Message answerHTTP = new Message();
    boolean isServerStarted = false;
    Message message;
    static MessageAdapter messageAdapter;
    public static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        messages.add(new Message("hi").setMine(false));
        messages.add(new Message("how are u?").setMine(true));

        messageAdapter = new MessageAdapter(this, messages);
        listView = findViewById(R.id.messages_view);
        listView.setAdapter(messageAdapter);

        final ImageButton sendButton = findViewById(R.id.sendButton);
        final EditText myMessageField = findViewById(R.id.sendMessage);
        final EditText ipText = findViewById(R.id.ip);
        ServerAsyncTask.execute();

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

                    ip = ipText.getText().toString();
                    if (ip.equals("")) {
                        ip = "localhost";
                    }
                    Log.d("MainActivity", "Trying to start client");
                    new ClientAsyncTask().execute();


                    myMessageField.setText("");

                    messageAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(messages.size() - 1);


                }

            }
        });


    }

    public static ArrayList<Message> getMessages() {
        return messages;
    }

    public static void updateAdapter() {
        messageAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(messages.size() - 1);
    }


    static class ServerAsyncTask {

        static ExecutorService executorIt = Executors.newFixedThreadPool(1);

        static void execute() {
            try {
                Log.d("MainActivity", "Starting server");
                Server server = new Server();
                executorIt.execute(server);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class ClientAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("Client start", "Starting client stream");
            Client.startClient(ip, message);
            Log.d("Client start", "Stream started successfully");

            return null;
        }


    }


}

