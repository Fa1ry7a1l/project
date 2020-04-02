package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Dialogue[] arr={new Dialogue("hi",new ArrayList<Message>()),new Dialogue("hello",new ArrayList<Message>()) };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.chatList);

        //ArrayAdapter<String> chatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        DialogueAdapter dialogueAdapter=new DialogueAdapter(this,arr);
        lv.setAdapter(dialogueAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                         // Toast.makeText(getApplicationContext(),arr[position],Toast.LENGTH_LONG).show();
                                      }
                                  }
        );

    }

    ;
}

