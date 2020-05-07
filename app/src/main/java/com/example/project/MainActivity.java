package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    public static String SAVE_TAG="MainActivity";

    public static Dialogue dialogue;
    private static DialogueAdapter dialogueAdapter;
    public static ArrayList<Dialogue> dialogues;
    public static ListView dialogueListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogues_activity);
        ServerStarter.execute();

        dialogues=/* makeDialogues();*/ load().getDialogues();
        if(dialogues.size() == 0)
        {
            dialogues=makeDialogues();
        }
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

        ImageButton settingsButton = findViewById(R.id.buttonSettings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChat = new Intent(getApplicationContext(), QRShowActivity.class);
                startActivity(intentChat);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialogueAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
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
        //dialogues.get(0).getMessages().add(new Message("hi"));

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

    void save()
    {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MainActivity.SAVE_TAG,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        SaveLoader saveLoader= new SaveLoader(MainActivity.dialogues);
        editor.putString(MainActivity.SAVE_TAG,saveLoader.toString());
        editor.apply();
        Log.d(MainActivity.SAVE_TAG,"Everything saved");
    }
    SaveLoader load()
    {
        SharedPreferences sharedPreferences= getSharedPreferences(MainActivity.SAVE_TAG,MODE_PRIVATE);
        String loadedText = sharedPreferences.getString(MainActivity.SAVE_TAG,"");

        Log.d(TAG,"Everything loaded");
        if(loadedText.length()<5)
        {
            return new SaveLoader();
        }
        return SaveLoader.fromString(loadedText);


    }

}

