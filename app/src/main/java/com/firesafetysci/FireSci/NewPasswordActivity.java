package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
    }

    public void resetPassword(View view) {
        Intent intent = new Intent(NewPasswordActivity.this, RegisterOrSignInActivity.class);
        startActivity(intent);
    }
}