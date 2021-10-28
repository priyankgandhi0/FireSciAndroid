package com.firesafetysci.FireSci.AccountRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.RequestHandler;
import com.firesafetysci.FireSci.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountRegistrationEmailVerificationActivity extends AppCompatActivity {
    private Button continueButton;
    private TextView emailVerificationTextView, tryAgainTextView, didntGetEmailTextView, clickOnLinkTextView;
    private LinearLayout progressBar;
    private Boolean isEmailVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        initViews();
        setOnClickListeners();

        Toolbar emailVerificationActivityToolbar = findViewById(R.id.emailVerificationActivityToolbar);
        emailVerificationActivityToolbar.setTitle("");
        setSupportActionBar(emailVerificationActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        checkEmailVerification(fireSciPin);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        continueButton = findViewById(R.id.continueButtonEmailVerification);
        progressBar = findViewById(R.id.progressBarEmailVerification);
        emailVerificationTextView = findViewById(R.id.emailVerificationTextView);
        tryAgainTextView = findViewById(R.id.tryAgainTextView);
        didntGetEmailTextView = findViewById(R.id.didntGetEmailTextView);
        clickOnLinkTextView = findViewById(R.id.clickOnLinkTextView);
    }

    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            if (isEmailVerified) {
                Intent intent = new Intent(AccountRegistrationEmailVerificationActivity.this, AccountRegistrationTwoStepVerificationActivity.class);
                startActivity(intent);

            } else {
                Snackbar.make(findViewById(R.id.continueButtonEmailVerification), "Email not verified!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();
            }
        });

        tryAgainTextView.setOnClickListener(v -> {
            String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
            checkEmailVerification(fireSciPin);
        });

        didntGetEmailTextView.setOnClickListener(v -> {
//            Intent intent = new Intent(AccountRegistrationEmailVerificationActivity.this, DidntGetEmailAActivity.class);
//            startActivity(intent);
        });
    }

    private void checkEmailVerification(String fireSciPin) {
        clickOnLinkTextView.setVisibility(View.GONE);
        tryAgainTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String URL = "http://firesafetysci.com/android_app/api/check_email_verification.php?firesci_pin=" + fireSciPin;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("verified")) {
                            isEmailVerified = true;
                            emailVerificationTextView.setText("Email Verified Successfully");
                            emailVerificationTextView.setTextColor(getResources().getColor(R.color.blue));

                        } else {
                            isEmailVerified = false;
                            emailVerificationTextView.setText("Email Not Verified");
                            emailVerificationTextView.setTextColor(getResources().getColor(R.color.red));

                            clickOnLinkTextView.setVisibility(View.VISIBLE);
                            tryAgainTextView.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.continueButtonEmailVerification), "Failed! Please try again!!!", 1250)
                            .setAction("Action", null)
                            .setActionTextColor(Color.WHITE)
                            .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                            .show();

                    isEmailVerified = false;
                    emailVerificationTextView.setText("Email Not Verified");
                    emailVerificationTextView.setTextColor(getResources().getColor(R.color.red));

                    clickOnLinkTextView.setVisibility(View.VISIBLE);
                    tryAgainTextView.setVisibility(View.VISIBLE);

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