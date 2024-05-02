package com.example.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        RecyclerView recyclerView = findViewById(R.id.reminderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Set horizontal layout manager

        int numButtons = Values.RemindersList.size();
        if(Values.RemindersList.size() > 0) {
            ButtonAdapter adapter = new ButtonAdapter(numButtons);
            recyclerView.setAdapter(adapter);
        }
        binding.bottomnavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // Handle click on home item
                // You can start the new activity like this:
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (item.getItemId() == R.id.notes) {
                // Handle click on notes item
                // Start another activity similarly
                Intent notesIntent = new Intent(this, ManageReminderActivity.class);
                startActivity(notesIntent);
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
                Intent settingsIntent = new Intent(this, ReminderActivity.class);
                startActivity(settingsIntent);
                return true;
            }

            // Add conditions for other items if needed
            return false;
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.reminderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Set horizontal layout manager

        int numButtons = Values.RemindersList.size();
        if(Values.RemindersList.size() > 0) {
            ButtonAdapter adapter = new ButtonAdapter(numButtons);
            recyclerView.setAdapter(adapter);
        }
    }


}
