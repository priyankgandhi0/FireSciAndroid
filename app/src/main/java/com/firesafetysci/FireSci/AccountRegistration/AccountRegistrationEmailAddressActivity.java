package com.firesafetysci.FireSci.AccountRegistration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.firesafetysci.FireSci.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountRegistrationEmailAddressActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private EditText emailAddressEditText, confirmEmailAddressEditText;
    private Button continueButton;
    private LinearLayout progressBar;
    private TextView emailsDoNotMatchTextView, enterValidEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_address);

        initViews();
        setOnClickListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        confirmEmailAddressEditText = findViewById(R.id.confirmEmailAddressEditText);
        continueButton = findViewById(R.id.continueButton);
        progressBar = findViewById(R.id.progressBarEmailAddress);
        emailsDoNotMatchTextView = findViewById(R.id.emailsDoNotMatchTextView);
        enterValidEmailTextView = findViewById(R.id.enterValidEmailTextView);
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String emailAddress = emailAddressEditText.getText().toString().trim();
            String confirmEmailAddress = confirmEmailAddressEditText.getText().toString().trim();

            emailsDoNotMatchTextView.setVisibility(View.GONE);
            enterValidEmailTextView.setVisibility(View.GONE);

            if (emailAddress.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButton), "Please enter the email!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (confirmEmailAddress.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButton), "Please confirm the email!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!emailAddress.equals(confirmEmailAddress)) {
                emailsDoNotMatchTextView.setVisibility(View.VISIBLE);

            } else if (!isEmailValid(emailAddress)) {
                enterValidEmailTextView.setVisibility(View.VISIBLE);

            } else if (!CommonFunctions.isNetworkConnected(AccountRegistrationEmailAddressActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButton), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                confirmEmailAddressEditText.clearFocus();
                setEmailInDatabase(emailAddress);
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private boolean isEmailValid(String emailAddress) {
        return emailAddress.matches("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}");
    }

    private void setEmailInDatabase(String emailAddress) {
        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/set_email.php?firesci_pin=" + fireSciPin + "&email=" + emailAddress;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            SharedPrefManager.getInstance(getApplicationContext()).setEmailAddress(emailAddress);
                            Intent intent = new Intent(AccountRegistrationEmailAddressActivity.this, AccountRegistrationAccountCreatedActivity.class);
                            startActivity(intent);

                        } else if (response.equals("exists")) {
                            Snackbar.make(findViewById(R.id.continueButton), "Email Address already taken!", 1250)
                                    .setAction("Action", null)
                                    .setActionTextColor(Color.WHITE)
                                    .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                                    .show();

                        } else {
                            Snackbar.make(findViewById(R.id.continueButton), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.continueButton), "Failed! Please try again!!!", 1250)
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