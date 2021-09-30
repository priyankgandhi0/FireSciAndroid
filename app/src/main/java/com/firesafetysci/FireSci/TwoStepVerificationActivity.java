package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TwoStepVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_step_verification);
    }
        public void goToSignIn(View v) {
            Intent intent = new Intent(TwoStepVerificationActivity.this, SignInActivity.class);
            startActivity(intent);
        }

    public void backArrow11 (View view) {
        Intent intent = new Intent(TwoStepVerificationActivity.this, EmailVerificationActivity.class);
        startActivity(intent);
    }

}