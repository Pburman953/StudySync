package com.example.studySync;

import static com.google.gson.internal.bind.util.ISO8601Utils.format;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studySync.databinding.ActivityNotesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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

        inputText = findViewById(R.id.editTextNote);
        noteListView = findViewById(R.id.noteListView);
        noteListView.setNestedScrollingEnabled(true);

        sharedPreferences = getSharedPreferences("NotesPref", MODE_PRIVATE);

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
                createNote();
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


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle click on home item
                    // You can start the new activity like this:
                    Intent homeIntent = new Intent(NotesActivity.this, DashboardActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.notes) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent notesIntent = new Intent(NotesActivity.this, NotesActivity.class);
                    notesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(notesIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.Reminders) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent reminderIntent = new Intent(NotesActivity.this, ReminderActivity.class);
                    reminderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(reminderIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.tracker) {
                    // Handle click on tracker item
                    // Start another activity similarly
                    Intent trackerIntent = new Intent(NotesActivity.this, Tracker.class);
                    trackerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    playmenuSuccessSound();
                    startActivity(trackerIntent);
                    return true;
                } else if (itemId == R.id.settings) {
                    // Handle click on settings item
                    // Start another activity similarly
                    Intent settingsIntent = new Intent(NotesActivity.this, SettingActivity.class);
                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    startActivity(settingsIntent);
                    playmenuSuccessSound();
                    return true;
                }

                // Add conditions for other items if needed
                return false;
            }
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

    private void createNote(){
        String noteText = inputText.getText().toString().trim();
        Date createdAt = getCurrentDate();

        if (!noteText.isEmpty()) {
            adapter.insert(noteText, 0);
            inputText.setText("");
            playSuccessSound();
            saveNotesToSharedPreferences();
        }

        Note newNote = new Note(noteText, createdAt);

        Values.NotesList.add(newNote);
    }

    private Date getCurrentDate() {
        return new Date();
    }

}