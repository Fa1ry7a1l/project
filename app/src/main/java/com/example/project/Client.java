package com.example.project;

import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private static String TAG = "Client";
    private Message message;
    private String ip;

    public int getMode() {
        return mode;
    }

    public Client setMode(int mode) {
        this.mode = mode;
        return this;
    }

    private int mode = 0;


    Client() throws Exception {
        throw new Exception("Do not use this method, client can1t work without ip and message");
    }

    Client(String ip, Message message) {
        this.message = message;
        this.ip = ip;
    }

    private void startClient(final String ip, Message message) {
        Log.d(TAG, "StartClient run");

        try {
            Log.d(TAG, "Trying to connect");
            //trying to connect to the server
            Socket socket = new Socket(ip, 3345);
            //creating streams
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            Log.d(TAG, "Connected to socket");
            Log.d(TAG, "Client writing and reading streams initialized");

            // if we have connection
            if (!socket.isOutputShutdown()) {

                Log.d(TAG, "Writing in chanel");
                Gson gson = new Gson();
                //check if ip is localhost and use real ip for other
                SendAbleMessage sendAbleMessage = new SendAbleMessage((ip.equals("localhost") ? "localhost" : Utils.getIPAddress(true)), message);
                Log.d(TAG,Utils.getIPAddress(true));
                sendAbleMessage.setIpFor(ip);
                String clientMessage = gson.toJson(sendAbleMessage);

                // we`r sending message
                out.writeUTF(clientMessage);
                out.flush();
                Log.d(TAG, "Client sent: " + clientMessage);
                Log.d(TAG, "Client kill connections");
                //waiting for answer
                try {
                    Thread.sleep(2000);
                    //do not exactly understand if it can generate exception but it is already in try{}
                    String input = in.readUTF();
                    if(getMode() == 3 && clientMessage.equals(input))
                    {
                        final NewPerson newPerson=  gson.fromJson(message.getMessage(),NewPerson.class);


                        MainActivity.dialogueListView.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.dialogues.add(new Dialogue().setName(newPerson.getOurName()).setIp(ip));
                                MainActivity.updateAdapter();

                            }
                        });
                    }
                    Log.d(TAG, "in.read():" + input);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            Log.d(TAG, "Closing connections & channels on client Side - DONE.");

            in.close();
            out.close();
            socket.close();
            setMode(0);

        } catch (Exception e) {
            e.printStackTrace();
            setMode(0);
            //Log.d(TAG, e.getMessage());
        }

    }

    @Override
    public void run() {
        startClient(ip,message);
    }
}