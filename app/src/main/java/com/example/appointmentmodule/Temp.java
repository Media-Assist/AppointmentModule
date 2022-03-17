package com.example.appointmentmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Temp extends AppCompatActivity {
    TextView view1;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Bundle bundle = getIntent().getExtras();
        String date = bundle.getString("finaldate", "Default");

        //Intent intent = getIntent();
        //date = intent.getStringExtra("finaldate");
        Toast.makeText(Temp.this, "Date check: " + date, Toast.LENGTH_SHORT).show();
        view1 = findViewById(R.id.textView1);
        view1.setText(date);
    }
}