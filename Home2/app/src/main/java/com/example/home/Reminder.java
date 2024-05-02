package com.example.home;

import android.util.Log;

public class Reminder {

    public int reminderID;

    public String Reminder;
    public String Description;
    public String Date;
    public String Time;

    public Reminder(int reminderID, String Reminder, String Description, String Date, String Time){

    this.reminderID = reminderID;
    this.Reminder = Reminder;
    this.Description = Description;
    this.Date = Date;
    this.Time = Time;

    }

    public int getreminderID() {return this.reminderID;}
    public String getReminderName() {return this.Reminder;}
    public String getDescription() {return this.Description;}
    public String getDate() {return this.Date;}
    public String getTime() {return this.Time;}

    public void logReminder() {
        Log.d("Reminder", "Name: " + getReminderName());
        Log.d("Reminder", "Description: " + getDescription());
        Log.d("Reminder", "Date: " + getDate());
        Log.d("Reminder", "Time: " + getTime());
        Log.d("CustomerDetails", "----------------------");
    }

}
