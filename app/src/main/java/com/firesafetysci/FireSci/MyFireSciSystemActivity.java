package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyFireSciSystemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fire_sci_system);
    }

    public void backArrow17(View view) {
        Intent intent = new Intent(MyFireSciSystemActivity.this, MyFireSciSystemLocationActivity.class);
        startActivity(intent);
    }

    public void plus(View view) {
        Intent intent = new Intent(MyFireSciSystemActivity.this, AddNewSystemActivity.class);
        startActivity(intent);
    }

    public void editlocation(View view) {
        Intent intent = new Intent(MyFireSciSystemActivity.this, EditLocationSystemActivity.class);
        startActivity(intent);
    }
    public void Edit_Data(View view) {
        Intent intent = new Intent(MyFireSciSystemActivity.this, EditSystemActivity.class);
        startActivity(intent);
    }
}