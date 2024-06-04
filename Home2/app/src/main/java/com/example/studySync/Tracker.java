package com.example.studySync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.widget.Toast;

import com.example.studySync.databinding.ActivityTrackerBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Tracker extends AppCompatActivity {
    private static final int REQUEST_PACKAGE_USAGE_STATS = 1;


    ActivityTrackerBinding binding;

    ListView listView;

    TextView date;
    PieChart chart;

    private boolean fontSizeEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listView = findViewById(R.id.listView);
        chart = findViewById(R.id.chart);
        date = findViewById(R.id.textView);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        date.append(currentDate);

        listView.setNestedScrollingEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        fontSizeEnabled = sharedPreferences.getBoolean("fontSizeEnabled", false);

        // Apply font size change when needed
        if (fontSizeEnabled) {
            applyFontSize(findViewById(R.id.title));
            applyFontSize(findViewById(R.id.textView));


        }
        else {
            resetFontSize(findViewById(R.id.textView));
            resetFontSize(findViewById(R.id.title));



        }


        // Check if the user has granted the PACKAGE_USAGE_STATS permission
        if (!hasPackageUsageStatsPermission()) {
            // Request the PACKAGE_USAGE_STATS permission
            requestPackageUsageStatsPermission();
        } else {
            // Permission already granted, proceed with querying and displaying app usage stats
            displayAppUsageStats();
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle click on home item
                    // You can start the new activity like this:
                    Intent homeIntent = new Intent(Tracker.this, DashboardActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.notes) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent notesIntent = new Intent(Tracker.this, NotesActivity.class);
                    notesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(notesIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.Reminders) {
                    // Handle click on notes item
                    // Start another activity similarly
                    Intent reminderIntent = new Intent(Tracker.this, ReminderActivity.class);
                    reminderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(reminderIntent);
                    playmenuSuccessSound();
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    return true;
                } else if (itemId == R.id.tracker) {
                    // Handle click on tracker item
                    // Start another activity similarly
                    Intent trackerIntent = new Intent(Tracker.this, Tracker.class);
                    trackerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                    playmenuSuccessSound();
                    startActivity(trackerIntent);
                    return true;
                } else if (itemId == R.id.settings) {
                    // Handle click on settings item
                    // Start another activity similarly
                    Intent settingsIntent = new Intent(Tracker.this, SettingActivity.class);
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
        long startTime = getStartOfDayMillis();
        long endTime = getEndOfDayMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);



        List<PieEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        Integer totaltime = 0;

        for (int i = 0; i < stats.size(); i++) {
            UsageStats usageStats = stats.get(i);
            String packageName = usageStats.getPackageName();
            String simplifiedName = simplifyPackageName(packageName);

            float totalTimeInForeground = usageStats.getTotalTimeInForeground();
            totalTimeInForeground = totalTimeInForeground / 60000;
            if (totalTimeInForeground > 1) {
                entries.add(new PieEntry(i, totalTimeInForeground));
                labels.add(packageName);
                list1.add(packageName + " : " + Math.round(totalTimeInForeground) + " Minutes");
                totaltime = (int) (totaltime + totalTimeInForeground);
            }
        }
        list1.add("Total Screen Time = " + totaltime.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list1);
        listView.setAdapter(adapter);
        chart.getDescription().setEnabled(false); // Disable description
        chart.setUsePercentValues(true); // Show percentage values
        chart.setEntryLabelColor(Color.BLACK); // Set label color

// Create a PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, labels.toString());
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Set colors for the chart

// Create PieData and set it to the chart
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.invalidate(); // Re


        Values.totalScreenTime = totaltime.toString();

    }

    public static String simplifyPackageName(String packageName) {
        // Remove common prefixes
        packageName = packageName.replace("com.android.", "")
                .replace("com.google.", "")
                .replace("com.", "")
                .replace("org.", "")
                .replace("net.", "");

        // Use the last part of the package name
        String[] parts = packageName.split("\\.");
        if (parts.length > 1) {
            packageName = parts[parts.length - 1];
        }
        return capitalize(packageName);
    }

    private static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static long getStartOfDayMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDayMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    private void applyFontSize(TextView textView) {
        // Apply larger text size
        float increasedTextSize = getResources().getDimension(R.dimen.increased_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, increasedTextSize);
    }

    private void resetFontSize(TextView textView) {
        float defaultTextSize = getResources().getDimension(R.dimen.default_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.screen_slide_left,R.anim.screen_slide_out_right);

    }

    private void playmenuSuccessSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer.start();
    }

}