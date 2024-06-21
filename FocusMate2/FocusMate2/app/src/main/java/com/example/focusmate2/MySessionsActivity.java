package com.example.focusmate2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySessionsActivity extends AppCompatActivity {

    private ListView sessionsListView;
    private List<String> sessionsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sessions);

        sessionsListView = findViewById(R.id.sessionsListView);

        // Load saved sessions from SharedPreferences
        sessionsList = loadSessions();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sessionsList);
        sessionsListView.setAdapter(adapter);
    }

    private List<String> loadSessions() {
        SharedPreferences sharedPreferences = getSharedPreferences("sessions", Context.MODE_PRIVATE);
        Set<String> sessions = sharedPreferences.getStringSet("sessions_list", new HashSet<String>());

        return new ArrayList<>(sessions);
    }
}
 