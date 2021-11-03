package com.firesafetysci.FireSci.AccountRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.firesafetysci.FireSci.Main.Location;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.firesafetysci.FireSci.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Objects;

public class AccountRegistrationDidntGetEmailActivity extends AppCompatActivity {
    private TextView isEmailCorrectTextView, useAnotherEmailTextView;
    private Button resendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_didnt_get_email);

        initViews();
        setOnClickListeners();

        Toolbar didntGetEmailActivityToolbar = findViewById(R.id.didntGetEmailActivityToolbar);
        didntGetEmailActivityToolbar.setTitle("");
        setSupportActionBar(didntGetEmailActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String email = SharedPrefManager.getInstance(getApplicationContext()).getEmailAddress();
        isEmailCorrectTextView.setText(String.format(Locale.US, "Is %s correct?", email));
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        isEmailCorrectTextView = findViewById(R.id.isEmailCorrectTextView);
        useAnotherEmailTextView = findViewById(R.id.useAnotherEmailTextView);
        resendEmailButton = findViewById(R.id.resendEmailButton);
    }

    private void setOnClickListeners() {
        resendEmailButton.setOnClickListener(v -> {
            setResult(2);
            finish();
        });

        useAnotherEmailTextView.setOnClickListener(v -> {
            setResult(3);
            finish();
        });
    }
}