package com.firesafetysci.FireSci.AccountRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.SignInActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void backArrow13 (View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
        startActivity(intent);
    }
    public void reset(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, AccountRegistrationEmailVerificationActivity.class);
        startActivity(intent);
    }
}