package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private Button continueButton;
    private EditText fireSciPinEditText;
  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setOnClickListeners();

    }
    public void backArrow1(View view) {
     Intent intent=new Intent(RegisterActivity.this ,RegisterOrSignInActivity.class);
     startActivity(intent);
     finish();
    }



    private void setOnClickListeners() {
        continueButton.setOnClickListener(v -> {
            String fireSciPin = fireSciPinEditText.getText().toString().trim();
            if (fireSciPin.isEmpty()) {
                Toast.makeText(this, "Please enter the FireSci Pin and try again!", Toast.LENGTH_SHORT).show();

            } else if (!CommonFunctions.isNetworkConnected(RegisterActivity.this)) {
                Toast.makeText(this, "Please connect to the internet!", Toast.LENGTH_SHORT).show();

            } else {
                checkFireSciPinInDatabase(fireSciPin);
            }
        });
    }

    private void checkFireSciPinInDatabase(String fireSciPin) {
        Intent intent = new Intent(RegisterActivity.this, NameActivity.class);
        startActivity(intent);
    }


    private void initViews() {
        continueButton = findViewById(R.id.continueButtonRegister);
        fireSciPinEditText = findViewById(R.id.fireSciPinEditTextRegister);
        getApplicationContext();
    }

}