package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CompanyDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
    }

    public void continue_location(View view) {
        Intent intent = new Intent(CompanyDetailsActivity.this, CreatePasswordActivity.class);
        startActivity(intent);
    }


    public void backArrow5(View view) {
        Intent intent=new Intent(CompanyDetailsActivity.this ,ChooseCountryActivity.class);
        startActivity(intent);
        finish();
    }

}