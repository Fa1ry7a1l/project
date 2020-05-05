package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, Message[] arr) {
        super(context, R.layout.my_message, arr);
    }
     MessageAdapter(Context context, ArrayList<Message> messages)
    {
        super(context, R.layout.my_message, messages);
    }




   @SuppressLint("InflateParams")
   @androidx.annotation.NonNull
    @Override
    public View getView(int position,  View convertView, @androidx.annotation.NonNull ViewGroup parent) //override method for displaying chat messages
       {

        final Message message = getItem(position);
           assert message != null;
           if(message.isMine()) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_message, parent,false);

            ((TextView) convertView.findViewById(R.id.my_message_body)).setText(message.getMessage());
        }
        else
        {
            //if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.other_message, parent,false);
            //}
            ((TextView)convertView.findViewById(R.id.sendersName)).setText(R.string.default_opponent_name);
            Log.d("MessageAdapter", "Message from other person:"+message.getMessage());
            ((TextView) convertView.findViewById(R.id.other_message_body)).setText(message.getMessage());
        }
        return convertView;
    }
}