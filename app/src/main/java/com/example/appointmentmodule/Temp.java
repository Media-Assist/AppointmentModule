package com.example.appointmentmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Temp extends AppCompatActivity {
    TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        view = findViewById(R.id.textView2);
        view.setText(date);

    }
}