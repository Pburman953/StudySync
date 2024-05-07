package com.example.studySync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterPage extends AppCompatActivity {

    EditText Name;
    EditText Email;
    EditText Password;
    EditText ConfirmPassword;
    EditText Phone;
    Button Register;
    boolean checked = false;

    ArrayList<String> User = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Name = findViewById(R.id.Name);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);
        ConfirmPassword= findViewById(R.id.ConfirmPassword);
        Register = findViewById(R.id.btn_register);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDataEntered();
                if (checked){
                    addLoginDetails();
                    Intent loginIntent = new Intent(RegisterPage.this, Login.class);
                    Toast.makeText(RegisterPage.this, "User Registered", Toast.LENGTH_SHORT).show();
                    startActivity(loginIntent);
                }
            }
        });
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered() {
        String password = Password.getText().toString();
        String confirmPassword = ConfirmPassword.getText().toString();

        if (isEmpty(Name)) {
            Name.setError("Enter Name");
        }

        if (!isEmail(Email)) {
            Email.setError("Enter valid email!");
        }

        if (isEmpty(Password)) {
            Password.setError("Enter Password");
        }
        else if (!password.equals(confirmPassword)) {
            ConfirmPassword.setError("Not the same as password");
        }
        else {
            checked = true;
        }
    }
    void addLoginDetails(){
        String name = Name.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        User.add(name);
        User.add(email);
        User.add(password);

        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.apply();
    }
}