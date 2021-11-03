package com.firesafetysci.FireSci.Installer;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.DeviceTypeAdapter;
import com.firesafetysci.FireSci.Main.System;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditSystemInstallerActivity extends AppCompatActivity implements DeviceTypeAdapter.OnDeviceTypeClickListener {
    public static System systemToEdit;

    private EditText deviceNameEditText;
    private RecyclerView deviceTypeRecyclerView;
    private Button saveChangesButton;

    private String selectedDeviceType = "";
    private ArrayList<String> deviceTypeArrayList;
    DividerItemDecoration decoration;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_system);

        initViews();
        setOnClickListeners();

        Toolbar editSystemInstallerActivityToolbar = findViewById(R.id.editSystemInstallerActivityToolbar);
        editSystemInstallerActivityToolbar.setTitle("");
        setSupportActionBar(editSystemInstallerActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setDefaultValues();

        deviceTypeArrayList = new ArrayList<>();
        deviceTypeArrayList.add("dar");
        deviceTypeArrayList.add("indi");
        deviceTypeArrayList.add("wind");
        populateRecyclerView(deviceTypeArrayList, systemToEdit.getSystemType());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        deviceNameEditText = findViewById(R.id.deviceNameEditTextEditSystemInstaller);
        deviceTypeRecyclerView = findViewById(R.id.recyclerViewEditSystemInstaller);
        saveChangesButton = findViewById(R.id.saveChangesButtonEditSystemInstaller);
        progressBar = findViewById(R.id.progressBarEditSystemInstaller);

        decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider)));
    }

    private void setDefaultValues() {
        selectedDeviceType = systemToEdit.getDeviceName();
        deviceNameEditText.setText(selectedDeviceType);
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
        saveChangesButton.setOnClickListener(v -> {
            String deviceName = deviceNameEditText.getText().toString().trim();

            if (deviceName.isEmpty()) {
                Snackbar.make(findViewById(R.id.saveChangesButtonEditSystemInstaller), "Please enter the Device Name and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(EditSystemInstallerActivity.this)) {
                Snackbar.make(findViewById(R.id.saveChangesButtonEditSystemInstaller), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                deviceNameEditText.clearFocus();
                editSystemInDatabase(systemToEdit.getSystemSerialNumber(), selectedDeviceType, deviceName);
            }
        });
    }

    private void editSystemInDatabase(String systemSerialNumber, String selectedDeviceType, String deviceName) {
        String URL = Uri.parse("http://firesafetysci.com/android_app/api/edit_system.php")
                .buildUpon()
                .appendQueryParameter("system_serial_number", systemSerialNumber)
                .appendQueryParameter("system_type", selectedDeviceType)
                .appendQueryParameter("device_name", deviceName)
                .build().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Toast.makeText(EditSystemInstallerActivity.this, "System Edited Successfully!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Snackbar.make(findViewById(R.id.saveChangesButtonEditSystemInstaller), "Failed! Please try again!!!", 1250)
                                    .setAction("Action", null)
                                    .setActionTextColor(Color.WHITE)
                                    .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.saveChangesButtonEditSystemInstaller), "Failed! Please try again!!!", 1250)
                            .setAction("Action", null)
                            .setActionTextColor(Color.WHITE)
                            .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                            .show();

                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getParams() {
                return new HashMap<>();
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onItemClick(int position) {
        String newSelectedDeviceType = deviceTypeArrayList.get(position);
        selectedDeviceType = newSelectedDeviceType;

        //Clear Recycler view
        deviceTypeArrayList.clear();
        deviceTypeRecyclerView.setAdapter(new DeviceTypeAdapter(deviceTypeArrayList, "", this, null));
        deviceTypeRecyclerView.removeItemDecoration(decoration);

        deviceTypeArrayList.add("dar");
        deviceTypeArrayList.add("indi");
        deviceTypeArrayList.add("wind");
        populateRecyclerView(deviceTypeArrayList, newSelectedDeviceType);
    }
}