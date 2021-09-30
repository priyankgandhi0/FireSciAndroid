package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void backArrow23 (View view) {
        Intent intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
        startActivity(intent);
    }
    public void change_password(View v) {
        Intent intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
        startActivity(intent);
    }
}