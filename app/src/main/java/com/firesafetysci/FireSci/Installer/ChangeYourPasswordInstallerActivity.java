package com.firesafetysci.FireSci.Installer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangeYourPasswordInstallerActivity extends AppCompatActivity {
    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton;
    private LinearLayout progressBar;
    private TextView notMatchTextViewChangePassword, mustContainTextViewChangePassword, oldPasswordIncorrectTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_installer);

        initViews();
        setOnClickListeners();

        Toolbar changePasswordActivityToolbar = findViewById(R.id.changePasswordActivityToolbar);
        changePasswordActivityToolbar.setTitle("");
        setSupportActionBar(changePasswordActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        progressBar = findViewById(R.id.progressBarChangePassword);
        notMatchTextViewChangePassword = findViewById(R.id.notMatchTextViewChangePassword);
        mustContainTextViewChangePassword = findViewById(R.id.mustContainTextViewChangePassword);
        oldPasswordIncorrectTextView = findViewById(R.id.oldPasswordIncorrectTextView);
    }

    private void setOnClickListeners() {
        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            notMatchTextViewChangePassword.setVisibility(View.GONE);
            mustContainTextViewChangePassword.setVisibility(View.GONE);
            oldPasswordIncorrectTextView.setVisibility(View.GONE);

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Snackbar.make(findViewById(R.id.changePasswordButton), "Please enter the fields and try again!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!CommonFunctions.isNetworkConnected(ChangeYourPasswordInstallerActivity.this)) {
                Snackbar.make(findViewById(R.id.changePasswordButton), "Please connect to the internet!", 1250)
                        .setAction("Action", null)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                        .show();

            } else if (!newPassword.equals(confirmPassword)) {
                notMatchTextViewChangePassword.setVisibility(View.VISIBLE);

            } else if (!isPasswordValid(newPassword)) {
                mustContainTextViewChangePassword.setVisibility(View.VISIBLE);

            } else {
                progressBar.setVisibility(View.VISIBLE);
                confirmPasswordEditText.clearFocus();
                changePasswordInDatabase(oldPassword, newPassword);
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}$");
    }

    //Change Password in Database
    private void changePasswordInDatabase(String oldPassword, String newPassword) {
        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/change_password.php?firesci_pin=" + fireSciPin +
                "&old_password=" + oldPassword + "&new_password=" + newPassword;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String response = jsonObject.getString("response");

                        if (response.equals("successful")) {
                            Toast.makeText(ChangeYourPasswordInstallerActivity.this, " Password Updated Successfully!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else if (response.equals("pass_inc")) {
                            oldPasswordIncorrectTextView.setVisibility(View.VISIBLE);

                        } else {
                            Snackbar.make(findViewById(R.id.changePasswordButton), "Failed! Please try again!!!", 1250)
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
                    Snackbar.make(findViewById(R.id.changePasswordButton), "Failed! Please try again!!!", 1250)
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