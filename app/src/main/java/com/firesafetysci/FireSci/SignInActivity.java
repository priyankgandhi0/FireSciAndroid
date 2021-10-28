package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firesafetysci.FireSci.AccountRegistration.ForgotPasswordActivity;
import com.firesafetysci.FireSci.AccountRegistration.AccountRegistrationTwoStepVerificationActivity;

public class SignInActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void backArrow12 (View view) {
        Intent intent = new Intent(SignInActivity.this, AccountRegistrationTwoStepVerificationActivity.class);
        startActivity(intent);
    }
    public void reset (View view) {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }  public void sign_in (View view) {
        Intent intent = new Intent(SignInActivity.this, WellcomeActivity.class);
        startActivity(intent);
    }
}