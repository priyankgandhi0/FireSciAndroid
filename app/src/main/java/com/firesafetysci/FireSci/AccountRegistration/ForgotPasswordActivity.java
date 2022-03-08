package com.firesafetysci.FireSci.AccountRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.firesafetysci.FireSci.Main.SignInActivity;
import com.firesafetysci.FireSci.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void backArrow13(View view) {
        onBackPressed();
    }

    public void reset(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, AccountRegistrationEmailVerificationActivity.class);
        startActivity(intent);
    }
}