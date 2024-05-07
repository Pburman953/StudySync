package com.example.studySync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ManageReminderActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String REMINDERS_KEY = "reminders";

    private SharedPreferences sharedPreferences;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reminder);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();

        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageReminderActivity.this, DashboardActivity.class));
                finish();
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




}
