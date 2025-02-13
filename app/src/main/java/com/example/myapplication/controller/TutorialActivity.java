package com.example.myapplication.controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class TutorialActivity extends AppCompatActivity {

    private TextView closeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);
        closeTextView = findViewById(R.id.tvClose);
        closeTextView.setOnClickListener(v -> finish());
    }
}