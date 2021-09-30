package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EmailAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_address);
    }

    public void phoneNext(View view) {
        Intent intent = new Intent(EmailAddressActivity.this, AccountCreatedActivity.class);
        startActivity(intent);
    }
    public void backArrow8(View view) {
        Intent intent=new Intent(EmailAddressActivity.this ,PhoneNumberActivity.class);
        startActivity(intent);
        finish();
    }
}