package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
    }

    public void emailNext(View view) {
        Intent intent = new Intent(CreatePasswordActivity.this, PhoneNumberActivity.class);
        startActivity(intent);
    }
    public void backArrow6(View view) {
        Intent intent=new Intent(CreatePasswordActivity.this ,CompanyDetailsActivity.class);
        startActivity(intent);
        finish();
    }
}