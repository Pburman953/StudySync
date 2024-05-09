package com.example.studySync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studySync.databinding.ActivityNotesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class NotesActivity extends AppCompatActivity {

    ActivityNotesBinding binding;

    private EditText inputText;
    private ListView noteListView;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputText = findViewById(R.id.editText);
        noteListView = findViewById(R.id.noteListView);
        noteListView.setNestedScrollingEnabled(true);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        noteListView.setAdapter(adapter);

        String savedText = sharedPreferences.getString("savedText", "");
        String[] notesArray = savedText.split("\n");
        adapter.addAll(new ArrayList<>(Arrays.asList(notesArray)));

        //Here notes are added from the top playing a success sound
        Submit = findViewById(R.id.submitNote);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = inputText.getText().toString().trim();
                if (!note.isEmpty()) {
                    adapter.insert(note, 0);
                    inputText.setText("");
                    playSuccessSound();
                    saveNotesToSharedPreferences();
                }
            }
        });

        //This function allows a note to be edited in the textbox
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNote = adapter.getItem(position);
                inputText.setText(selectedNote);
                adapter.remove(selectedNote);
                saveNotesToSharedPreferences();
            }
        });


        FloatingActionButton fab = findViewById(R.id.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, ReminderActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                playmenuSuccessSound();
            }
        });

        //This section is to do with the navigation bar at the bottom
        binding.bottomnavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                playmenuSuccessSound();
                startActivity(homeIntent);
                return true;
            } else if (item.getItemId() == R.id.notes) {
                return true;
            } else if (item.getItemId() == R.id.tracker) {
                Intent trackerIntent = new Intent(this, Tracker.class);
                startActivity(trackerIntent);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                playmenuSuccessSound();
                return true;
            } else if (item.getItemId() == R.id.settings) {
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                startActivity(settingsIntent);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                playmenuSuccessSound();
                return true;
            }
            return false;
        });
    }

    //sharedPreferences is where notes are saved so that they persist when the app is next run.
    private void saveNotesToSharedPreferences() {
        StringBuilder updatedText = new StringBuilder();
        for (int i = 0; i < adapter.getCount(); i++) {
            updatedText.append(adapter.getItem(i)).append("\n");
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedText", updatedText.toString().trim());
        editor.apply();
    }

    //standard transition for all pages
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.screen_slide_left,R.anim.screen_slide_out_right);
    }

    //successSound function called when a note is submitted and saved
    private void playSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.page);
        mediaPlayer.start();
    }

    private void playmenuSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer.start();
    }
}