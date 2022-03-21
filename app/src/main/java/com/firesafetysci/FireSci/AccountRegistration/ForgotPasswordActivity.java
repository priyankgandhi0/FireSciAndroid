package com.firesafetysci.FireSci.AccountRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.firesafetysci.FireSci.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText resetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetEmail = findViewById(R.id.resetEmail);
    }

    public void backArrow13(View view) {
        onBackPressed();
    }

    public void reset(View view) {
        if (!resetEmail.getText().toString().trim().isEmpty()) {
            Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordVerificationCodeActivity.class);
            startActivity(intent);
        }
    }
}