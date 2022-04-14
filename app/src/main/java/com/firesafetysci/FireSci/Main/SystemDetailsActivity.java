package com.firesafetysci.FireSci.Main;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firesafetysci.FireSci.R;

public class SystemDetailsActivity extends AppCompatActivity {
    public static System system;
    private TextView serialNumberTextView, deviceNameTextView, roomTextView, buildingTextView, floorTextView, descriptionTextView;
    private ImageButton backButton;
    private ImageView systemTypeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_details);

        initViews();
        setOnClickListeners();

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
        backButton = findViewById(R.id.btnBack);
        systemTypeImageView = findViewById(R.id.systemTypeImageView);
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(v -> finish());
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