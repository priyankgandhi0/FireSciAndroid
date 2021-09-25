package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ChooseCountryActivity extends AppCompatActivity {
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_country);

        initViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseCountryActivity.this, CompanyDetailsActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        continueButton = findViewById(R.id.continueButtonChooseCountry);
    }
}