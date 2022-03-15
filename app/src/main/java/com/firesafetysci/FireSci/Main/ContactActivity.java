package com.firesafetysci.FireSci.Main;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.firesafetysci.FireSci.R;

public class ContactActivity extends AppCompatActivity {

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initViews();
        setOnClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void setOnClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }
}