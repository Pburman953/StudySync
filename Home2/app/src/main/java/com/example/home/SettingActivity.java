package com.example.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.home.databinding.ActivityDashboardBinding;
import com.example.home.databinding.ActivitySettingBinding;


public class SettingActivity extends AppCompatActivity {


    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivitySettingBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        setContentView(R.layout.activity_setting);


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
