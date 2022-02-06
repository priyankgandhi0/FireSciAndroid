package com.firesafetysci.FireSci.AccountRegistration;

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
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private Button continueButton;
    private EditText fireSciPinEditText;
    private TextView pinNotRecognizedTextView;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setOnClickListeners();

        Toolbar registerActivityToolbar = findViewById(R.id.registerActivityToolbar);
        registerActivityToolbar.setTitle("");
        setSupportActionBar(registerActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String fireSciPin = fireSciPinEditText.getText().toString().trim();

            if (fireSciPin.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButtonRegister), "Please enter the FireSci Pin and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(RegisterActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButtonRegister), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (fireSciPin.length() != 5 && fireSciPin.length() != 6) {
                Snackbar.make(findViewById(R.id.continueButtonRegister), "Please enter 5 or 6 digits FireSci Pin and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                fireSciPinEditText.clearFocus();
                checkFireSciPinInDatabase(fireSciPin);
            }
        });
    }

    private void checkFireSciPinInDatabase(String fireSciPin) {
        String URL = "http://firesafetysci.com/android_app/api/check_firesci_pin.php?pin=" + fireSciPin;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("found")) {
                            SharedPrefManager.getInstance(getApplicationContext()).setFiresciPin(fireSciPin);
                            pinNotRecognizedTextView.setVisibility(View.GONE);

                            int insOrCus = -1;

                            if (fireSciPin.length() == 5) {
                                insOrCus = 1;
                            } else if (fireSciPin.length() == 6) {
                                insOrCus = 2;
                            }

                            installerOrCustomerServerOperation(insOrCus, fireSciPin);

                        } else {
                            pinNotRecognizedTextView.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.continueButtonRegister), "Failed! Please try again!!!", 1250)
                            .setAction("Action", null)
                            .setActionTextColor(Color.WHITE)
                            .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                            .show();

                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void initViews() {
        continueButton = findViewById(R.id.continueButtonRegister);
        fireSciPinEditText = findViewById(R.id.fireSciPinEditTextRegister);
        pinNotRecognizedTextView = findViewById(R.id.pinIsNotRecognizedTextView);
        progressBar = findViewById(R.id.progressBar);
    }


    private void installerOrCustomerServerOperation(int installerOrCustomer, String fireSciPin) {
        String URL = "http://firesafetysci.com/android_app/api/set_installer_or_customer.php?firesci_pin=" + fireSciPin + "&ins_or_cus=" + installerOrCustomer;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Intent intent = new Intent(RegisterActivity.this, AccountRegistrationNameActivity.class);
                            startActivity(intent);

                        } else {
                            Snackbar.make(findViewById(R.id.continueButtonRegister), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.continueButtonRegister), "Failed! Please try again!!!", 1250)
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