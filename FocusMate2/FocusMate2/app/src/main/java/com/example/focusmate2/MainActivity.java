package com.example.focusmate2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button mySessionsButton;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find both buttons by their IDs
        mySessionsButton = findViewById(R.id.sessionsListView);
        button = findViewById(R.id.button);

        // Set click listener for mySessionsButton
        mySessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SessionActivity.class);
                startActivity(intent);
            }
        });
    }
}
