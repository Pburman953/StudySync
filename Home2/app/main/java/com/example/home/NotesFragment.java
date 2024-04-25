package com.example.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

public class NotesFragment extends Fragment {

    private EditText inputText;
    private ListView noteListView;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        inputText = view.findViewById(R.id.editText);
        noteListView = view.findViewById(R.id.noteListView);

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", requireActivity().MODE_PRIVATE);

        adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1);
        noteListView.setAdapter(adapter);

        String savedText = sharedPreferences.getString("savedText", "");
        String[] notesArray = savedText.split("\n");

        adapter.addAll(notesArray);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNote = adapter.getItem(position);
                inputText.setText(selectedNote);
                adapter.remove(selectedNote);
                saveNotesToSharedPreferences();
            }
        });

        Button printButton = view.findViewById(R.id.submitNote);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredText = inputText.getText().toString();
                adapter.insert(enteredText, 0);
                saveNotesToSharedPreferences();
                inputText.getText().clear();
            }
        });

        return view;
    }

    private void saveNotesToSharedPreferences() {
        StringBuilder updatedText = new StringBuilder();
        for (int i = 0; i < adapter.getCount(); i++) {
            updatedText.append(adapter.getItem(i)).append("\n");
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedText", updatedText.toString().trim());
        editor.apply();
    }
}