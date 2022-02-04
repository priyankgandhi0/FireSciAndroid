package com.firesafetysci.FireSci.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firesafetysci.FireSci.Installer.AddNewLocationInstallerActivity;
import com.firesafetysci.FireSci.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class SystemDetailsActivity extends AppCompatActivity {
    private TextView serialNumberTextView, deviceNameTextView, roomTextView, buildingTextView, floorTextView, descriptionTextView;
    private Button backButton;
    private ImageView systemTypeImageView;
    public static System system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_details);

        initViews();
        setOnClickListeners();

        Toolbar systemDetailsActivityToolbar = findViewById(R.id.systemDetailsActivityToolbar);
        systemDetailsActivityToolbar.setTitle("");
        setSupportActionBar(systemDetailsActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setValuesInViews();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        serialNumberTextView = findViewById(R.id.serialNumberTextView);
        deviceNameTextView = findViewById(R.id.deviceNameTextView);
        roomTextView = findViewById(R.id.roomTextView);
        buildingTextView = findViewById(R.id.buildingTextView);
        floorTextView = findViewById(R.id.floorTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        backButton = findViewById(R.id.backButton);
        systemTypeImageView = findViewById(R.id.systemTypeImageView);
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void setValuesInViews() {
        //Setting the image
        String systemType = system.getSystemType();

        switch (systemType) {
            case "dar":
                systemTypeImageView.setImageResource(R.drawable.direct_fire_suppression);
                break;
            case "indi":
                systemTypeImageView.setImageResource(R.drawable.indirect_fire_suppression);
                break;
            default:
                systemTypeImageView.setImageResource(R.drawable.default_icon);
                break;
        }

        serialNumberTextView.setText(system.getSystemSerialNumber());
        deviceNameTextView.setText(system.getDeviceName());
        roomTextView.setText(system.getRoom());
        buildingTextView.setText(system.getBuilding());
        floorTextView.setText(system.getFloor());
        descriptionTextView.setText(system.getDescription());
    }
}