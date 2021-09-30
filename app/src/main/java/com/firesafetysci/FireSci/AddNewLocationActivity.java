package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddNewLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);

    }
    public void backArrow14 (View view) {
        Intent intent = new Intent(AddNewLocationActivity.this, WellcomeActivity.class);
        startActivity(intent);
    }
    public void Add_location(View view) {
        Toast.makeText(getApplicationContext(), "Location Added Successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddNewLocationActivity.this, WellcomeActivity.class);
        startActivity(intent);
    }
}