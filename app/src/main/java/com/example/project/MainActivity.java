package com.example.project;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST = 9;
    private static String TAG = "MainActivity";
    public static String SAVE_TAG="MainActivity";

    public static Dialogue dialogue;
    private static DialogueAdapter dialogueAdapter;
    public static ArrayList<Dialogue> dialogues;

    //can be a reason of memory leak
    //replace static to a new MainActivity member
    public static ListView dialogueListView;
    private String[] permissions = new String[]{/*Manifest.permission.NFC_TRANSACTION_EVENT,*/Manifest.permission.NFC, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogues_activity);
        //Dialogue.privateKeyFormat = RSAHelper.generateKeyPair().getPrivate().getFormat();
        //Dialogue.publicKeyFormat = RSAHelper.generateKeyPair().getPublic().getFormat();

        //Log.d(TAG,"Private key format: "+ Dialogue.privateKeyFormat);
        //Log.d(TAG,"Public key format: " + Dialogue.publicKeyFormat);

        //asking for access to write/read external storage and for NFC.

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.NFC_TRANSACTION_EVENT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            Log.d(TAG, "GOT PERMISSIONS");
        }

        //server starts
        ServerStarter.execute();
        /////
        SaveLoader saveLoader=load();
        dialogues=/* makeDialogues();*/ saveLoader.getDialogues();
        InvitesHead.invitesHead=saveLoader.invitesHead;
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

        ImageButton settingsButton = findViewById(R.id.addPerson);
        settingsButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialogueAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            String msg = new String(message.getRecords()[0].getPayload());

            Gson gson = new Gson();
            SendAbleMessage sendAbleMessage = gson.fromJson(msg,SendAbleMessage.class);
            ClientStarter.execute(sendAbleMessage.getIpFor(), sendAbleMessage.getMessage(),3);

        }


    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }


    @Override
    public void onClick(View v) {
        //switch(v.getId())
        //{
        //   case R.id.addPerson:
                Intent intentChat = new Intent(getApplicationContext(), AddPersonActivity.class);
                startActivity(intentChat);
        //       break;
        //}

    }



    public static void updateAdapter()
    {
        MainActivity.dialogueAdapter.notifyDataSetChanged();
    }


    ArrayList<Dialogue> makeDialogues() {
        ArrayList<Dialogue> dialogues = new ArrayList<>();
        //dialogues.add(new Dialogue());
        //dialogues.get(0).setName("Mary").setMessages(new ArrayList<Message>());
        //dialogues.get(0).getMessages().add(new Message().setMessage("Hi, I`m Mary and Im using ur address: localhost "));
        // dialogues.get(0).setMessages(new ArrayList<Message>());

        /*
        dialogues.add(new Dialogue().setName("Mary").setMessages(new ArrayList<Message>()));
        KeyPair keyPair = RSAHelper.generateKeyPair();
        dialogues.get(0).friendsPublicKey = dialogues.get(0).myPublicKey = RSAHelper.getPublicKey(keyPair);
        dialogues.get(0).myPrivateKey = RSAHelper.getPrivateKey(keyPair);
        */
        try {
            KeyPair keyPair = RSAHelper.generateKeyPair();
            assert keyPair != null;
            dialogues.add(new Dialogue("localhost", "Mary", new ArrayList<Message>(), RSAHelper.getPrivateKey(keyPair), RSAHelper.getPublicKey(keyPair), RSAHelper.getPublicKey(keyPair)));

            dialogues.get(0).getMessages().add(new Message(RSAHelper.encrypt("What r u doing?", dialogues.get(0).friendsPublicKey)));
            dialogues.get(0).getMessages().add(new Message(RSAHelper.decrypt(RSAHelper.encrypt("What r u doing?", dialogues.get(0).friendsPublicKey), dialogues.get(0).myPrivateKey)));

            keyPair = RSAHelper.generateKeyPair();
            assert keyPair != null;
            dialogues.add(new Dialogue(Utils.getIPAddress(true), "John", new ArrayList<Message>(), RSAHelper.getPrivateKey(keyPair), RSAHelper.getPublicKey(keyPair), RSAHelper.getPublicKey(keyPair)));
            //dialogues.add(new Dialogue().setName("John").setIp(Utils.getIPAddress(true)).setMessages(new ArrayList<Message>()));
            dialogues.get(1).getMessages().add(new Message("Hi, Im John and Im using ur address: ur ip in local network: " + dialogues.get(1).getIp()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialogues;
    }



    static class ServerStarter {

        static ExecutorService executorServer = Executors.newFixedThreadPool(1);

        static void execute() {
            Log.d(MainActivity.TAG, "Starting server");
            ServerFactory server = new ServerFactory();
            executorServer.execute(server);


        }
    }

    void save() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MainActivity.SAVE_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SaveLoader saveLoader = new SaveLoader(MainActivity.dialogues, InvitesHead.invitesHead);
        editor.putString(MainActivity.SAVE_TAG, saveLoader.toString());
        editor.apply();
        Log.d(MainActivity.SAVE_TAG, "Everything saved");
    }

    SaveLoader load() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SAVE_TAG, MODE_PRIVATE);
        String loadedText = sharedPreferences.getString(MainActivity.SAVE_TAG, "");

        Log.d(TAG, "Everything loaded");
        assert loadedText != null;
        if (loadedText.length() > 5) {
            return SaveLoader.fromString(loadedText);
        }

        return new SaveLoader();
    }

}

