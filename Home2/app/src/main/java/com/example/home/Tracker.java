package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.Manifest;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


public class Tracker extends AppCompatActivity {
    private static final int REQUEST_PACKAGE_USAGE_STATS = 1;

    ListView listView;
    BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        listView = findViewById(R.id.listView);
        chart = findViewById(R.id.chart);

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
        // Query and retrieve the app usage stats
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis());

// Create a BarDataSet with the package name as X axis and total time in foreground as Y axis
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>(); // Add a list to store the labels

        for (int i = 0; i < stats.size(); i++) {
            UsageStats usageStats = stats.get(i);
            String packageName = usageStats.getPackageName();
            float totalTimeInForeground = usageStats.getTotalTimeInForeground();
            totalTimeInForeground = totalTimeInForeground / 60000;
            if (totalTimeInForeground > 1) {
                entries.add(new BarEntry(i, totalTimeInForeground)); // Remove the packageName parameter from the constructor
                labels.add(packageName); // Add the packageName to the labels list
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "App Usage Stats");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        Description description = new Description();
        description.setText("App Usage Stats");
        chart.setDescription(description);

        XAxis xAxis = chart.getXAxis(); // Get the x-axis
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Set the labels for the x-axis
        xAxis.setGranularity(1f); // Ensure labels are not skipped
        xAxis.setCenterAxisLabels(true); // Center the labels on the bars
        xAxis.setLabelCount(labels.size()); // Set the number of labels to display


    }

}