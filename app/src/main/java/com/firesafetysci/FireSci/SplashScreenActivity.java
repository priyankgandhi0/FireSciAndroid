package com.firesafetysci.FireSci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        int splashTime = 1500;
        handler.postDelayed(this::goToLoginActivity, splashTime);
    }

    private void goToLoginActivity() {
        startActivity(new Intent(SplashScreenActivity.this, RegisterOrSignInActivity.class));
        finish();
    }
}