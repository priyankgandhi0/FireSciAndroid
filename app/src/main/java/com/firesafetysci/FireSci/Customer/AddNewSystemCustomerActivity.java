package com.firesafetysci.FireSci.Customer;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.Location;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewSystemCustomerActivity extends AppCompatActivity {
    public static Location location;
    public static String enteredSerialNumber;
    public static String selectedDeviceType;

    private Button addSystemButton;
    private EditText deviceNameEditText, roomEditText, buildingEditText, floorEditText, descriptionEditText;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_system_customer);

        initViews();
        setOnClickListeners();

        Toolbar addNewSystemCustomerActivityToolbar = findViewById(R.id.addNewSystemCustomerActivityToolbar);
        addNewSystemCustomerActivityToolbar.setTitle("");
        setSupportActionBar(addNewSystemCustomerActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        deviceNameEditText = findViewById(R.id.deviceNameEditTextCus);
        roomEditText = findViewById(R.id.roomEditTextCus);
        buildingEditText = findViewById(R.id.buildingEditTextCus);
        floorEditText = findViewById(R.id.floorEditTextCus);
        descriptionEditText = findViewById(R.id.descriptionEditTextCus);
        addSystemButton = findViewById(R.id.addSystemButtonAddSysCus);
        progressBar = findViewById(R.id.progressBarAddNewSystemCustomer);
    }

    private void setOnClickListeners() {
        addSystemButton.setOnClickListener(v -> {
            String deviceName = deviceNameEditText.getText().toString().trim();
            String room = roomEditText.getText().toString().trim();
            String building = buildingEditText.getText().toString().trim();
            String floor = floorEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            if (deviceName.isEmpty() || description.isEmpty()) {
                Snackbar.make(findViewById(R.id.addSystemButtonAddSysCus), "Please enter the required fields and try again!\"", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(AddNewSystemCustomerActivity.this)) {
                Snackbar.make(findViewById(R.id.addSystemButtonAddSysCus), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                deviceNameEditText.clearFocus();
                addSystemInDatabase(enteredSerialNumber, selectedDeviceType,  location.getId(), deviceName, room, building, floor, description);
            }
        });
    }

    private void addSystemInDatabase(String enteredSerialNumber, String selectedDeviceType, int locationId, String deviceName, String room, String building, String floor, String description) {
        String uri = Uri.parse("http://firesafetysci.com/android_app/api/add_new_system.php?system_serial_number=")
                .buildUpon()
                .appendQueryParameter("system_serial_number", enteredSerialNumber)
                .appendQueryParameter("system_type", selectedDeviceType)
                .appendQueryParameter("device_name", deviceName)
                .appendQueryParameter("room", room)
                .appendQueryParameter("building", building)
                .appendQueryParameter("floor", floor)
                .appendQueryParameter("desc", description)
                .appendQueryParameter("location_id", String.valueOf(locationId))
                .build().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, uri,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Toast.makeText(AddNewSystemCustomerActivity.this, "System Added Successfully!", Toast.LENGTH_SHORT).show();
                            setResult(1); //This is to finish 3 activities in the stack
                            finish();

                        } else if (response.equals("system_exists")) {
                            Toast.makeText(AddNewSystemCustomerActivity.this,
                                    "System already exists! Try again with another serial number.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Snackbar.make(findViewById(R.id.addSystemButtonAddSysCus), "Failed! Please try again!!!", 1250)
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
                    error.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.addSystemButtonAddSysCus), "Failed! Please try again!!!", 1250)
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

}