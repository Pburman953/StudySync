package com.example.studySync;

// BaseActivity.java

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NavBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBottomNavigation();
    }



    @SuppressLint("NonConstantResourceId")
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle click on home item
                    // You can start the new activity like this:
                    Intent homeIntent = new Intent(NavBarActivity.this, DashboardActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.notes) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent notesIntent = new Intent(NavBarActivity.this, NotesActivity.class);
                    notesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(notesIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.Reminders) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent reminderIntent = new Intent(NavBarActivity.this, ReminderActivity.class);
                    reminderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(reminderIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.tracker) {
                    // Handle click on tracker item
                    // Start another activity similarly
                    Intent trackerIntent = new Intent(NavBarActivity.this, Tracker.class);
                    trackerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    playmenuSuccessSound();
                    startActivity(trackerIntent);
                    return true;
                } else if (itemId == R.id.settings) {
                    // Handle click on settings item
                    // Start another activity similarly
                    Intent settingsIntent = new Intent(NavBarActivity.this, SettingActivity.class);
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


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                // Handle click on home item
                // You can start the new activity like this:
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                playmenuSuccessSound();
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                return true;
            } else if (itemId == R.id.notes) {
                // Handle click on notes item
                // Start another activity similarly
                Intent notesIntent = new Intent(this, NotesActivity.class);
                notesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notesIntent);
                playmenuSuccessSound();
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                return true;
            } else if (itemId == R.id.Reminders) {
                // Handle click on notes item
                // Start another activity similarly
                Intent reminderIntent = new Intent(this, ReminderActivity.class);
                reminderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(reminderIntent);
                playmenuSuccessSound();
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                return true;
            } else if (itemId == R.id.tracker) {
                // Handle click on tracker item
                // Start another activity similarly
                Intent trackerIntent = new Intent(this, Tracker.class);
                trackerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                playmenuSuccessSound();
                startActivity(trackerIntent);
                return true;
            } else if (itemId == R.id.settings) {
                // Handle click on settings item
                // Start another activity similarly
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                startActivity(settingsIntent);
                playmenuSuccessSound();
                return true;
            }

            // Add conditions for other items if needed
            return false;
        });
    }

    private void playmenuSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer.start();
    }

}

