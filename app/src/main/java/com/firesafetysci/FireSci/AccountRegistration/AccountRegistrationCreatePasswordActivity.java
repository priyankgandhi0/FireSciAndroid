package com.firesafetysci.FireSci.AccountRegistration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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

public class AccountRegistrationCreatePasswordActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button continueButton;
    private EditText passwordEditText, confirmPasswordEditText;
    private LinearLayout progressBar;
    private TextView passwordsDoNotMatchTextView, mustContainUpperLowerTextView;
    private boolean isPasswordChecked = false;
    private boolean isConfirmPasswordChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

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
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        continueButton = findViewById(R.id.continueButton);
        progressBar = findViewById(R.id.progressBarCreatePassword);
        passwordsDoNotMatchTextView = findViewById(R.id.passwordsDoNotMatchTextView);
        mustContainUpperLowerTextView = findViewById(R.id.mustContainUpperLowerTextView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            passwordsDoNotMatchTextView.setVisibility(View.GONE);
            mustContainUpperLowerTextView.setVisibility(View.GONE);

            if (password.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButton), "Please enter the password!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (confirmPassword.isEmpty()) {
                Snackbar.make(findViewById(R.id.continueButton), "Please confirm the password!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!password.equals(confirmPassword)) {
                passwordsDoNotMatchTextView.setVisibility(View.VISIBLE);

            } else if (!isPasswordValid(password)) {
                mustContainUpperLowerTextView.setVisibility(View.VISIBLE);

            } else if (!CommonFunctions.isNetworkConnected(AccountRegistrationCreatePasswordActivity.this)) {
                Snackbar.make(findViewById(R.id.continueButton), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                confirmPasswordEditText.clearFocus();
                setPasswordInDatabase(password);
            }
        });

        //Set Show Password Change Listener
        passwordEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    isPasswordChecked = !isPasswordChecked;
                    if (isPasswordChecked) {
                        showPassword(passwordEditText);
                    } else {
                        hidePassword(passwordEditText);
                    }
                    return true;
                }
            }
            return false;
        });

        //Set Show Password Change Listener
        confirmPasswordEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (confirmPasswordEditText.getRight() - confirmPasswordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    isConfirmPasswordChecked = !isConfirmPasswordChecked;
                    if (isConfirmPasswordChecked) {
                        showPassword(confirmPasswordEditText);
                    } else {
                        hidePassword(confirmPasswordEditText);
                    }
                    return true;
                }
            }
            return false;
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    void showPassword(EditText editText) {
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editText.setSelection(editText.getText().length());
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_visibility_off, 0);
    }

    void hidePassword(EditText editText) {
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setSelection(editText.getText().length());
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_visibility, 0);
    }

    private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}$");
    }

    private void setPasswordInDatabase(String password) {
        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/set_password.php?firesci_pin=" + fireSciPin + "&password=" + password;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Intent intent = new Intent(AccountRegistrationCreatePasswordActivity.this, AccountRegistrationPhoneNumberActivity.class);
                            startActivity(intent);

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