package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterOrSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_sign_in);
    }

    public void clicked(View view) {
        Intent intent = new Intent(RegisterOrSignInActivity.this, SignInActivity.class);
        startActivity(intent);

    }

    public void register(View view) {
        Intent intent = new Intent(RegisterOrSignInActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void forgotPass(View view) {
        Intent intent = new Intent(RegisterOrSignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}