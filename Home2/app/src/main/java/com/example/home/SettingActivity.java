package com.example.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.home.databinding.ActivityDashboardBinding;
import com.example.home.databinding.ActivitySettingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    Switch switcher;
    boolean nightmode;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find switcher from the binding
        switcher = binding.switcher;

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode = sharedPreferences.getBoolean("nightmode", false);

        if (nightmode) {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightmode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightmode", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightmode", true);
                }
                editor.apply();
            }
        });


        FloatingActionButton fab = findViewById(R.id.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace 'YourActivity' with the name of your activity or fragment class
                Intent intent = new Intent(SettingActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });

        binding.bottomnavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // Handle click on home item
                // You can start the new activity like this:
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (item.getItemId() == R.id.notes) {
                // Do nothing if the current item is already the Notes activity
                return true;
            } else if (item.getItemId() == R.id.tracker) {
                // Handle click on tracker item
                // Start another activity similarly
                Intent trackerIntent = new Intent(this, Tracker.class);
                startActivity(trackerIntent);
                return true;
            } else if (item.getItemId() == R.id.settings) {
                // Handle click on settings item
                // Start another activity similarly
                Intent settingsIntent = new Intent(this, DashboardActivity.class);
                startActivity(settingsIntent);
                return true;
            }

            // Add conditions for other items if needed
            return false;
        });
    }

}
