package com.example.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class NotesActivity extends AppCompatActivity {

    private EditText inputText;
    private ListView noteListView;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        inputText = findViewById(R.id.editText);
        noteListView = findViewById(R.id.noteListView);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        noteListView.setAdapter(adapter);

        String savedText = sharedPreferences.getString("savedText", "");
        String[] notesArray = savedText.split("\n");
        adapter.addAll(new ArrayList<>(Arrays.asList(notesArray)));

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNote = adapter.getItem(position);
                inputText.setText(selectedNote);
                adapter.remove(selectedNote);
                saveNotesToSharedPreferences();
            }
        });
    }

    private void saveNotesToSharedPreferences() {
        StringBuilder updatedText = new StringBuilder();
        for (int i = 0; i < adapter.getCount(); i++) {
            updatedText.append(adapter.getItem(i)).append("\n");
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedText", updatedText.toString().trim());
        editor.apply();
    }
}
