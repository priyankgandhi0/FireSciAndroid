package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddNewSystemDeviceNameActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_system_device_name_activty);
    }
    public void backArrow20(View view) {
        Intent intent = new Intent(AddNewSystemDeviceNameActivty.this, AddNewSystemChoseDeviceActivity.class);
        startActivity(intent);
    } public void Add_new_system(View view) {
        Toast.makeText(getApplicationContext(), "System Added Successfully \n AND SHOW DATA ON MAIN PAGE ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddNewSystemDeviceNameActivty.this, MyFireSciSystemActivity.class);
        startActivity(intent);
    }
}