package com.firesafetysci.FireSci.AccountRegistration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.firesafetysci.FireSci.R;

public class ForgotPasswordVerificationCodeActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvResendCodeButton;
    private EditText verificationCodeEditText;
    private TextView tvResendCodeTimer;
    private Button continueButton;
    private int counter = 30;
    private CountDownTimer countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_verification_code);

        initViews();
        setOnClickListeners();

        countdown = new CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                tvResendCodeTimer.setText(counter + "s");
                counter--;
            }

            public void onFinish() {
                tvResendCodeTimer.setText("");
                tvResendCodeButton.setText(getString(R.string.resend));
                tvResendCodeButton.setTextColor(ContextCompat.getColor(ForgotPasswordVerificationCodeActivity.this, R.color.colorPrimary));
            }
        }.start();
    }


    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvResendCodeButton = findViewById(R.id.tvResendCodeButton);
        verificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        tvResendCodeTimer = findViewById(R.id.tvResendCodeTimer);
        continueButton = findViewById(R.id.continueButton);
    }

    private void setOnClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        tvResendCodeButton.setOnClickListener(v -> {
            counter = 30;
            tvResendCodeButton.setText(getString(R.string.resend_code));
            tvResendCodeButton.setTextColor(ContextCompat.getColor(ForgotPasswordVerificationCodeActivity.this, R.color.light_grey2));
            countdown.start();
        });

        continueButton.setOnClickListener(v -> {
            if (!verificationCodeEditText.getText().toString().trim().isEmpty()) {
                Intent intent = new Intent(ForgotPasswordVerificationCodeActivity.this, ForgotPasswordNewPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
