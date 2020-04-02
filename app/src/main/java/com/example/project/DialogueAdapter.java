package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DialogueAdapter extends ArrayAdapter<Dialogue> {
    public DialogueAdapter(Context context, Dialogue[] arr) {
        super(context, R.layout.dialogue_adapter, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Dialogue dialogue = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialogue_adapter, null);
        }


        //заполняем адаптер
        ((TextView) convertView.findViewById(R.id.name)).setText(dialogue.getName());

        //если сообщение есть возвращает его, иначе пустая строка
        ((TextView) convertView.findViewById(R.id.last_message)).setText((dialogue.getMessages().size()>0?dialogue.getMessages().get(dialogue.getMessages().size()-1).getMessage():""));





        return convertView;
    }
}
