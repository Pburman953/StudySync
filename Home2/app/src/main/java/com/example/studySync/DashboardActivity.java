package com.example.studySync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.example.studySync.databinding.ActivityDashboardBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean fontSizeEnabled;

    ActivityDashboardBinding binding;

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String REMINDERS_KEY = "reminders";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    private TextView screenTime;

    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        screenTime = findViewById(R.id.textView4);
        date = findViewById(R.id.textView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        date.append(currentDate);



        if(Values.totalScreenTime != null ){
            screenTime.setText(Values.totalScreenTime + " minutes");
        }
        else{
            Toast.makeText(this, "Allow data usage permissions in tracker", Toast.LENGTH_SHORT).show();
        }


        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();

        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        fontSizeEnabled = sharedPreferences.getBoolean("fontSizeEnabled", false);

        // Apply font size change when needed
        if (fontSizeEnabled) {
            applyFontSize(findViewById(R.id.textView5));
            applyFontSize(findViewById(R.id.textView6));
            applyFontSize(findViewById(R.id.textView3));
            applyFontSize(findViewById(R.id.textview7));
        }
     else {
        resetFontSize(findViewById(R.id.textView5));
            resetFontSize(findViewById(R.id.textView6));
            resetFontSize(findViewById(R.id.textView3));
            resetFontSize(findViewById(R.id.textview7));


    }

    RecyclerView recyclerView = findViewById(R.id.reminderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Set horizontal layout manager

        String remindersJson = sharedPreferences.getString(REMINDERS_KEY, "");

        if (!remindersJson.isEmpty()) {
            List<Reminder> reminderList = gson.fromJson(remindersJson, new TypeToken<List<Reminder>>(){}.getType());
            Values.RemindersList.clear();
            Values.RemindersList.addAll(reminderList);
        }

        int numButtons = Values.RemindersList.size();
        if(Values.RemindersList.size() > 0) {
            ButtonAdapter adapter = new ButtonAdapter(numButtons);
            recyclerView.setAdapter(adapter);

        }
        FloatingActionButton fab = findViewById(R.id.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ReminderActivity.class);
                startActivity(intent);
                playmenuSuccessSound();
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
            }
        });
        binding.bottomnavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // Handle click on home item
                // You can start the new activity like this:
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                startActivity(homeIntent);
                playmenuSuccessSound();
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                return true;
            } else if (item.getItemId() == R.id.notes) {
                // Handle click on notes item
                // Start another activity similarly
                Intent notesIntent = new Intent(this, NotesActivity.class);
                startActivity(notesIntent);
                playmenuSuccessSound();
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                return true;
            } else if (item.getItemId() == R.id.tracker) {
                // Handle click on tracker item
                // Start another activity similarly
                Intent trackerIntent = new Intent(this, Tracker.class);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                playmenuSuccessSound();
                startActivity(trackerIntent);
                return true;
            } else if (item.getItemId() == R.id.settings) {
                // Handle click on settings item
                // Start another activity similarly
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                startActivity(settingsIntent);
                playmenuSuccessSound();
                return true;
            }

            // Add conditions for other items if needed
            return false;
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.reminderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Set horizontal layout manager

        int numButtons = Values.RemindersList.size();
        if(Values.RemindersList.size() > 0) {
            ButtonAdapter adapter = new ButtonAdapter(numButtons);
            recyclerView.setAdapter(adapter);
        }
    }


    private void applyFontSize(TextView textView) {
        // Apply larger text size
        float increasedTextSize = getResources().getDimension(R.dimen.increased_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, increasedTextSize);
    }

    private void resetFontSize(TextView textView) {
        float defaultTextSize = getResources().getDimension(R.dimen.default_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
    }
    private void playmenuSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer.start();
    }


}
