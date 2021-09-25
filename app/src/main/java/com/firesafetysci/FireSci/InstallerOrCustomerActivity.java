package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class InstallerOrCustomerActivity extends AppCompatActivity {
    private Button installerButton, customerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installer_or_customer);

        initViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        installerButton.setOnClickListener(v -> {
            Intent intent = new Intent(InstallerOrCustomerActivity.this, ChooseCountryActivity.class);
            startActivity(intent);
        });

        customerButton.setOnClickListener(v -> {
            Intent intent = new Intent(InstallerOrCustomerActivity.this, ChooseCountryActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        installerButton = findViewById(R.id.installerButton);
        customerButton = findViewById(R.id.customerButton);
    }
}