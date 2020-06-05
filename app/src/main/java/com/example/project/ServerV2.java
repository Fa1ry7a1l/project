package com.example.project;

import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.KeyPair;
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
                final Gson gson = new Gson();
                msg = gson.fromJson(entry, SendAbleMessage.class);
                Log.d(TAG, "Message: " + entry);

                Log.d(TAG, "Sent message to client");
                //out.writeUTF(entry);
                //out.flush();

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
                                out.writeUTF("Ok");
                                out.flush();
                                Log.d(TAG, "backMessage sent");
                                break;
                            }

                        }
                        break;
                    case 3:
                        int num = msg.getMessage().getStatus() - (int) (3 * Math.pow(10, (Integer.toString(msg.getMessage().getStatus())).length() - 1));
                        try {
                            Log.d(TAG, "Looking for New User " + num);
                            Log.d(TAG, "User found.");

                            NewPerson newPerson1 = gson.fromJson(gson.fromJson(msg.getMessage().getMessage(), SendAbleMessage.class).getMessage().getMessage(), NewPerson.class);


                            NewPerson returnNewPerson = newPerson1;
                            final NewPerson newPerson = returnNewPerson;

                            final KeyPair keyPair = RSAHelper.generateKeyPair();
                            MainActivity.dialogueListView.post(new Runnable() {
                                @Override
                                public void run() {

                                    MainActivity.dialogues.add(new Dialogue(msg.getIpFrom(), newPerson.getFriendName(), new ArrayList<Message>(), RSAHelper.getPrivateKey(keyPair),
                                            RSAHelper.getPublicKey(keyPair), newPerson.getFriendsPublicKey()));

                                    Log.d(TAG, "ADDED new user");
                                    MainActivity.updateAdapter();
                                    //ChatActivity.updateAdapter();

                                }
                            });
                            Log.d(TAG, newPerson.getFriendName());
                            returnNewPerson.setMyPublicKey(RSAHelper.getPublicKey(keyPair));
                            Log.d(TAG, "ReturnNewPerson is: " + gson.toJson(returnNewPerson));
                            out.writeUTF(gson.toJson(returnNewPerson));
                            Log.d(TAG, "returnNewPerson writed");
                            out.flush();

                            break;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        num = msg.getMessage().getStatus() - (int) (3 * Math.pow(10, (Integer.toString(msg.getMessage().getStatus())).length() - 1));
                        Log.d(TAG, "Looking for New User " + num);
                        Log.d(TAG, "User found.");

                        NewPerson newPerson1 = gson.fromJson(msg.getMessage().getMessage(), NewPerson.class);
                        NewPerson returnNewPerson = newPerson1;
                        final NewPerson newPerson = returnNewPerson;
                        MainActivity.dialogueListView.post(new Runnable() {
                            @Override
                            public void run() {

                                MainActivity.dialogues.add(new Dialogue(msg.getIpFrom(), newPerson.getFriendName(), new ArrayList<Message>()));

                                Log.d(TAG, "ADDED new user");
                                MainActivity.updateAdapter();
                                //ChatActivity.updateAdapter();

                            }
                        });

                        Log.d(TAG, newPerson.getFriendName());
                        Log.d(TAG, "ReturnNewPerson is: " + gson.toJson(returnNewPerson));
                        out.writeUTF(gson.toJson(returnNewPerson));
                        Log.d(TAG, "returnNewPerson writed");
                        out.flush();

                        break;
                    default:

                        msg.getMessage().setMine(false);
                        //MainActivity.serverMessage.setText(entry);
                        MainActivity.dialogueListView.post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < MainActivity.dialogues.size(); i++) {
                                    if (MainActivity.dialogues.get(i).equals(msg)) {
                                        Message message = msg.getMessage();
                                        if (MainActivity.dialogues.get(i).getFriendsPublicKey() != null)
                                            message.setMessage(RSAHelper.decrypt(message.getMessage(), MainActivity.dialogues.get(i).getMyPrivateKey()));
                                        MainActivity.dialogues.get(i).getMessages().add(message);
                                    }
                                }
                                MainActivity.updateAdapter();
                                ChatActivity.updateAdapter();

                            }
                        });
                        out.writeUTF("ok");
                        out.flush();
                        Log.d(TAG, "BackMessage sent");


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
