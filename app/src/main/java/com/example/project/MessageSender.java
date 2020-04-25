package com.example.project;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class MessageSender extends AsyncTask<Message,Message,Message> {


    @Override
    protected Message doInBackground(Message... messages) {
        // Создаем HttpClient и Post Header
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.0.9:8080");

        try{
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String answerHTTP = EntityUtils.toString(entity);
            }
        }catch(Exception e)
        {

        }

        return null;
    }



}
