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
import com.firesafetysci.FireSci.Main.Location;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditLocationInstallerActivity extends AppCompatActivity {
    private EditText companyNameEditText, cityEditText, stateOrProvinceEditText, addressEditText,
            zipcodeEditText, locationDescriptionEditText, customerFireSciPinEditText, customerFireSciPin2EditText,
            customerFireSciPin3EditText;
    private Button saveChangesButton;
    private LinearLayout progressBar;
    private CountryCodePicker countryPicker;

    public static Location locationToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location_installer);

        initViews();
        setOnClickListeners();

        Toolbar editLocationInsActivityToolbar = findViewById(R.id.editLocationInsActivityToolbar);
        editLocationInsActivityToolbar.setTitle("");
        setSupportActionBar(editLocationInsActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setDefaultValuesInViews();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        companyNameEditText = findViewById(R.id.companyNameEditTextEditLocationIns);
        cityEditText = findViewById(R.id.cityEditTextEditLocationIns);
        stateOrProvinceEditText = findViewById(R.id.stateEditTextEditLocationIns);
        addressEditText = findViewById(R.id.addressEditTextEditLocationIns);
        zipcodeEditText = findViewById(R.id.zipcodeEditTextEditLocationIns);
        locationDescriptionEditText = findViewById(R.id.locationDescriptionEditTextEditLocationIns);
        customerFireSciPinEditText = findViewById(R.id.customerFiresciPinEditTextEditLocationIns);
        customerFireSciPin2EditText = findViewById(R.id.customerFiresciPin2EditTextEditLocationIns);
        customerFireSciPin3EditText = findViewById(R.id.customerFiresciPin3EditTextEditLocationIns);
        saveChangesButton = findViewById(R.id.saveChangesButtonEditLocationInstaller);
        progressBar = findViewById(R.id.progressBarEditLocationInstaller);
        countryPicker = findViewById(R.id.countryPickerEditLocationInstaller);
    }

    private void setDefaultValuesInViews() {
        if (locationToEdit != null) {
            companyNameEditText.setText(locationToEdit.getCompanyName());
            cityEditText.setText(locationToEdit.getCity());
            stateOrProvinceEditText.setText(locationToEdit.getStateProvince());
            addressEditText.setText(locationToEdit.getAddress());
            zipcodeEditText.setText(locationToEdit.getZipcode());
            locationDescriptionEditText.setText(locationToEdit.getLocationDescription());
            customerFireSciPinEditText.setText(locationToEdit.getCustomerFiresciPin());
            customerFireSciPin2EditText.setText(locationToEdit.getCustomerFiresciPin2());
            customerFireSciPin3EditText.setText(locationToEdit.getCustomerFiresciPin3());
            countryPicker.setCountryForNameCode(getCountryCode(locationToEdit.getCountry()));
        }
    }

    public String getCountryCode(String countryName) {
        String[] isoCountryCodes = Locale.getISOCountries();
        for (String code : isoCountryCodes) {
            Locale locale = new Locale("", code);
            if (countryName.equalsIgnoreCase(locale.getDisplayCountry())) {
                return code;
            }
        }
        return "";
    }

    private void setOnClickListeners() {
        saveChangesButton.setOnClickListener(v -> {
            String companyName = companyNameEditText.getText().toString().trim();
            String city = cityEditText.getText().toString().trim();
            String state = stateOrProvinceEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String zipCode = zipcodeEditText.getText().toString().trim();
            String locationDescription = locationDescriptionEditText.getText().toString().trim();
            String customerFireSciPin = customerFireSciPinEditText.getText().toString().trim();
            String customerFireSciPin2 = customerFireSciPin2EditText.getText().toString().trim();
            String customerFireSciPin3 = customerFireSciPin3EditText.getText().toString().trim();

            if (companyName.isEmpty() || city.isEmpty() || state.isEmpty() || address.isEmpty() ||
                    zipCode.isEmpty() || locationDescription.isEmpty()) {
                Snackbar.make(findViewById(R.id.saveChangesButtonEditLocationInstaller), "Please enter the fields and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(EditLocationInstallerActivity.this)) {
                Snackbar.make(findViewById(R.id.saveChangesButtonEditLocationInstaller), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                customerFireSciPinEditText.clearFocus();
                addLocationInDatabase(companyName, city, state, address, zipCode, locationDescription, customerFireSciPin, customerFireSciPin2, customerFireSciPin3);
            }
        });
    }

    private void addLocationInDatabase(String companyName, String city, String state, String address, String zipCode, String locationDescription, String customerFireSciPin, String customerFireSciPin2, String customerFireSciPin3) {
        String URL = "http://firesafetysci.com/android_app/api/edit_location.php?location_id=" + locationToEdit.getId() + "&customer_firesci_pin=" + customerFireSciPin + "&customer_firesci_pin_2=" + customerFireSciPin2 + "&customer_firesci_pin_3=" + customerFireSciPin3 +
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
                            Toast.makeText(EditLocationInstallerActivity.this, "Location Edited Successfully!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Snackbar.make(findViewById(R.id.saveChangesButtonEditLocationInstaller), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.saveChangesButtonEditLocationInstaller), "Failed! Please try again!!!", 1250)
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