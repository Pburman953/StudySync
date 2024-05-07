package com.example.studySync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btn_Login);
        registerButton = findViewById(R.id.btn_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                String savedEmail = sharedPreferences.getString("Email", "");
                String savedPassword = sharedPreferences.getString("Password", "");

                if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
                    if (email.equals(savedEmail) && password.equals(savedPassword)) {
                        // Login successful
                        Intent loginIntent = new Intent(Login.this, MainActivity.class);
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(loginIntent);
                        finish();
                    } else {
                        // Invalid credentials
                        Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (email.equals("Admin@example.com") && password.equals("Password")) {
                        // Login successful
                        Intent loginIntent = new Intent(Login.this, MainActivity.class);
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(loginIntent);
                        finish();
                    } else {
                        // Invalid credentials
                        Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }
}