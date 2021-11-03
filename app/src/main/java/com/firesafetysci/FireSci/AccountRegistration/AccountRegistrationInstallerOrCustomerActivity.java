package com.firesafetysci.FireSci.AccountRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountRegistrationInstallerOrCustomerActivity extends AppCompatActivity {
    private Button installerButton, customerButton;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installer_or_customer);

        initViews();
        setOnClickListeners();

        Toolbar installerOrCustomerActivityToolbar = findViewById(R.id.installerOrCustomerActivityToolbar);
        installerOrCustomerActivityToolbar.setTitle("");
        setSupportActionBar(installerOrCustomerActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        installerButton = findViewById(R.id.installerButton);
        customerButton = findViewById(R.id.customerButton);
        progressBar = findViewById(R.id.progressBarInstallerOrCustomer);
    }


    private void setOnClickListeners() {
        installerButton.setOnClickListener(v -> {
            if (!CommonFunctions.isNetworkConnected(AccountRegistrationInstallerOrCustomerActivity.this)) {
                Snackbar.make(findViewById(R.id.installerButton), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();
            } else {
                performServerOperation(1);
            }
        });

        customerButton.setOnClickListener(v -> {
            if (!CommonFunctions.isNetworkConnected(AccountRegistrationInstallerOrCustomerActivity.this)) {
                Snackbar.make(findViewById(R.id.installerButton), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();
            } else {
                performServerOperation(2);
            }
        });
    }


    private void performServerOperation(int installerOrCustomer) {
        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/set_installer_or_customer.php?firesci_pin=" + fireSciPin + "&ins_or_cus=" + installerOrCustomer;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Intent intent = new Intent(AccountRegistrationInstallerOrCustomerActivity.this, AccountRegistrationChooseCountryActivity.class);
                            startActivity(intent);

                        } else {
                            Snackbar.make(findViewById(R.id.installerButton), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.installerButton), "Failed! Please try again!!!", 1250)
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