package com.firesafetysci.FireSci.Customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class AddSystemSerNoCustomerActivity extends AppCompatActivity {
    private EditText serialNumberEditText;
    private Button continueButton;
    private LinearLayout progressBar;
    private TextView serNoNotRecognizedTextView;

    public static Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_system_ser_no_customer);

        initViews();
        setOnClickListeners();

        Toolbar addSystemSerNoCustomerActivityToolbar = findViewById(R.id.addSystemSerNoCustomerActivityToolbar);
        addSystemSerNoCustomerActivityToolbar.setTitle("");
        setSupportActionBar(addSystemSerNoCustomerActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        serialNumberEditText = findViewById(R.id.serialNumberEditTextAddSysCus);
        continueButton = findViewById(R.id.continueButtonAddSysCus);
        continueButton = findViewById(R.id.continueButtonAddSysCus);
        progressBar = findViewById(R.id.progressBarAddSysSerNoCus);
        serNoNotRecognizedTextView = findViewById(R.id.serialNumberNotRecognizedTextView);
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String serialNumber = serialNumberEditText.getText().toString().trim();

            if (serialNumber.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButtonAddSysCus), "Please enter the Serial Number and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(AddSystemSerNoCustomerActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButtonAddSysCus), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                serNoNotRecognizedTextView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                serialNumberEditText.clearFocus();
                checkSerialNumberPinInDatabase(serialNumber);
            }
        });
    }

    private void checkSerialNumberPinInDatabase(String serialNumber) {
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
                                Snackbar.make(findViewById(R.id.continueButtonAddSysCus), "System Already exists!", 1250)
                                        .setAction("Action", null)
                                        .setActionTextColor(Color.WHITE)
                                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                                        .show();
                            } else {
                                Intent intent = new Intent(AddSystemSerNoCustomerActivity.this, AddSystemSelDeviceTypeCusActivity.class);
                                startActivityForResult(intent, 0);
                                AddSystemSelDeviceTypeCusActivity.location = location;
                                AddSystemSelDeviceTypeCusActivity.enteredSerialNumber = serialNumber;
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
                    Snackbar.make(findViewById(R.id.continueButtonAddSysCus), "Failed! Please try again!!!", 1250)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            finish();
        }
    }
}