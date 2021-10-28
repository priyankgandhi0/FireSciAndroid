package com.firesafetysci.FireSci;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.firesafetysci.FireSci.AccountRegistration.RegisterOrSignInActivity;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }
    public void backArrow16 (View view) {
        Intent intent = new Intent(AccountActivity.this, WellcomeActivity.class);
        startActivity(intent);
    }
    public void view_Account(View v) {
        Toast.makeText(getApplicationContext(), "User Details Updated!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AccountActivity.this, WellcomeActivity.class);
        startActivity(intent);
    }

    public void change_password(View v) {
        Intent intent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void log_Out(View v) {
        alertDialog();
    }

    private void alertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("LOG OUT")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(Html.fromHtml("<font color='#2D71C3'>YES</font>"), (dialog, which) -> {
                    Intent i2 = new Intent(AccountActivity.this, RegisterOrSignInActivity.class);
                    startActivity(i2);
                })
                .setNegativeButton(Html.fromHtml("<font color='#BF0000'>NO</font>"), (dialog, which) -> dialog.dismiss())
                .show();

    }


}