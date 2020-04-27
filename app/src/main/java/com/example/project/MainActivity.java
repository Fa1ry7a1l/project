package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Message> messages = new ArrayList<>();
    public Message answerHTTP=new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        messages.add(new Message("hi").setMine(false));
        messages.add(new Message("how are u?").setMine(true));

        final MessageAdapter messageAdapter = new MessageAdapter(this, messages);
        final ListView listView = findViewById(R.id.messages_view);
        listView.setAdapter(messageAdapter);

        final ImageButton sendButton = findViewById(R.id.sendButton);
        final EditText myMessageField = findViewById(R.id.sendMessage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myMessageField.getText().toString().isEmpty()) {
                    Message msg = new Message(myMessageField.getText().toString());
                    msg.setMine(true);
                    messages.add(msg);
                    myMessageField.setText("");

                    messageAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(messages.size() - 1);


                    new MessageSender().execute();
                    while(answerHTTP.getMessage().equals(""))
                    {
                        Log.d("Going round", "do not do this");
                    }
                    messages.add(new Message(answerHTTP));

                    messageAdapter.notifyDataSetChanged();
                    answerHTTP.setMessage("");
                    listView.smoothScrollToPosition(messages.size() - 1);
                }
            }
        });
    }


     class MessageSender extends AsyncTask<Message,Message,Message> {


        @Override
        protected Message doInBackground(Message... messages) {
            // Создаем HttpClient и Post Header
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.0.8:8080");

            try{
                //httpPost.setEntity(new UrlEncodedFormEntity(new Message("hello"),"UTF-8"));
                HttpResponse response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    Gson gson = new Gson();
                    answerHTTP = gson.fromJson(EntityUtils.toString(entity),Message.class);
                    answerHTTP.setMine(false);

                    Log.d("Server","Message is: "+answerHTTP.getMessage());
                }
            }catch(Exception e)
            {
                Log.d("Server","error in server");
            }

            return null;
        }
        @Override
        protected void onPostExecute(Message result) {
            super.onPostExecute(result);
        }



    }


}

