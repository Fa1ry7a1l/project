package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, Message[] arr) {
        super(context, R.layout.my_message, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Message message = getItem(position);
        if(message.isMine()) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_message, null);
            }
        }
        else
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.other_message, null);
            }
            ((TextView)convertView.findViewById(R.id.sendersName)).setText("Mary");
        }
            ((TextView) convertView.findViewById(R.id.message_body)).setText(message.getMessage());


        return convertView;
    }
}