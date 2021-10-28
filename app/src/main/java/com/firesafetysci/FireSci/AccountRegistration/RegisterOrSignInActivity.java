package com.firesafetysci.FireSci.AccountRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.SignInActivity;

public class RegisterOrSignInActivity extends AppCompatActivity {
    private Button registerButton, signInButton;
    private TextView forgotPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_sign_in);

        initViews();
        setOnClickListeners();
    }

    private void initViews() {
        registerButton = findViewById(R.id.registerButton);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
    }

    private void setOnClickListeners() {
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterOrSignInActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterOrSignInActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterOrSignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}