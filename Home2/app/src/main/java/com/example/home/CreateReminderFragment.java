package com.example.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.home.databinding.FragmentCreatereminderBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateReminderFragment extends Fragment {

    private FragmentCreatereminderBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCreatereminderBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.BackReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CreateReminderFragment.this)
                        .navigate(R.id.action_CreateReminderFragment_to_DashboardFragment);
            }
        });

        binding.buttonCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReminder();
            }
        });

    }

    private void createReminder() {
        View view = getView();

        if (view != null) {
            EditText reminderNameEditText = view.findViewById(R.id.editTextReminderName);
            EditText descriptionEditText = view.findViewById(R.id.editTextDescription);
            EditText dateEditText = view.findViewById(R.id.editTextDate);
            EditText timeEditText = view.findViewById(R.id.editTextTime);

            if (!areInputsValid(reminderNameEditText, descriptionEditText, dateEditText, timeEditText)) {
                return; // Stop further execution if inputs are invalid
            }

            String reminderName = reminderNameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String time = timeEditText.getText().toString();

            Reminder newReminder = new Reminder(reminderName, description, date, time);

            Values.RemindersList.add(newReminder);
            Toast.makeText(requireContext(), "Reminder Created!", Toast.LENGTH_SHORT).show();

            NavHostFragment.findNavController(CreateReminderFragment.this)
                    .navigate(R.id.action_CreateReminderFragment_to_ManageReminderFragment);
            newReminder.logReminder();
        }
    }

    private boolean areInputsValid(EditText... editTexts) {
        for (EditText editText : editTexts) {
            String input = editText.getText().toString().trim();
            if (input.isEmpty()) {
                String fieldLabel = getFieldLabel(editText);
                String errorMessage = fieldLabel + " cannot be empty";
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                return false;
            } else if (editText.getId() == R.id.editTextDate) {
                if (!isValidDateFormat(input)) {
                    Toast.makeText(getContext(), "Invalid date format (yyyy-mm-dd)", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (editText.getId() == R.id.editTextTime) {
                if(!isValidTimeFormat(input)){
                    Toast.makeText(getContext(), "Invalid Time Format (XX:XX 24hr)", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        return true;
    }

    private boolean isValidDateFormat(String date) {
        // Check if the date matches the specified format (yyyy-mm-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTimeFormat(String timeString) {
        // Check if the date matches the specified format (yyyy-mm-dd)
        String regexPattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(timeString);
        return matcher.matches();
    }


    private String getFieldLabel(EditText editText) {
        if (editText.getId() == EditTextFieldNames.EDIT_TEXT_REMINDER_NAME) {
            return "Reminder name";
        } else if (editText.getId() == EditTextFieldNames.EDIT_TEXT_DESCRIPTION) {
            return "Description";
        } else if (editText.getId() == EditTextFieldNames.EDIT_TEXT_DATE) {
            return "Date";
        } else if (editText.getId() == EditTextFieldNames.EDIT_TEXT_TIME) {
            return "Time";
        } else {
            return "this field";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}