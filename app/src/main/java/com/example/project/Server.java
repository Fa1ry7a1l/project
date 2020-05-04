package com.example.project;

import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {


    private static String TAG = "Server";
    private String entry;
    private Message msg;

    private void startServer() throws IOException {
        //creating socket
        ServerSocket serverSocket = new ServerSocket(3345);
        entry = "";
        // waiting for client
        try {
            while (true) {

                // client connected
                Log.d(TAG, "Waiting for client");
                Socket client = serverSocket.accept();
                //creating socket
                Log.d(TAG, "Got client");

                // creating output stream
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                Log.d(TAG, "Output stream created");
                // creating input stream
                DataInputStream in = new DataInputStream(client.getInputStream());
                Log.d(TAG, "Input stream created");

                if (!client.isClosed()) {

                    Log.d(TAG, "Reading ");
                    entry = in.readUTF();
                    Gson gson = new Gson();
                    msg = gson.fromJson(entry, Message.class);
                    msg.setMine(false);


                    //MainActivity.serverMessage.setText(entry);
                    MainActivity.listView.post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.getMessages().add(msg);
                            try {
                                MainActivity.updateAdapter();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    Log.d(TAG, "Message: " + entry);

                    Log.d(TAG, "Sent message to client");
                    out.writeUTF(entry);
                    out.flush();


                }
                //closing streams
                Log.d(TAG, "Client disconnected");
                Log.d(TAG, "Closing connections");
                in.close();
                out.close();
                //closing socket
                Log.d(TAG, "Closing socket");
                client.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {

            Log.d(TAG, "Server started from run");
            this.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
