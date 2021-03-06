package com.firesafetysci.FireSci.Installer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.AccountRegistration.RegisterOrSignInActivity;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.firesafetysci.FireSci.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivityInstaller extends AppCompatActivity {
    String firstNameGlobal = "", lastNameGlobal = "", companyNameGlobal = "", emailGlobal = "", phoneNumberGlobal = "";
    private ImageButton btnBack;
    private EditText nameEditText, lastNameEditText, companyNameEditText, emailEditText, phoneNumberEditText;
    private Button saveChangesButton;
    private LinearLayout progressBar;
    private TextView changeYourPasswordTextView, logoutTextView, pinTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_installer);

        initViews();
        setOnClickListeners();

        if (!CommonFunctions.isNetworkConnected(AccountActivityInstaller.this)) {
            Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Please connect to the internet!", 1250)
                    .setAction("Action", null)
                    .setActionTextColor(Color.WHITE)
                    .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                    .show();
        } else {
            getUserDataFromDatabase();
        }

        pinTextView.setText(SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        nameEditText = findViewById(R.id.nameEditTextAccountInstaller);
        lastNameEditText = findViewById(R.id.lastNameEditTextAccountInstaller);
        companyNameEditText = findViewById(R.id.companyNameEditTextAccountInstaller);
        emailEditText = findViewById(R.id.emailEditTextAccountInstaller);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditTextAccountInstaller);
        saveChangesButton = findViewById(R.id.saveChangesButtonAccountInstaller);
        progressBar = findViewById(R.id.progressBarAccountDetails);
        changeYourPasswordTextView = findViewById(R.id.changeYourPasswordTextViewAccountInstaller);
        logoutTextView = findViewById(R.id.logoutTextViewAccountInstaller);
        pinTextView = findViewById(R.id.pinTextViewInstaller);
    }

    private void setOnClickListeners() {
        saveChangesButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String companyName = companyNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();

            if (name.isEmpty() || lastName.isEmpty() || companyName.isEmpty() || email.isEmpty()) {
                Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Please enter the fields and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(AccountActivityInstaller.this)) {
                Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (name.equals(firstNameGlobal) && lastName.equals(lastNameGlobal) && companyName.equals(companyNameGlobal) && email.equals(emailGlobal) && phoneNumber.equals(phoneNumberGlobal)) {
                Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "No Changes made to the user details!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!isEmailValid(email)) {
                Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Email not valid!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                phoneNumberEditText.clearFocus();
                editUserDetails(name, lastName, companyName, email, phoneNumber);
            }
        });

        changeYourPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivityInstaller.this, ChangeYourPasswordInstallerActivity.class);
            startActivity(intent);
        });

        logoutTextView.setOnClickListener(v -> new AlertDialog.Builder(AccountActivityInstaller.this)
                .setTitle("LOG OUT")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("YES", (dialog, which) -> {
                    SharedPrefManager.getInstance(getApplicationContext()).setKeepMeSignedIn(false);

                    Intent intent = new Intent(AccountActivityInstaller.this, RegisterOrSignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                    finish();
                })
                .setNegativeButton("NO", null)
                .show());

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    //Get user data from database
    private void getUserDataFromDatabase() {
        progressBar.setVisibility(View.VISIBLE);

        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/get_user_data_by_firesci_pin.php?firesci_pin=" + fireSciPin;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("found")) {
                            String firstName = jsonObject.getString("first_name");
                            String lastName = jsonObject.getString("last_name");
                            String companyName = jsonObject.getString("company_name");
                            String email = jsonObject.getString("email");
                            String phoneNumber = jsonObject.getString("phone");

                            setDefaultValuesInViews(firstName, lastName, companyName, email, phoneNumber);

                        } else {
                            Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Failed! Please try again!!!", 1250)
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

    // Set Default Values
    private void setDefaultValuesInViews(String firstName, String lastName, String companyName, String email, String phoneNumber) {
        nameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        companyNameEditText.setText(companyName);
        emailEditText.setText(email);
        phoneNumberEditText.setText(phoneNumber);

        this.firstNameGlobal = firstName;
        this.lastNameGlobal = lastName;
        this.companyNameGlobal = companyName;
        this.emailGlobal = email;
        this.phoneNumberGlobal = phoneNumber;
    }

    // Edit user details in database
    private void editUserDetails(String firstName, String lastName, String companyName, String email, String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);

        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/edit_user_data_by_firesci_pin.php?firesci_pin=" + fireSciPin + "&first_name=" + firstName + "&last_name=" + lastName +
                "&company_name=" + companyName + "&email=" + email + "&phone_number=" + phoneNumber;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Toast.makeText(AccountActivityInstaller.this, "User Details Updated!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.saveChangesButtonAccountInstaller), "Failed! Please try again!!!", 1250)
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

    private boolean isEmailValid(String emailAddress) {
        return emailAddress.matches("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}");
    }
}