package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AccountCreatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);
    }

    public void Verify_Successfully_Email (View view) {
        Intent intent = new Intent(AccountCreatedActivity.this, EmailVerificationActivity.class);
        startActivity(intent);
    }


    public void backArrow9 (View view) {
        Intent intent = new Intent(AccountCreatedActivity.this, EmailAddressActivity.class);
        startActivity(intent);
    }
}