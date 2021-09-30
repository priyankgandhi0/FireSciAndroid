package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyFireSciSystemLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fire_sci_system_location);
    }
    public void backArrow15(View view) {
        Intent intent = new Intent(MyFireSciSystemLocationActivity.this, WellcomeActivity.class);
        startActivity(intent);
    }
    public void continue_locatio(View view) {
        Intent intent = new Intent(MyFireSciSystemLocationActivity.this, MyFireSciSystemActivity.class);
        startActivity(intent);
    }
}