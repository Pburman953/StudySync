package com.example.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.home.databinding.FragmentManageremindersBinding;

public class ManageReminderFragment extends Fragment {

    private FragmentManageremindersBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentManageremindersBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ManageReminderFragment.this)
                        .navigate(R.id.action_ManageReminder_to_DashboardFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();



        LinearLayout container = binding.reminderHistory;

            for (Reminder reminder : Values.RemindersList) {
            Button button = new Button(requireContext());
            button.setText(String.format("%s - %n %s %n - %s - %s", reminder.getReminderName(),reminder.getDescription(),reminder.getDate(),reminder.getTime()));

            // Set button layout parameters
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT

            );
            layoutParams.setMargins(0, 0, 0, 8);

            button.setLayoutParams(layoutParams);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("YOU DID IT");

                    PopupMenu popupMenu = new PopupMenu(requireContext(), button);

                    // Inflating popup menu from popup_menu.xml file
                    popupMenu.getMenuInflater().inflate(R.menu.reminder_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            if (menuItem.getItemId() == R.id.editReminder) {
                                ReminderController.deleteReminder(reminder.getReminderName());
                                System.out.println("reminder deleted make new one");
                                NavHostFragment.findNavController(ManageReminderFragment.this)
                                        .navigate(R.id.action_ManageReminder_to_CreateReminderFragment);

                                Toast.makeText(requireContext(), "Enter New Reminder Details", Toast.LENGTH_SHORT).show();

                                return true;
                            } else if (menuItem.getItemId() == R.id.deleteReminder) {
                                ReminderController.deleteReminder(reminder.getReminderName());
                                System.out.println("reminder deleted");
                                Toast.makeText(requireContext(), "Reminder Deleted!", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(ManageReminderFragment.this)
                                        .navigate(R.id.action_ManageReminder_to_DashboardFragment);
                                return true;
                            } else {
                                return false;
                            }

                        }
                    });
                    // Showing the popup menu
                    popupMenu.show();
                }

            });
            container.addView(button);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}