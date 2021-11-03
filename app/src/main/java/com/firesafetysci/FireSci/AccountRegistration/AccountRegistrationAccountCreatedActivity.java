package com.firesafetysci.FireSci.AccountRegistration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountRegistrationAccountCreatedActivity extends AppCompatActivity {
    private Button continueButtonAccountCreated;
    private TextView emailSentTextView, emailNotSentTextView;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);

        initViews();
        setOnClickListeners();

        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String email = SharedPrefManager.getInstance(getApplicationContext()).getEmailAddress();
        String firstName = SharedPrefManager.getInstance(getApplicationContext()).getFirstName();

        sendVerificationCodeEmail(fireSciPin, email, firstName);

        Toolbar accountCreatedActivityToolbar = findViewById(R.id.accountCreatedActivityToolbar);
        accountCreatedActivityToolbar.setTitle("");
        setSupportActionBar(accountCreatedActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        continueButtonAccountCreated = findViewById(R.id.continueButtonAccountCreated);
        emailSentTextView = findViewById(R.id.emailSentTextView);
        emailNotSentTextView = findViewById(R.id.emailNotSentTextView);
        progressBar = findViewById(R.id.progressBarAccountCreated);
    }

    private void setOnClickListeners() {
        continueButtonAccountCreated.setOnClickListener(v -> {
            Intent intent = new Intent(AccountRegistrationAccountCreatedActivity.this, AccountRegistrationEmailVerificationActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            finish();
        }
    }

    private void sendVerificationCodeEmail(String fireSciPin, String email, String firstName) {
        progressBar.setVisibility(View.VISIBLE);

        String URL = "http://firesafetysci.com/android_app/api/send_verification_code.php?firesci_pin=" + fireSciPin + "&email=" + email + "&last_name=" + firstName;

        emailSentTextView.setVisibility(View.GONE);
        emailNotSentTextView.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("success")) {
                            emailSentTextView.setVisibility(View.VISIBLE);

                        } else {
                            emailNotSentTextView.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    emailNotSentTextView.setVisibility(View.VISIBLE);
                    Snackbar.make(findViewById(R.id.continueButtonAccountCreated), "Failed! Please try again!!!", 1250)
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