package com.example.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Tracker extends AppCompatActivity {

    private static final int REQUEST_PACKAGE_USAGE_STATS = 1;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        listView = findViewById(R.id.listView);

        // Check if the user has granted the PACKAGE_USAGE_STATS permission
        if (!hasPackageUsageStatsPermission()) {
            // Request the PACKAGE_USAGE_STATS permission
            requestPackageUsageStatsPermission();
        } else {
            // Permission already granted, proceed with querying and displaying app usage stats
            displayAppUsageStats();
        }
    }

    // Check if the user has granted the PACKAGE_USAGE_STATS permission
    private boolean hasPackageUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    // Request the PACKAGE_USAGE_STATS permission
    private void requestPackageUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, REQUEST_PACKAGE_USAGE_STATS);
    }

    // Handle the result of the permission request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PACKAGE_USAGE_STATS) {
            // Check if the user has granted the PACKAGE_USAGE_STATS permission after the request
            if (hasPackageUsageStatsPermission()) {
                // Permission granted, proceed with querying and displaying app usage stats
                displayAppUsageStats();
            } else {
                // Permission not granted, show a toast message or handle it accordingly
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Display the app usage stats in the ListView
    private void displayAppUsageStats() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        // Get the current time
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 24 * 60 * 60 * 1000; // 24 hours ago

        // Get the app usage stats for the specified time range
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // Iterate through the app usage stats
        List<String> data = new ArrayList<>();

        // Iterate through the usageStatsList
        for (UsageStats usageStats : usageStatsList) {
            String packageName = usageStats.getPackageName();
            long totalTimeInForeground = usageStats.getTotalTimeInForeground();

            // Add the package name and total time in foreground to the data list
            data.add("Package Name: " + packageName + "\nTotal Time in Foreground: " + totalTimeInForeground);
        }

        // Create an ArrayAdapter to bind the data to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);
    }
}