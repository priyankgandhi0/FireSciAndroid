package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddNewSystemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_system);
    }

    public void backArrow18 (View view) {
        Intent intent = new Intent(AddNewSystemActivity.this, MyFireSciSystemActivity.class);
        startActivity(intent);
    }
    public void continue_btn(View view) {

        Intent intent = new Intent(AddNewSystemActivity.this, AddNewSystemChoseDeviceActivity.class);
        startActivity(intent);
    }
}