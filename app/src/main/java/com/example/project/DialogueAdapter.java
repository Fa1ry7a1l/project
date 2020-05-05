package com.example.project;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DialogueAdapter extends ArrayAdapter<Dialogue> {
    private String TAG = "DialogueAdapter";

    public DialogueAdapter(Context context, Dialogue[] dialogues) {
        super(context, R.layout.dialogue_adapter, dialogues);
    }

    DialogueAdapter(Context context, ArrayList<Dialogue> dialogues) {
        super(context, R.layout.dialogue_adapter, dialogues);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Dialogue dialogue = getItem(position);
        //generating adapter if it is not already made
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialogue_adapter,parent, false);
        }

        //filling adapter
        //puts name of dialogue holder into adapter
        ((TextView) convertView.findViewById(R.id.name)).setText(dialogue.getName());
        //puts text from last message in dialogue into adapter
        ((TextView) convertView.findViewById(R.id.lastMessage)).setText(dialogue.getMessages().get(dialogue.getMessages().size() - 1).getMessage());


        return convertView;
    }


}
