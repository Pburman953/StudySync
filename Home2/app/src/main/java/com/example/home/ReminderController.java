package com.example.home;

import java.util.Iterator;

public class ReminderController {



    public static void deleteReminder(String nameToRemove) {
        Iterator<Reminder> iterator = Values.RemindersList.iterator();
        while (iterator.hasNext()) {
            Reminder reminder = iterator.next();
            if (reminder.getReminderName().equals(nameToRemove)) {
                iterator.remove(); // Removes the reminder with the specified name
                System.out.println("Reminder with name '" + nameToRemove + "' removed.");
                return; // Exit the method after removing the reminder
            }
        }
        System.out.println("No reminder found with name '" + nameToRemove + "'.");
    }

}
