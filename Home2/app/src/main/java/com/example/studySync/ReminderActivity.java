package com.example.studySync;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.studySync.databinding.ActivityReminderBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class ReminderActivity extends AppCompatActivity {


    String formattedDate;
    String formattedTime;



    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String REMINDERS_KEY = "reminders";

    private SharedPreferences reminderPreferences;
    private Gson gson;
    ActivityReminderBinding binding;

    private static final int REQUEST_NOTIFICATION = 490;
    private static final long DELAY_IN_MILLIS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Context context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        reminderPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();

        Button datePickerButton = findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        findViewById(R.id.BackReminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this, DashboardActivity.class));
                finish();
            }
        });

        findViewById(R.id.buttonMakeReservation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReminder();
            }
        });

        // Check if the user has granted the PACKAGE_USAGE_STATS permission
        if (!hasNotificationPermission()) {
            requestNotificationPermission();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle click on home item
                    // You can start the new activity like this:
                    Intent homeIntent = new Intent(ReminderActivity.this, DashboardActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.notes) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent notesIntent = new Intent(ReminderActivity.this, NotesActivity.class);
                    notesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(notesIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.Reminders) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent reminderIntent = new Intent(ReminderActivity.this, ReminderActivity.class);
                    reminderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(reminderIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.tracker) {
                    // Handle click on tracker item
                    // Start another activity similarly
                    Intent trackerIntent = new Intent(ReminderActivity.this, Tracker.class);
                    trackerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    playmenuSuccessSound();
                    startActivity(trackerIntent);
                    return true;
                } else if (itemId == R.id.settings) {
                    // Handle click on settings item
                    // Start another activity similarly
                    Intent settingsIntent = new Intent(ReminderActivity.this, SettingActivity.class);
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

    private void showDateTimePicker() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();

        // Date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

                        // Now show time picker dialog
                        showTimePicker();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Show date picker dialog
        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Get current time
        Calendar calendar = Calendar.getInstance();

        // Time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle selected time
                        formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

                        // For example, display them in a TextView
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // 24-hour format
        );

        // Show time picker dialog
        timePickerDialog.show();
    }

    public static boolean isDateTimeAfterNow(String formattedDate, String formattedTime) {
        // Get the current date and time
        Calendar currentDateTime = Calendar.getInstance();

        // Convert the formattedDate and formattedTime to Calendar objects
        Calendar selectedDateTime = Calendar.getInstance();
        selectedDateTime.set(
                Integer.parseInt(formattedDate.substring(0, 4)), // year
                Integer.parseInt(formattedDate.substring(5, 7)) - 1, // month (subtract 1 as Calendar months are zero-based)
                Integer.parseInt(formattedDate.substring(8)), // day
                Integer.parseInt(formattedTime.substring(0, 2)), // hour
                Integer.parseInt(formattedTime.substring(3)) // minute
        );

        // Compare the selected date and time with the current date and time
        return selectedDateTime.after(currentDateTime);
    }


    private boolean hasNotificationPermission() {
        boolean mode = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
        return mode;
    }



    private void requestNotificationPermission() {
        // Launch notification settings activity for the app
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName()); // Optional: Specify the package name of the app
        startActivityForResult(intent, REQUEST_NOTIFICATION);
    }

    // Handle the result of the permission request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NOTIFICATION) {
            // Check if the user has granted the notification permission after the request
            if (hasNotificationPermission()) {
                // Permission granted, perform necessary actions
            } else {
                // Permission not granted, show a toast message or handle it accordingly
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void createReminder() {



        EditText reminderNameEditText = findViewById(R.id.editTextReminderName);
        EditText descriptionEditText = findViewById(R.id.editTextDescription);



        int reminderID = (int) System.currentTimeMillis() + new Random().nextInt(1000) ;
        String reminderName = reminderNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String date = formattedDate;
        String time = formattedTime;

        if (!areInputsValid(reminderNameEditText, descriptionEditText)) {
            return; // Stop further execution if inputs are invalid
        }

        if(formattedDate != null && formattedTime != null){
            if(!isDateTimeAfterNow(formattedDate, formattedTime)){
                Toast.makeText(this, "Enter a Date and Time after the current Time", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            Toast.makeText(this, "Enter a Date and Time", Toast.LENGTH_SHORT).show();
            return;
        }

        Reminder newReminder = new Reminder(reminderID ,reminderName, description, date, time);

        Values.RemindersList.add(newReminder);
        SharedPreferences.Editor editor = reminderPreferences.edit();
        editor.putString(REMINDERS_KEY, gson.toJson(Values.RemindersList));
        editor.apply();

        Toast.makeText(this, "Reminder Created!", Toast.LENGTH_SHORT).show();
        String message = "" + reminderID;
        Log.d("improntnant", message);
        Intent notifationIntent = new Intent(getApplicationContext(), ReminderReceiver.class);
        notifationIntent.putExtra("notificationTitle", reminderName);
        notifationIntent.putExtra("notificationText", description);
        notifationIntent.putExtra("requestCode", newReminder.getreminderID());
        PendingIntent notificationPending = PendingIntent.getBroadcast(getApplicationContext(), newReminder.getreminderID(), notifationIntent, PendingIntent.FLAG_IMMUTABLE);

        String message2 = "" + newReminder.getreminderID();
        Log.d("improntnant", message2);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

        String[] timeSplit = time.split(":");
        // Set the desired time for the notification (replace these values with your desired time)
        int hour = Integer.parseInt(timeSplit[0]); // Hour in 24-hour format (0-23)
        int minute =Integer.parseInt(timeSplit[1]) ; // Minute (0-59)

        // Get the current time
        Calendar currentTime = Calendar.getInstance();

        // Set the desired time
        Calendar notifyTime = Calendar.getInstance();
        notifyTime.set(Calendar.HOUR_OF_DAY, hour);
        notifyTime.set(Calendar.MINUTE, minute);
        notifyTime.set(Calendar.SECOND, 0);

        // Check if the desired time is already passed today
        if (notifyTime.before(currentTime)) {
            // If it's already passed, set the notification for tomorrow
            notifyTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Calculate the delay until the notification time
        long delayInMillis = notifyTime.getTimeInMillis() - currentTime.getTimeInMillis();

        // Set the alarm using setExact
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delayInMillis,
                notificationPending
        );
        ReminderController.addPendingIntent(newReminder.getreminderID(), notificationPending);

        startActivity(new Intent(ReminderActivity.this, DashboardActivity.class));
        newReminder.logReminder();
        finish();
    }

    private boolean areInputsValid(EditText... editTexts) {
        for (EditText editText : editTexts) {
            String input = editText.getText().toString().trim();
            if (input.isEmpty()) {
                String fieldLabel = getFieldLabel(editText);
                String errorMessage = fieldLabel + " cannot be empty";
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        playSuccessSound();

        return true;
    }




    private String getFieldLabel(EditText editText) {
        if (editText.getId() == EditTextFieldNames.EDIT_TEXT_REMINDER_NAME) {
            return "Reminder name";
        } else if (editText.getId() == EditTextFieldNames.EDIT_TEXT_DESCRIPTION) {
            return "Description";
        } else {
            return "this field";
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.screen_slide_left,R.anim.screen_slide_out_right);

    }

    private void playSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.remindersound);
        mediaPlayer.start();
    }

    private void playmenuSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer.start();
    }

    public Context getActivityContext() {
        return this;
    }
}
