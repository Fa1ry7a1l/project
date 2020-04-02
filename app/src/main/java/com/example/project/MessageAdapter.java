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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialogue_adapter, null);
        }






        return convertView;
    }
}