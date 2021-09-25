package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PhoneNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
    }

    public void verifyEmailBtn(View view) {
        Intent intent = new Intent(PhoneNumberActivity.this, EmailAddressActivity.class);
        startActivity(intent);
    }
}