package com.example.project;

import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

class ServerV2 implements Runnable {
    private static String TAG = "ServerV2";
    private String entry;
    private SendAbleMessage msg;

    private Socket socket;

    ServerV2(Socket socket) {
        this.socket = socket;
    }

    private void startServer() {

        try {
            //entry = "";


            // client connected
            Log.d(TAG, "Waiting for client");
            Socket client = socket;


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
                msg = gson.fromJson(entry, SendAbleMessage.class);
                Log.d(TAG, "Message: " + entry);

                Log.d(TAG, "Sent message to client");
                out.writeUTF(entry);
                out.flush();

                Log.d(TAG, "Code for switch is: " + ((int) ((Integer.toString(msg.getMessage().getStatus())).charAt(0)) - (int) ('0')));
                switch ((int) ((Integer.toString(msg.getMessage().getStatus())).charAt(0)) - (int) ('0')) {
                    case 2:
                        //getting code of sender
                        int code = msg.getMessage().getStatus() - (int) (2 * Math.pow(10, (Integer.toString(msg.getMessage().getStatus())).length() - 1));
                        Log.d(TAG, "code " + code);
                        for (int i = 0; i < MainActivity.dialogues.size(); i++) {
                            if (MainActivity.dialogues.get(i).getId() == code) {
                                try {
                                    Log.d(TAG + "000000", gson.toJson(MainActivity.dialogue));

                                    if (msg.getMessage().getMessage().equals(MainActivity.dialogues.get(i).getRecoverCode())) {

                                        MainActivity.dialogues.get(i).setRecoverCode(null);
                                        MainActivity.dialogues.get(i).setIp(msg.getIpFrom());
                                        Log.d(TAG + "111111", gson.toJson(MainActivity.dialogue));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }

                        }
                        break;
                    case 3:
                        int num = msg.getMessage().getStatus() - (int) (3 * Math.pow(10, (Integer.toString(msg.getMessage().getStatus())).length() - 1));
                        try {
                            Log.d(TAG,"Looking for New User");
                            for (int i = 0; i < InvitesHead.invitesHead.newPeople.size(); i++) {
                                if (InvitesHead.invitesHead.newPeople.get(i).getNum() == num) {
                                    Log.d(TAG,"User found.");
                                    final NewPerson newPerson = gson.fromJson(msg.getMessage().getMessage(), NewPerson.class);
                                    if (!newPerson.getCode().equals(InvitesHead.invitesHead.newPeople.get(i).getCode())) {
                                        Log.d(TAG,"Not correct code");
                                        break;
                                    }
                                    MainActivity.dialogueListView.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            MainActivity.dialogues.add(new Dialogue().setIp(msg.getIpFrom()).setName(newPerson.getFriendName()).setMessages(new ArrayList<Message>()));
                                            MainActivity.updateAdapter();
                                            //ChatActivity.updateAdapter();

                                        }
                                    });

                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:

                        msg.getMessage().setMine(false);
                        //MainActivity.serverMessage.setText(entry);
                        MainActivity.dialogueListView.post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < MainActivity.dialogues.size(); i++) {
                                    if (MainActivity.dialogues.get(i).equals(msg)) {
                                        MainActivity.dialogues.get(i).getMessages().add(msg.getMessage());
                                    }
                                }
                                MainActivity.updateAdapter();
                                ChatActivity.updateAdapter();

                            }
                        });




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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startServer();

    }
}
