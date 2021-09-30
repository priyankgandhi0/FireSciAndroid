package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EditSystemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_system);
    }

    public void backArrow22 (View view) {
        Intent intent = new Intent(EditSystemActivity.this, MyFireSciSystemActivity.class);
        startActivity(intent);
    }
    public void save_Changes(View view) {
        Toast.makeText(getApplicationContext(), "System Edited Successfully!\n SHOW SERVER ROOM FLOOR \n WHICH WELL BE EDIT OR DELETE", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditSystemActivity.this, MyFireSciSystemActivity.class);
        startActivity(intent);
    }
}