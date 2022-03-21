package com.firesafetysci.FireSci.AccountRegistration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.R;
import com.google.android.material.snackbar.Snackbar;

public class ForgotPasswordNewPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button continueButton;
    private EditText passwordEditText, confirmPasswordEditText;
    private LinearLayout progressBar;
    private TextView passwordsDoNotMatchTextView, mustContainUpperLowerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_new_password);

        initViews();
        setOnClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        continueButton = findViewById(R.id.continueButton);
        progressBar = findViewById(R.id.progressBarNewPassword);
        passwordsDoNotMatchTextView = findViewById(R.id.passwordsDoNotMatchTextView);
        mustContainUpperLowerTextView = findViewById(R.id.mustContainUpperLowerTextView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            passwordsDoNotMatchTextView.setVisibility(View.GONE);
            mustContainUpperLowerTextView.setVisibility(View.GONE);

            if (password.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButton), "Please enter the password!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (confirmPassword.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButton), "Please confirm the password!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!password.equals(confirmPassword)) {
                passwordsDoNotMatchTextView.setVisibility(View.VISIBLE);

            } else if (!isPasswordValid(password)) {
                mustContainUpperLowerTextView.setVisibility(View.VISIBLE);

            } else if (!CommonFunctions.isNetworkConnected(ForgotPasswordNewPasswordActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButton), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                confirmPasswordEditText.clearFocus();

                Intent intent = new Intent(ForgotPasswordNewPasswordActivity.this, RegisterOrSignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                finish();
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}$");
    }
}