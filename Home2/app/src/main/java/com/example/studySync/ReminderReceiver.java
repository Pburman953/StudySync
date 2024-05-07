package com.example.studySync;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class ReminderReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = intent.getStringExtra("notificationTitle");
        String notificationDescription = intent.getStringExtra("notificationText");
        if (hasNotificationPermission(context)) {
            if(ReminderController.pendingIntentMap.containsKey(intent.getIntExtra("requestCode", 90))) {
                showNotification(context, notificationTitle, notificationDescription);
                ReminderController.deleteReminder(intent.getIntExtra("requestCode", 90));
            }
        }
    }

    private boolean hasNotificationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void showNotification(Context context, String title, String description) {

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Study Sync")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = 908;
        notificationManager.notify(notificationId, builder.build());
    }


}