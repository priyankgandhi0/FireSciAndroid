package com.firesafetysci.FireSci.Main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.AccountRegistration.ForgotPasswordActivity;
import com.firesafetysci.FireSci.Customer.HomePageCustomerActivity;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Installer.HomePageInstallerActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private EditText fireSciPinEmailEditText, passwordEditText;
    private TextView forgotPasswordTextView;
    private CheckBox keepMeSignedInCheckBox;
    private CheckBox showPasswordCheckBox;
    private Button signInButton;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();
        setOnClickListeners();

        Toolbar signInActivityToolbar = findViewById(R.id.signInActivityToolbar);
        signInActivityToolbar.setTitle("");
        setSupportActionBar(signInActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void showPassword() {
        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    void hidePassword() {
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        fireSciPinEmailEditText = findViewById(R.id.emailEditTextSignIn);
        passwordEditText = findViewById(R.id.passwordEditTextSignIn);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextViewSignIn);
        keepMeSignedInCheckBox = findViewById(R.id.keepMeSignedInCheckBox);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        signInButton = findViewById(R.id.signInButton);
        progressBar = findViewById(R.id.progressBarSignIn);
    }

    private void setOnClickListeners() {
        signInButton.setOnClickListener(v -> {
            String fireSciPinEmail = fireSciPinEmailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (fireSciPinEmail.isEmpty() || password.isEmpty()) {
                Snackbar.make(findViewById(R.id.signInButton), "Please enter the fields and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(SignInActivity.this)) {
                Snackbar.make(findViewById(R.id.signInButton), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                passwordEditText.clearFocus();
                sigIn(fireSciPinEmail, password);
            }
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        //Set Show Password Change Listener
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showPassword();
            } else {
                hidePassword();
            }
        });
    }

    private void sigIn(String fireSciPinEmail, String password) {
        String URL = "http://firesafetysci.com/android_app/api/sign_in_user.php?pin_or_email=" + fireSciPinEmail + "&password=" + password;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("found")) {
                            String fireSciPin = jsonObject.getString("firesci_pin");
                            String firstName = jsonObject.getString("first_name");
                            String lastName = jsonObject.getString("last_name");
                            String email = jsonObject.getString("email");
                            int installerOrCustomer = jsonObject.getInt("installer_or_customer");

                            SharedPrefManager.getInstance(getApplicationContext()).setFiresciPin(fireSciPin);
                            SharedPrefManager.getInstance(getApplicationContext()).setFirstName(firstName);
                            SharedPrefManager.getInstance(getApplicationContext()).setLastName(lastName);
                            SharedPrefManager.getInstance(getApplicationContext()).setEmailAddress(email);
                            SharedPrefManager.getInstance(getApplicationContext()).setInstallerOrCustomer(installerOrCustomer);
                            SharedPrefManager.getInstance(getApplicationContext()).setKeepMeSignedIn(keepMeSignedInCheckBox.isChecked());

                            Intent intent;
                            if (installerOrCustomer == 1) {
                                intent = new Intent(SignInActivity.this, HomePageInstallerActivity.class);
                            } else {
                                intent = new Intent(SignInActivity.this, HomePageCustomerActivity.class);
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                            finish();
                            
                        } else {
                            Snackbar.make(findViewById(R.id.signInButton), "Email/FireSci Pin or Password incorrect!", 1250)
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
                    Snackbar.make(findViewById(R.id.signInButton), "Failed! Please try again!!!", 1250)
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

