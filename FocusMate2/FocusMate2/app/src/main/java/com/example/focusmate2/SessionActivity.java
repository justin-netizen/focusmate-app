package com.example.focusmate2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SessionActivity extends AppCompatActivity {

    private EditText sessionName;
    private Button CreateSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        sessionName = findViewById(R.id.sessionName);
        CreateSession = findViewById(R.id.CreateSession);

        CreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String session = sessionName.getText().toString();
                Intent intent = new Intent(SessionActivity.this, TimerActivity.class);
                intent.putExtra("SESSION_NAME", session);
                startActivity(intent);
            }
        });
    }
}
