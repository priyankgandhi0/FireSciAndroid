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

    public void goToMain(View view) {
        Intent intent = new Intent(AccountCreatedActivity.this, RegisterOrSignInActivity.class);
        startActivity(intent);
    }
}