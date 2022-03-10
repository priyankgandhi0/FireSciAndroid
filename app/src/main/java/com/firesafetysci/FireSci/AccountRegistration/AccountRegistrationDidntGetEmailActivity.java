package com.firesafetysci.FireSci.AccountRegistration;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.firesafetysci.FireSci.R;

import java.util.Locale;

public class AccountRegistrationDidntGetEmailActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private TextView isEmailCorrectTextView, useAnotherEmailTextView;
    private Button resendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_didnt_get_email);

        initViews();
        setOnClickListeners();

        String email = SharedPrefManager.getInstance(getApplicationContext()).getEmailAddress();
        isEmailCorrectTextView.setText(String.format(Locale.US, "Is %s correct?", email));
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
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

        btnBack.setOnClickListener(v -> onBackPressed());
    }
}