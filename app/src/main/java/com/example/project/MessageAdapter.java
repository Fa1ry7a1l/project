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
        super(context, R.layout.dialogue_adapter, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Message message = getItem(position);
        if(message.isMine()) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_message, null);
            }
            ((TextView) convertView.findViewById(R.id.my_message_body)).setText(message.getMessage());
        }
        else
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.other_message, null);
            }
            ((TextView) convertView.findViewById(R.id.other_message_body)).setText(message.getMessage());
           // ((TextView) convertView.findViewById(R.id.sendersName)).setText(message.get);
        }

        return convertView;
    }
}