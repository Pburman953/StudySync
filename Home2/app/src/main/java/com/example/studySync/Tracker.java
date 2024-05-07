package com.example.studySync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


        FloatingActionButton fab = findViewById(R.id.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace 'TrackerActivity' with the name of your activity or fragment class
                Intent intent = new Intent(Tracker.this, ReminderActivity.class);
                startActivity(intent);
            }
        });

        binding.bottomnavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // Handle click on home item
                // You can start the new activity like this:
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                startActivity(homeIntent);
                return true;
            } else if (item.getItemId() == R.id.notes) {
                // Handle click on notes item
                // Start another activity similarly
                Intent notesIntent = new Intent(this, NotesActivity.class);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                startActivity(notesIntent);
                return true;
            } else if (item.getItemId() == R.id.tracker) {
                // Do nothing if the current item is already the Tracker activity
                return true;
            } else if (item.getItemId() == R.id.settings) {
                // Handle click on settings item
                // Start another activity similarly
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                startActivity(settingsIntent);
                overridePendingTransition(R.anim.screen_slide_right,R.anim.screen_slide_left);
                return true;
            }

            // Add conditions for other items if needed
            return false;
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
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis());

        List<PieEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        Integer totaltime = 0;

        for (int i = 0; i < stats.size(); i++) {
            UsageStats usageStats = stats.get(i);
            String packageName = usageStats.getPackageName();
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

}