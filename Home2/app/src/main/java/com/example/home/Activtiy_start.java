package com.example.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.home.databinding.ActivityDashboardBinding;

public class Activtiy_start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {

            @Override
                    public void run(){
                startActivity(new Intent(Activtiy_start.this,Login.class));
                finish();
            }
        },3000);
    }
}