package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "ChatActivity";


    String ip ;
    //private static ArrayList<Message> messages = new ArrayList<>();
    private static ArrayList<Message> messages;
    private EditText myMessageField;
    private Message message;
    //I think it can generate mistakes
    private static MessageAdapter messageAdapter;
    public static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        ip=MainActivity.dialogue.getIp();
        messages=MainActivity.dialogue.getMessages();


        messageAdapter = new MessageAdapter(this, messages);
        listView = findViewById(R.id.messages_view);
        listView.setAdapter(messageAdapter);

        final ImageButton sendButton = findViewById(R.id.sendButton);
        final ImageButton recoverButton = findViewById(R.id.recoverPersonConnection);
        final ImageButton backButton = findViewById(R.id.arrowBackButton);
        listView.smoothScrollToPosition(messages.size() - 1);
        myMessageField = findViewById(R.id.sendMessage);


        sendButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        recoverButton.setOnClickListener(this);


    }



    public static ArrayList<Message> getMessages() {
        return messages;
    }

    public static void updateAdapter(){
        messageAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(messages.size() - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.arrowBackButton:
                onBackPressed();
                break;
            case R.id.sendButton:
                Log.d(TAG, "Button pressed");
                if (!myMessageField.getText().toString().isEmpty()) {
                    Log.d(TAG, "Sending message");
                    Message msg = new Message(myMessageField.getText().toString());
                    msg.setMine(true);
                    message = Message.copy(msg);
                    // message.setMine(false);
                    messages.add(msg);

                    Log.d(TAG, "Trying to start client");
                    ClientStarter.execute(ip,message);


                    myMessageField.setText("");

                    messageAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(messages.size() - 1);
                }
                break;

            case R.id.recoverPersonConnection:
                try {
                    createQR();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;

        }


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
        editor.putString(MainActivity.SAVE_TAG,saveLoader.toString());
        editor.apply();
        Log.d(MainActivity.SAVE_TAG,"Everything saved");
    }

    void createQR() throws WriterException {
        String message = "Привет";
        //ЗАПИШИ В MESSAGE ЧТО НИБУДЬ ДА


        //creating sendabel img
        Uri bmpUri= QRCodeGenerator.getLocalBitmapUri( QRCodeGenerator.encodeAsBitmap(message, BarcodeFormat.QR_CODE,500,500),this);
        if (bmpUri != null) {
            try {
                //bmpUri = new Uri(bmpUri.getPath());
                // Construct a ShareIntent with link to image
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                // Launch sharing dialog for image
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,"Something gone wrong", Toast.LENGTH_SHORT).show();
        }

    }

}

