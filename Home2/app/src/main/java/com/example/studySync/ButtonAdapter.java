package com.example.studySync;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {

    private int numButtons;

    public ButtonAdapter(int numButtons) {
        this.numButtons = numButtons;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_item, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numButtons;
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button button;

        Button button2;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            button2 = itemView.findViewById(R.id.button2);
        }



        public void bind(int position) {
            String name = Values.RemindersList.get(position).getReminderName();
            String date = Values.RemindersList.get(position).getDate();
            button.setText(name);
            button2.setText( date);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click
                    Intent intent = new Intent(v.getContext(), ManageReminderActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}