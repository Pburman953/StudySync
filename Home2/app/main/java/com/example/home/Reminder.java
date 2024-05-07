package com.example.studySync;

import android.util.Log;

public class Reminder {

    public String Reminder;
    public String Description;
    public String Date;
    public String Time;

    public Reminder(String Reminder, String Description, String Date, String Time){

    this.Reminder = Reminder;
    this.Description = Description;
    this.Date = Date;
    this.Time = Time;

    }

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
