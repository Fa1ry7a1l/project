package com.example.project;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final String serverTag="Server";
    public void startServer()
    {
        int port = 9999;
        try{
            ServerSocket serverSocket=new ServerSocket(port);
            Log.d(serverTag,"Waiting for connection---");

            Socket socket = serverSocket.accept();
            Log.d(serverTag," Client connected");

            InputStream sin=socket.getInputStream();
            OutputStream sout=socket.getOutputStream();

            DataInputStream inputStream=new DataInputStream(sin);
            DataOutputStream outputStream=new DataOutputStream(sout);


        }catch (Exception e){
            Log.e(serverTag,e.getMessage()+" server exception");}
    }


}
