package com.example.project;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//runs sending message stream
public class ClientStarter {
    private static String TAG = "ClientStarter";
    static ExecutorService executorClient = Executors.newFixedThreadPool(1);

    static void execute(String ip, Message message) {
        Log.d(TAG, "Starting client");
        Client client = new Client(ip, message);
        executorClient.execute(client);
    }
}

