package com.example.home;

import static android.content.Context.ALARM_SERVICE;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

//import com.example.home.databinding.FragmentCreatereminderBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION = 490;
    private static final long DELAY_IN_MILLIS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
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
        EditText dateEditText = findViewById(R.id.editTextDate);
        EditText timeEditText = findViewById(R.id.editTextTime);

        if (!areInputsValid(reminderNameEditText, descriptionEditText, dateEditText, timeEditText)) {
            return; // Stop further execution if inputs are invalid
        }
        int reminderID = (int) System.currentTimeMillis() + new Random().nextInt(1000) ;
        String reminderName = reminderNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();

        Reminder newReminder = new Reminder(reminderID ,reminderName, description, date, time);

        Values.RemindersList.add(newReminder);
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

        startActivity(new Intent(ReminderActivity.this, ManageReminderActivity.class));
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
            } else if (editText.getId() == R.id.editTextDate) {
                if (!isValidDateFormat(input)) {
                    Toast.makeText(this, "Invalid date format (yyyy-mm-dd)", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (editText.getId() == R.id.editTextTime) {
                if (!isValidTimeFormat(input)) {
                    Toast.makeText(this, "Invalid Time Format (XX:XX 24hr)", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        return true;
    }

    private boolean isValidDateFormat(String date) {
        // Check if the date matches the specified format (yyyy-mm-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTimeFormat(String timeString) {
        // Check if the date matches the specified format (yyyy-mm-dd)
        String regexPattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(timeString);
        return matcher.matches();
    }


    private String getFieldLabel(EditText editText) {
        if (editText.getId() == EditTextFieldNames.EDIT_TEXT_REMINDER_NAME) {
            return "Reminder name";
        } else if (editText.getId() == EditTextFieldNames.EDIT_TEXT_DESCRIPTION) {
            return "Description";
        } else if (editText.getId() == EditTextFieldNames.EDIT_TEXT_DATE) {
            return "Date";
        } else if (editText.getId() == EditTextFieldNames.EDIT_TEXT_TIME) {
            return "Time";
        } else {
            return "this field";
        }
    }

    public Context getActivityContext() {
        return this;
    }
}
