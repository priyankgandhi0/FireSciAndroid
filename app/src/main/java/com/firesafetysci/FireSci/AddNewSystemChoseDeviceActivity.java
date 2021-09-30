package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddNewSystemChoseDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_system_chose_device);
    }
    public void backArrow19(View view) {
        Intent intent = new Intent(AddNewSystemChoseDeviceActivity.this, AddNewSystemActivity.class);
        startActivity(intent);
    } public void continue_btn(View view) {
        Intent intent = new Intent(AddNewSystemChoseDeviceActivity.this, AddNewSystemDeviceNameActivty.class);
        startActivity(intent);
    }
}