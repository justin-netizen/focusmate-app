package com.example.focusmate2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TimerActivity extends Activity {

    private TextView sessionNameDisplay;
    private TextView timerText;
    private Button startButton;
    private Button pauseButton;
    private Button stopButton;
    private Handler handler;
    private long startTime;
    private long pausedTime;
    private boolean isRunning;
    private Runnable timerRunnable;
    private MediaPlayer mediaPlayer;
    private int pauseCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        sessionNameDisplay = findViewById(R.id.sessionNameDisplay);
        timerText = findViewById(R.id.timerText);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        handler = new Handler();
        isRunning = false;
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        // Get session name from intent
        String sessionName = getIntent().getStringExtra("SESSION_NAME");
        sessionNameDisplay.setText(sessionName);

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    long millis = System.currentTimeMillis() - startTime;
                    int seconds = (int) (millis / 1000);
                    int minutes = seconds / 60;
                    int hours = minutes / 60;
                    seconds = seconds % 60;
                    minutes = minutes % 60;

                    timerText.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
                    handler.postDelayed(this, 500);
                }
            }
        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis() - pausedTime;
                handler.postDelayed(timerRunnable, 0);
                isRunning = true;
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedTime = System.currentTimeMillis() - startTime;
                handler.removeCallbacks(timerRunnable);
                isRunning = false;
                pauseButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);

                // Increment pause count
                pauseCount++;

                // Schedule an alarm to ring if paused for too long
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isRunning) {
                            mediaPlayer.start();
                        }
                    }
                }, 600000); // 10 minutes
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedTime = 0;
                isRunning = false;
                handler.removeCallbacks(timerRunnable);
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);

                showSaveDialog(sessionName);
            }
        });
    }

    private void showSaveDialog(String sessionName) {
        String duration = timerText.getText().toString();
        String message = String.format(Locale.getDefault(), "Session: %s\nDuration: %s\nPauses: %d", sessionName, duration, pauseCount);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Session");
        builder.setMessage(message);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSession(sessionName, duration, pauseCount);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void saveSession(String sessionName, String duration, int pauseCount) {
        SharedPreferences sharedPreferences = getSharedPreferences("sessions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> sessions = sharedPreferences.getStringSet("sessions_list", new HashSet<String>());
        String session = String.format(Locale.getDefault(), "Session: %s\nDuration: %s\nPauses: %d", sessionName, duration, pauseCount);
        sessions.add(session);

        editor.putStringSet("sessions_list", sessions);
        editor.apply();
    }
}
