package com.firesafetysci.FireSci.Installer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.Location;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.System;
import com.firesafetysci.FireSci.Main.SystemAdapter;
import com.firesafetysci.FireSci.Main.SystemDetailsActivity;
import com.firesafetysci.FireSci.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SystemsInstallerActivity extends AppCompatActivity
        implements SystemAdapter.OnEditButtonPressedListener, SystemAdapter.OnDeleteButtonPressedListener, SystemAdapter.OnSystemClickListener {
    public static Location location;
    String companyNameGlobal, cityGlobal, stateGlobal, addressGlobal, zipcodeGlobal, locationDescriptionGlobal,
            customerFiresciPinGlobal, selectedCountrySelectedCountryLabelEdit;
    private ImageButton btnBack;
    private RecyclerView systemsRecyclerViewInstaller;
    private FloatingActionButton addSystemImageButton;
    private ImageButton editLocationImageButton, deleteLocationImageButton;
    private ImageView noSystemsFoundImageView;
    private LinearLayout progressBar;
    private ArrayList<System> systemsArrayList;
    private TextView companyNameTextView, dateTextView, addressTextView;
    private int locationId;
    private SystemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systems_installer);

        initViews();
        setOnClickListeners();

        setDefaultValues();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        clearRecyclerView();

        if (!CommonFunctions.isNetworkConnected(SystemsInstallerActivity.this)) {
            Snackbar.make(findViewById(R.id.addSystemImageButtonInstaller), "Please connect to the internet!", 1250)
                    .setAction("Action", null)
                    .setActionTextColor(Color.WHITE)
                    .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                    .show();
        } else {
            getSystemsFromDatabase();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        systemsRecyclerViewInstaller = findViewById(R.id.systemsRecyclerViewInstaller);
        addSystemImageButton = findViewById(R.id.addSystemImageButtonInstaller);
        editLocationImageButton = findViewById(R.id.editLocationImageButtonInstaller);
        deleteLocationImageButton = findViewById(R.id.deleteLocationButtonInstaller);
        noSystemsFoundImageView = findViewById(R.id.noSystemsFoundImageView);
        progressBar = findViewById(R.id.progressBarSystemsInstaller);
        companyNameTextView = findViewById(R.id.companyTextViewSystemsInstaller);
        dateTextView = findViewById(R.id.dateTextViewInstaller);
        systemsArrayList = new ArrayList<>();
        addressTextView = findViewById(R.id.addressTextViewInstaller);
    }

    private void setOnClickListeners() {
        addSystemImageButton.setOnClickListener(v -> {
//            Intent intent = new Intent(SystemsInstallerActivity.this, AddSystemSerNoInstallerActivity.class);
//            startActivity(intent);
//            AddSystemSerNoInstallerActivity.location = location;

            LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.add_system_dialog_layout, null);
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setView(dialogView);
            EditText serialNumberEditText = dialogView.findViewById(R.id.serialNumberEditTextAddSysCus);
            TextView serNoNotRecognizedTextView = dialogView.findViewById(R.id.serialNumberNotRecognizedTextView);
            MaterialButton continueButton = dialogView.findViewById(R.id.continueButtonAddSysCus);
            dialogView.findViewById(R.id.btn_dialog).setOnClickListener(v1 -> dialog.dismiss());
            continueButton.setOnClickListener(v2 -> {
                String serialNumber = serialNumberEditText.getText().toString().trim();

                if (serialNumber.isEmpty()) {
                    Snackbar.make(findViewById(R.id.continueButtonAddSysIns), "Please enter the Serial Number and try again!", 1250)
                            .setAction("Action", null)
                            .setActionTextColor(Color.WHITE)
                            .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                            .show();

                } else if (!CommonFunctions.isNetworkConnected(SystemsInstallerActivity.this)) {
                    Snackbar.make(findViewById(R.id.continueButtonAddSysIns), "Please connect to the internet!", 1250)
                            .setAction("Action", null)
                            .setActionTextColor(Color.WHITE)
                            .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                            .show();

                } else {
                    serNoNotRecognizedTextView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    serialNumberEditText.clearFocus();
                    checkSerialNumberPinInDatabase(serialNumber, serialNumberEditText);
                }
            });

            dialog.show();
        });

        editLocationImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(SystemsInstallerActivity.this, EditLocationInstallerActivity.class);
            startActivity(intent);
            EditLocationInstallerActivity.locationToEdit = location;
        });

        deleteLocationImageButton.setOnClickListener(v -> new AlertDialog.Builder(SystemsInstallerActivity.this)
                .setTitle("Delete Location")
                .setMessage("Are you sure you want to delete this location?")
                .setPositiveButton("DELETE", (dialog, which) -> deleteLocationFromDatabase())
                .setNegativeButton("CANCEL", null)
                .show());

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setDefaultValues() {
        //Set Current Date in Label
        Date date = new Date();
        String dateFormat = "E, MMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        dateTextView.setText(simpleDateFormat.format(date));

        if (location != null) {
            locationId = location.getId();
            companyNameGlobal = location.getCompanyName();
            companyNameTextView.setText(companyNameGlobal);

            cityGlobal = location.getCity();
            stateGlobal = location.getStateProvince();
            addressGlobal = location.getAddress();
            zipcodeGlobal = location.getZipcode();
            locationDescriptionGlobal = location.getLocationDescription();
            customerFiresciPinGlobal = location.getCustomerFiresciPin();
            selectedCountrySelectedCountryLabelEdit = location.getCountry();
        }

        String fullAddress = String.format("%s, %s (%s)", addressGlobal, cityGlobal, zipcodeGlobal);
        addressTextView.setText(fullAddress);
    }

    private void getSystemsFromDatabase() {
        progressBar.setVisibility(View.VISIBLE);

        String URL = "http://firesafetysci.com/android_app/api/get_systems_by_location_id.php?location_id=" + locationId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    Log.i("Result", result);
                    progressBar.setVisibility(View.GONE);

                    if (result.isEmpty()) {
                        noSystemsFoundImageView.setVisibility(View.VISIBLE);
                        clearRecyclerView();

                    } else {
                        noSystemsFoundImageView.setVisibility(View.GONE);
                        systemsArrayList.clear();

                        try {
                            JSONArray jsonArray = new JSONArray(result);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                System system = new System();
                                system.setId(Integer.parseInt(jsonObject.getString("id")));
                                system.setSystemSerialNumber(jsonObject.getString("system_serial_number"));
                                system.setSystemType(jsonObject.getString("system_type"));
                                system.setDeviceName(jsonObject.getString("device_name"));
                                system.setRoom(jsonObject.getString("room"));
                                system.setBuilding(jsonObject.getString("building"));
                                system.setFloor(jsonObject.getString("floor"));
                                system.setDescription(jsonObject.getString("description"));
                                system.setLocationId(Integer.parseInt(jsonObject.getString("location_id")));

                                systemsArrayList.add(system);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        populateRecyclerView(systemsArrayList);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.addSystemImageButtonInstaller), "Failed! Please try again!!!", 1250)
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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void deleteLocationFromDatabase() {
        progressBar.setVisibility(View.VISIBLE);

        String URL = "http://firesafetysci.com/android_app/api/delete_location_by_id.php?id=" + locationId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    Log.i("Result", result);
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            finish();
                        }

                    } catch (Exception e) {
                        Snackbar.make(findViewById(R.id.addSystemImageButtonInstaller), "Failed! Please try again!!!", 1250)
                                .setAction("Action", null)
                                .setActionTextColor(Color.WHITE)
                                .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                                .show();

                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.addSystemImageButtonInstaller), "Failed! Please try again!!!", 1250)
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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void clearRecyclerView() {
        systemsArrayList.clear();
        systemsRecyclerViewInstaller.setAdapter(new SystemAdapter(systemsArrayList, this, null, null, null));
    }

    private void populateRecyclerView(List<System> systemList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        systemsRecyclerViewInstaller.setLayoutManager(layoutManager);
        adapter = new SystemAdapter(systemList, this, this, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        systemsRecyclerViewInstaller.setLayoutManager(linearLayoutManager);
        systemsRecyclerViewInstaller.setItemAnimator(new DefaultItemAnimator());
        systemsRecyclerViewInstaller.setAdapter(adapter);
    }

    private void checkSerialNumberPinInDatabase(String serialNumber, TextView serNoNotRecognizedTextView) {
        String URL = "http://firesafetysci.com/android_app/api/check_system_serial_number.php?serial_number="
                + serialNumber;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");
                        String doesSystemExist = jsonObject.getString("exists");

                        if (response.equals("found")) {
                            if (doesSystemExist.equals("yes")) {
                                Snackbar.make(findViewById(R.id.continueButtonAddSysIns), "System Already exists!", 1250)
                                        .setAction("Action", null)
                                        .setActionTextColor(Color.WHITE)
                                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                                        .show();
                            } else {
                                Intent intent = new Intent(SystemsInstallerActivity.this, AddSystemSelDeviceTypeInsActivity.class);
                                startActivityForResult(intent, 0);
                                AddSystemSelDeviceTypeInsActivity.location = location;
                                AddSystemSelDeviceTypeInsActivity.enteredSerialNumber = serialNumber;
                            }
                        } else {
                            serNoNotRecognizedTextView.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.continueButtonAddSysIns), "Failed! Please try again!!!", 1250)
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
    public void onEditClick(int position) {
        Intent intent = new Intent(SystemsInstallerActivity.this, EditSystemInstallerActivity.class);
        startActivity(intent);
        EditSystemInstallerActivity.systemToEdit = systemsArrayList.get(position);
    }

    @Override
    public void onDeleteClick(int position) {
        new AlertDialog.Builder(SystemsInstallerActivity.this)
                .setTitle("DELETE SYSTEM")
                .setMessage("Are you sure you want to delete this system?")
                .setPositiveButton("DELETE", (dialog, which) -> {
                    String URL = "http://firesafetysci.com/android_app/api/delete_system_by_serial_number.php?serial_number="
                            + systemsArrayList.get(position).getSystemSerialNumber();
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET, URL,
                            result -> {
                            }, Throwable::printStackTrace

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

                    if (adapter != null) {
                        adapter.removeItem(position);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SystemsInstallerActivity.this, SystemDetailsActivity.class);
        startActivity(intent);
        SystemDetailsActivity.system = systemsArrayList.get(position);
    }
}