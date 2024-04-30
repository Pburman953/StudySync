package com.example.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ManageReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reminder);

        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageReminderActivity.this, DashboardActivity.class));
                finish();
            }
        });

        LinearLayout container = findViewById(R.id.reminderHistory);
        for (Reminder reminder : Values.RemindersList) {
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
                            if (menuItem.getItemId() == R.id.editReminder) {
                                ReminderController.deleteReminder(reminder.getReminderName());
                                startActivity(new Intent(ManageReminderActivity.this, ReminderActivity.class));
                                Toast.makeText(ManageReminderActivity.this, "Enter New Reminder Details", Toast.LENGTH_SHORT).show();
                                return true;
                            } else if (menuItem.getItemId() == R.id.deleteReminder) {
                                ReminderController.deleteReminder(reminder.getReminderName());
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
