package com.example.studySync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studySync.databinding.ActivityDashboardBinding;
import com.example.studySync.databinding.ActivityManageReminderBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ManageReminderActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String REMINDERS_KEY = "reminders";


    ActivityManageReminderBinding binding;


    private SharedPreferences sharedPreferences;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle click on home item
                    // You can start the new activity like this:
                    Intent homeIntent = new Intent(ManageReminderActivity.this, DashboardActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.notes) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent notesIntent = new Intent(ManageReminderActivity.this, NotesActivity.class);
                    notesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(notesIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.Reminders) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent reminderIntent = new Intent(ManageReminderActivity.this, ReminderActivity.class);
                    reminderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(reminderIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.tracker) {
                    // Handle click on tracker item
                    // Start another activity similarly
                    Intent trackerIntent = new Intent(ManageReminderActivity.this, Tracker.class);
                    trackerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    playmenuSuccessSound();
                    startActivity(trackerIntent);
                    return true;
                } else if (itemId == R.id.settings) {
                    // Handle click on settings item
                    // Start another activity similarly
                    Intent settingsIntent = new Intent(ManageReminderActivity.this, SettingActivity.class);
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

        LinearLayout container = findViewById(R.id.reminderHistory);


        String remindersJson = sharedPreferences.getString(REMINDERS_KEY, "");
        List<Reminder> reminders = new ArrayList<>();

        if (!remindersJson.isEmpty()) {
            Reminder[] reminderArray = gson.fromJson(remindersJson, Reminder[].class);
            for (Reminder reminder : reminderArray) {
                Button button = new Button(this);
                button.setText(String.format("%s - %n %s %n - %s - %s", reminder.getReminderName(),reminder.getDescription(),reminder.getDate(),reminder.getTime()));

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, 8);

                button.setLayoutParams(layoutParams);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(ManageReminderActivity.this, button);
                        popupMenu.getMenuInflater().inflate(R.menu.reminder_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(android.view.MenuItem menuItem) {
                                if (menuItem.getItemId() == R.id.deleteReminder) {
                                    ReminderController.cancelPendingIntent(getApplicationContext() ,reminder.getreminderID());
                                    ReminderController.deleteReminder(reminder.getreminderID());
                                    Toast.makeText(ManageReminderActivity.this, "Reminder Deleted!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ManageReminderActivity.this, DashboardActivity.class));
                                    finish();
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                        popupMenu.show();
                    }
                });
                container.addView(button);
            }
        }


    }

    private void playmenuSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer.start();
    }




}
