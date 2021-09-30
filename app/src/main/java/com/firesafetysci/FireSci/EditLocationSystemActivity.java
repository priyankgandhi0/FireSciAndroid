package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EditLocationSystemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location_system);
    }

    public void backArrow21 (View view) {
        Intent intent = new Intent(EditLocationSystemActivity.this, MyFireSciSystemActivity.class);
        startActivity(intent);
    }
    public void save_location(View view) {
        Toast.makeText(getApplicationContext(), "Location Edit Successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditLocationSystemActivity.this, MyFireSciSystemLocationActivity.class);
        startActivity(intent);
    }
}