package com.example.studySync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.studySync.databinding.ActivitySettingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    Switch switcher;
    Switch fontSizeSwitch;
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        switcher = binding.switcher;

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        fontSizeSwitch = findViewById(R.id.fontsize);
        fontSizeSwitch.setChecked(sharedPreferences.getBoolean("fontSizeEnabled", false));

        nightmode = sharedPreferences.getBoolean("nightmode", false);

        if (nightmode) {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        fontSizeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("fontSizeEnabled", isChecked).apply();
                if (isChecked) {
                    applyFontSize(findViewById(R.id.textView1));
                    applyFontSize(findViewById(R.id.textView2));

                } else {
                    resetFontSize(findViewById(R.id.textView1));
                    resetFontSize(findViewById(R.id.textView2));


                }
            }
        });

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
                Intent intent = new Intent(SettingActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });

        binding.bottomnavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (item.getItemId() == R.id.notes) {
                return true;
            } else if (item.getItemId() == R.id.tracker) {
                Intent trackerIntent = new Intent(this, Tracker.class);
                startActivity(trackerIntent);
                return true;
            } else if (item.getItemId() == R.id.settings) {
                Intent settingsIntent = new Intent(this, DashboardActivity.class);
                startActivity(settingsIntent);
                return true;
            }
            return false;
        });
    }

    private void applyFontSize(TextView textView) {
        float increasedTextSize = getResources().getDimension(R.dimen.increased_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, increasedTextSize);
    }

    private void resetFontSize(TextView textView) {
        float defaultTextSize = getResources().getDimension(R.dimen.default_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
    }
}



