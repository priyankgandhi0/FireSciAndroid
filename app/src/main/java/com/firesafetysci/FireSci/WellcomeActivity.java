package com.firesafetysci.FireSci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class WellcomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        /*-------------HooKs-------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        /*--------------Tool bar---------------*/
        setSupportActionBar(toolbar);
        /*--------------Navigation Drawer Menu---------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }


    public void AddNewLocation(View v) {
        Intent intent = new Intent(WellcomeActivity.this, AddNewLocationActivity.class);
        startActivity(intent);
    }

    public void add_new_loaction(View v) {
        Intent intent = new Intent(WellcomeActivity.this, AddNewLocationActivity.class);
        startActivity(intent);
    }

    public void my_fireSci_system_loaction(View v) {
        Intent intent = new Intent(WellcomeActivity.this, MyFireSciSystemLocationActivity.class);
        startActivity(intent);
    }

    public void view_Account(View v) {
        Intent intent = new Intent(WellcomeActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.nav_new_location:
                Intent intent = new Intent(WellcomeActivity.this, AddNewLocationActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_My_FireSci_System_Location:
                Toast.makeText(getApplicationContext(), "Fire", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(WellcomeActivity.this, MyFireSciSystemLocationActivity.class);
                startActivity(i);
                break;
            case R.id.nav_View_account:
                Toast.makeText(getApplicationContext(), "View Account", Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(WellcomeActivity.this,AccountActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
                alertDialog();

                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void alertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("LOG OUT")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(Html.fromHtml("<font color='#2D71C3'>YES</font>"), (dialog, which) -> {
                    Intent i2 = new Intent(WellcomeActivity.this,RegisterOrSignInActivity.class);
                    startActivity(i2);
                })
                .setNegativeButton(Html.fromHtml("<font color='#BF0000'>NO</font>"), (dialog, which) -> dialog.dismiss())
                .show();

    }
}