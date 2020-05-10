package com.example.project;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFactory implements Runnable {
    private static String TAG = "ServerFactory";

    private static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    private void startMultiServer()  {
        try {
            ServerSocket serverSocket = new ServerSocket(3345);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();

                executeIt.execute(new ServerV2(socket));
                Log.d(TAG,"Got connection");
            }

            executeIt.shutdown();

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }




    @Override
    public void run() {
        startMultiServer();
    }
}
