package com.firesafetysci.FireSci.Installer;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.DeviceTypeAdapter;
import com.firesafetysci.FireSci.Main.Location;
import com.firesafetysci.FireSci.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddSystemSelDeviceTypeInsActivity extends AppCompatActivity implements DeviceTypeAdapter.OnDeviceTypeClickListener {
    public static Location location;
    public static String enteredSerialNumber;

    private RecyclerView deviceTypeRecyclerView;
    private Button continueButton;

    private String selectedDeviceType = "";
    private ArrayList<String> deviceTypeArrayList;
    DividerItemDecoration decoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_system_sel_device_ins);

        initViews();
        setOnClickListeners();

        Toolbar addSystemDevTypeInstallerActivityToolbar = findViewById(R.id.addSystemDevTypeInstallerActivityToolbar);
        addSystemDevTypeInstallerActivityToolbar.setTitle("");
        setSupportActionBar(addSystemDevTypeInstallerActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Set initially none selected
        deviceTypeArrayList = new ArrayList<>();
        deviceTypeArrayList.add("dar");
        deviceTypeArrayList.add("indi");
        deviceTypeArrayList.add("wind");
        populateRecyclerView(deviceTypeArrayList, "");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        deviceTypeRecyclerView = findViewById(R.id.recyclerViewAddSysSelDevInstaller);
        continueButton = findViewById(R.id.continueButtonAddSysSelDevIns);

        decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider)));
    }

    private void populateRecyclerView(List<String> deviceTypeArrayList, String selectedDeviceType) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        deviceTypeRecyclerView.setLayoutManager(layoutManager);
        DeviceTypeAdapter adapter = new DeviceTypeAdapter(deviceTypeArrayList, selectedDeviceType, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        deviceTypeRecyclerView.setLayoutManager(linearLayoutManager);
        deviceTypeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        deviceTypeRecyclerView.setAdapter(adapter);
        deviceTypeRecyclerView.addItemDecoration(decoration);
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {

            if (selectedDeviceType.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButtonAddSysSelDevIns), "Please select the device type and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(AddSystemSelDeviceTypeInsActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButtonAddSysSelDevIns), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                Intent intent = new Intent(AddSystemSelDeviceTypeInsActivity.this, AddNewSystemInstallerActivity.class);
                startActivityForResult(intent, 1);
                AddNewSystemInstallerActivity.location = location;
                AddNewSystemInstallerActivity.enteredSerialNumber = enteredSerialNumber;
                AddNewSystemInstallerActivity.selectedDeviceType = selectedDeviceType;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            setResult(0);
            finish();
        }
    }

    @Override
    public void onItemClick(int position) {
        String newSelectedDeviceType = deviceTypeArrayList.get(position);
        selectedDeviceType = newSelectedDeviceType;

        //Clear Recycler view
        deviceTypeArrayList.clear();
        deviceTypeRecyclerView.setAdapter(new DeviceTypeAdapter(deviceTypeArrayList, "newSelectedDeviceType", this, null));
        deviceTypeRecyclerView.removeItemDecoration(decoration);

        deviceTypeArrayList.add("dar");
        deviceTypeArrayList.add("indi");
        deviceTypeArrayList.add("wind");
        populateRecyclerView(deviceTypeArrayList, newSelectedDeviceType);
    }
}