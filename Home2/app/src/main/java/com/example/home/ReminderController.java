package com.example.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import com.example.home.ReminderActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReminderController {

    private static Map<Integer, PendingIntent> pendingIntentMap = new HashMap<>();

    public static void addPendingIntent(int requestCode, PendingIntent pendingIntent) {
        pendingIntentMap.put(requestCode, pendingIntent);
    }

    public static void removePendingIntent(int requestCode) {
        pendingIntentMap.remove(requestCode);
    }

    public static void cancelPendingIntent(Context context, int requestCode) {
        PendingIntent pendingIntent = pendingIntentMap.get(requestCode);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
            pendingIntentMap.remove(requestCode);
        }
        removePendingIntent(requestCode);
    }

    public static void cancelAllPendingIntents(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            for (PendingIntent pendingIntent : pendingIntentMap.values()) {
                alarmManager.cancel(pendingIntent);
            }
        }
        pendingIntentMap.clear();
    }



    public static void deleteReminder(long reminderID) {
        Iterator<Reminder> iterator = Values.RemindersList.iterator();
        while (iterator.hasNext()) {
            Reminder reminder = iterator.next();
            if (reminder.getreminderID() == (reminderID)) {
                iterator.remove(); // Removes the reminder with the specified name
                System.out.println("Reminder with name '" + reminder.getReminderName() + "' removed.");
                return; // Exit the method after removing the reminder
            }
        }
        System.out.println("No reminder found with name '" + reminderID + "'.");
    }

}
