package com.firesafetysci.FireSci.Installer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewLocationInstallerActivity extends AppCompatActivity {
    private EditText companyNameEditText, cityEditText, stateOrProvinceEditText, addressEditText,
            zipcodeEditText, locationDescriptionEditText, customerFireSciPinEditText;
    private Button continueButton;
    private LinearLayout progressBar;
    private CountryCodePicker countryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location_installer);

        initViews();
        setOnClickListeners();

        Toolbar addNewLocationActivityToolbar = findViewById(R.id.addNewLocationActivityToolbar);
        addNewLocationActivityToolbar.setTitle("");
        setSupportActionBar(addNewLocationActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        companyNameEditText = findViewById(R.id.companyEditTextAddNewLocation);
        cityEditText = findViewById(R.id.cityEditTextAddNewLocation);
        stateOrProvinceEditText = findViewById(R.id.stateEditTextAddNewLocation);
        addressEditText = findViewById(R.id.addressEditTextAddNewLocation);
        zipcodeEditText = findViewById(R.id.zipCodeEditTextAddNewLocation);
        locationDescriptionEditText = findViewById(R.id.locationDescriptionEditTextAddNewLocation);
        customerFireSciPinEditText = findViewById(R.id.customerFireSciPinEditTextAddNewLocation);
        continueButton = findViewById(R.id.continueButtonAddNewLocation);
        progressBar = findViewById(R.id.progressBarAddNewLocation);
        countryPicker = findViewById(R.id.countryPickerAddNewLocation);
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String companyName = companyNameEditText.getText().toString().trim();
            String city = cityEditText.getText().toString().trim();
            String state = stateOrProvinceEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String zipCode = zipcodeEditText.getText().toString().trim();
            String locationDescription = locationDescriptionEditText.getText().toString().trim();
            String customerFireSciPin = customerFireSciPinEditText.getText().toString().trim();

            if (companyName.isEmpty() || city.isEmpty() || state.isEmpty() || address.isEmpty() ||
                    zipCode.isEmpty() || locationDescription.isEmpty() || customerFireSciPin.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButtonAddNewLocation), "Please enter the fields and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(AddNewLocationInstallerActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButtonAddNewLocation), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                customerFireSciPinEditText.clearFocus();
                addLocationInDatabase(companyName, city, state, address, zipCode, locationDescription, customerFireSciPin);
            }
        });
    }

    private void addLocationInDatabase(String companyName, String city, String state, String address, String zipCode, String locationDescription, String customerFireSciPin) {
        String installerFireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/add_new_location.php?installer_firesci_pin=" + installerFireSciPin + "&customer_firesci_pin=" + customerFireSciPin +
                "&company_name=" + companyName + "&city=" + city + "&state_province=" + state + "&country=" + countryPicker.getSelectedCountryName() + "&address=" + address + "&zipcode=" + zipCode + "&location_description="
                + locationDescription;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Toast.makeText(AddNewLocationInstallerActivity.this, "Location Added Successfully!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Snackbar.make(findViewById(R.id.continueButtonAddNewLocation), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.continueButtonAddNewLocation), "Failed! Please try again!!!", 1250)
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


