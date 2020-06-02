package com.example.project;

import android.app.Activity;
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

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "ChatActivity";
    private Activity activity = this;

    private ImageButton backButton;
    private ImageButton recoverButton;
    private ImageButton sendButton;


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

        sendButton = findViewById(R.id.sendButton);
        recoverButton = findViewById(R.id.recoverPersonConnection);
        backButton = findViewById(R.id.arrowBackButton);
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
        if(messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(messages.size() - 1);
        }
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
                    message.setMessage(RSAHelper.encrypt(message.getMessage(), MainActivity.dialogue.getFriendsPublicKey()));

                    messages.add(msg);

                    Log.d(TAG, "Trying to start client");
                    ClientStarter.execute(ip, message);


                    myMessageField.setText("");

                    messageAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(messages.size() - 1);
                }
                break;

            case R.id.recoverPersonConnection:
                ExecutorService executorClient = Executors.newFixedThreadPool(1);
                QRGen qrGen = new QRGen();
                executorClient.execute(qrGen);

                break;
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
        SaveLoader saveLoader = new SaveLoader(MainActivity.dialogues,InvitesHead.invitesHead);
        editor.putString(MainActivity.SAVE_TAG,saveLoader.toString());
        editor.apply();
        Log.d(MainActivity.SAVE_TAG,"Everything saved");
    }

    class QRGen implements Runnable {
        ExecutorService executorClient = Executors.newFixedThreadPool(1);

        void createQR() throws WriterException {
            String message = "Привет";
            Random rnd = new Random(System.currentTimeMillis());

            //creating new connecting code
            char[] mass = new char[20];
            for (int i = 0; i < mass.length; i++) {

                mass[i] = (char) ((rnd.nextInt(2) == 1 ? (int) ('a') : (int) ('A')) + rnd.nextInt(((int) 'z' - (int) 'a')));
            }
            message = new String(mass);
            //message = RSAHelper.encrypt(message,MainActivity.dialogue.getFriendsPublicKey());

            try {
                SendAbleMessage sendAbleMessage = new SendAbleMessage().setMessage(new Message(message).setStatus(
                        2*(int)Math.pow(10,Integer.toString(MainActivity.dialogue.getId()).length())
                        +MainActivity.dialogue.getId())).setIpFor(Utils.getIPAddress(true)).setIpFrom("");
                //if last line generate exception, i will replace message on link to i wanna give u love or a link for cat

                MainActivity.dialogue.setRecoverCode(sendAbleMessage.getMessage().getMessage());
                Gson gson = new Gson();
                message = gson.toJson(sendAbleMessage);

            } catch (Exception e) {
                e.printStackTrace();
                message = rnd.nextInt(2) == 1 ? "https://www.youtube.com/watch?v=dQw4w9WgXcQ" : "https://i.ytimg.com/vi/jpsGLsaZKS0/maxresdefault.jpg";
            }
            //place code into dialogue
            MainActivity.dialogue.setRecoverCode(new String(mass));


            //creating sendabel img
            Uri bmpUri = QRCodeGenerator.getLocalBitmapUri(QRCodeGenerator.encodeAsBitmap(message, BarcodeFormat.QR_CODE, 500, 500), activity);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Something gone wrong", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void run() {
            try {
                createQR();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }
}

