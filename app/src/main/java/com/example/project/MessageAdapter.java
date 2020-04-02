package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, Message[] arr) {
        super(context, R.layout.my_message, arr);
    }
    public MessageAdapter(Context context, ArrayList<Message> messages)
    {
        super(context, R.layout.my_message, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Message message = getItem(position);
        if(message.isMine()) {
            //if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_message, null);

            //}
            ((TextView) convertView.findViewById(R.id.my_message_body)).setText(message.getMessage());
        }
        else
        {
            //if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.other_message, null);
            //}
            ((TextView)convertView.findViewById(R.id.sendersName)).setText("Mary");
            Log.d("MessageAdapter", "Message from other person:"+message.getMessage());

        }



        return convertView;
    }
}