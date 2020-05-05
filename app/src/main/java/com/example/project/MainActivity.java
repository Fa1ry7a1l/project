package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    public static Dialogue dialogue;
    private static DialogueAdapter dialogueAdapter;
    public static ArrayList<Dialogue> dialogues;
    public static ListView dialogueListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogues);
        ServerStarter.execute();

        dialogues= makeDialogues();
        dialogueAdapter = new DialogueAdapter(this, dialogues);
        dialogueListView = findViewById(R.id.dialoguesList);
        dialogueListView.setAdapter(dialogueAdapter);
        dialogueListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.dialogue = dialogueAdapter.getItem(position);
                Intent intentChat = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intentChat);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialogueAdapter.notifyDataSetChanged();


    }

    public static void updateAdapter()
    {
        MainActivity.dialogueAdapter.notifyDataSetChanged();
    }


    ArrayList<Dialogue> makeDialogues() {
        ArrayList<Dialogue> dialogues = new ArrayList<>();
        dialogues.add(new Dialogue());
        dialogues.get(0).setName("Mary");
        dialogues.get(0).setMessages(new ArrayList<Message>());
        dialogues.get(0).getMessages().add(new Message("hi"));

        return dialogues;
    }

    static class ServerStarter {

        static ExecutorService executorServer = Executors.newFixedThreadPool(1);

        static void execute() {
            Log.d(MainActivity.TAG, "Starting server");
            Server server = new Server();
            executorServer.execute(server);


        }
    }


}

