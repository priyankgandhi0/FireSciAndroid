package com.firesafetysci.FireSci.AccountRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.CommonFunctions;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.RequestHandler;
import com.firesafetysci.FireSci.SharedPrefManager;
import com.firesafetysci.FireSci.SignInActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountRegistrationCompanyDetailsActivity extends AppCompatActivity {
    private EditText companyNameEditText, cityEditText, stateEditText, addressEditText, zipCodeEditText;
    private Button continueButton;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        initViews();
        setOnClickListeners();

        Toolbar accountDetailsActivityToolbar = findViewById(R.id.accountDetailsActivityToolbar);
        accountDetailsActivityToolbar.setTitle("");
        setSupportActionBar(accountDetailsActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        companyNameEditText = findViewById(R.id.companyNameEditText);
        cityEditText = findViewById(R.id.cityEditText);
        stateEditText = findViewById(R.id.stateEditText);
        addressEditText = findViewById(R.id.addressEditText);
        zipCodeEditText = findViewById(R.id.zipCodeEditText);
        continueButton = findViewById(R.id.continueButtonCompanyDetails);
        progressBar = findViewById(R.id.progressBarAccountDetails);
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String companyName = companyNameEditText.getText().toString().trim();
            String city = cityEditText.getText().toString().trim();
            String state = stateEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String zipCode = zipCodeEditText.getText().toString().trim();

            if (companyName.isEmpty() || city.isEmpty() || state.isEmpty() || address.isEmpty() || zipCode.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButtonCompanyDetails), "Please enter the fields and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(AccountRegistrationCompanyDetailsActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButtonCompanyDetails), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                zipCodeEditText.clearFocus();
                setCompanyDetailsInDatabase(companyName, city, state, address, zipCode);
            }
        });
    }

    private void setCompanyDetailsInDatabase(String companyName, String city, String state, String address, String zipCode) {
        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/set_company_details.php?firesci_pin=" + fireSciPin + "&company_name=" + companyName +
                "&city=" + city + "&stateProvince=" +
                "" + state + "&address=" + address + "&zipcode=" + zipCode;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Intent intent = new Intent(AccountRegistrationCompanyDetailsActivity.this, AccountRegistrationCreatePasswordActivity.class);
                            startActivity(intent);

                        } else {
                            Snackbar.make(findViewById(R.id.continueButtonCompanyDetails), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.continueButtonCompanyDetails), "Failed! Please try again!!!", 1250)
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