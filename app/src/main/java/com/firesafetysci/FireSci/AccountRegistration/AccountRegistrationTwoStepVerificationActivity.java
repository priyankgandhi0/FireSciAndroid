package com.firesafetysci.FireSci.AccountRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firesafetysci.FireSci.CommonFunctions;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.SignInActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AccountRegistrationTwoStepVerificationActivity extends AppCompatActivity {
    private TextView skipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_step_verification);

        initViews();
        setOnClickListeners();

        Toolbar twoStepVerificationActivityToolbar = findViewById(R.id.twoStepVerificationActivityToolbar);
        twoStepVerificationActivityToolbar.setTitle("");
        setSupportActionBar(twoStepVerificationActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        skipTextView = findViewById(R.id.skipTextView);
    }


    private void setOnClickListeners() {
        skipTextView.setOnClickListener(v -> {
            //Go to Sign in or Register Activity
            Intent intent = new Intent(AccountRegistrationTwoStepVerificationActivity.this, RegisterOrSignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}