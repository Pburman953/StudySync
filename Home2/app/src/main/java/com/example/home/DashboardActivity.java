package com.example.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.example.home.databinding.ActivityDashboardBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    ActivityDashboardBinding binding;

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String REMINDERS_KEY = "reminders";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();

        RecyclerView recyclerView = findViewById(R.id.reminderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Set horizontal layout manager

        String remindersJson = sharedPreferences.getString(REMINDERS_KEY, "");

        if (!remindersJson.isEmpty()) {
            Reminder[] reminderArray = gson.fromJson(remindersJson, Reminder[].class);
            for (int i = 0; i < reminderArray.length; i++ ) {
                if(!Values.RemindersList.contains(reminderArray[i])){
                    Values.RemindersList.add(reminderArray[i]);
                }
            }
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
                // Replace 'YourActivity' with the name of your activity or fragment class
                Intent intent = new Intent(DashboardActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });
        binding.bottomnavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // Handle click on home item
                // You can start the new activity like this:
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (item.getItemId() == R.id.notes) {
                // Handle click on notes item
                // Start another activity similarly
                Intent notesIntent = new Intent(this, NotesActivity.class);
                startActivity(notesIntent);
                return true;
            } else if (item.getItemId() == R.id.tracker) {
                // Handle click on tracker item
                // Start another activity similarly
                Intent trackerIntent = new Intent(this, Tracker.class);
                startActivity(trackerIntent);
                return true;
            } else if (item.getItemId() == R.id.settings) {
                // Handle click on settings item
                // Start another activity similarly
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                startActivity(settingsIntent);
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


}
