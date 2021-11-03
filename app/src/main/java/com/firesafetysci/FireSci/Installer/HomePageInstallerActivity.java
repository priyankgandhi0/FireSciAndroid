package com.firesafetysci.FireSci.Installer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firesafetysci.FireSci.AccountRegistration.RegisterOrSignInActivity;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

public class HomePageInstallerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private LinearLayout systemLocationsLinearLayout, addANewLocationLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_installer);

        initViews();
        setOnClickListeners();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.homeActivityInstallerToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        systemLocationsLinearLayout = findViewById(R.id.systemLocationsLinearLayout);
        addANewLocationLinearLayout = findViewById(R.id.addANewLocationLinearLayout);
    }

    private void setOnClickListeners() {
        systemLocationsLinearLayout.setOnClickListener(v -> {
            Intent intent2 = new Intent(HomePageInstallerActivity.this, LocationsInstallerActivity.class);
            startActivity(intent2);
        });

        addANewLocationLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageInstallerActivity.this, AddNewLocationInstallerActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                break;

            case R.id.nav_new_location:
                Intent intent = new Intent(HomePageInstallerActivity.this, AddNewLocationInstallerActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_my_locations:
                Intent intent2 = new Intent(HomePageInstallerActivity.this, LocationsInstallerActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_view_account:
                Intent intent3 = new Intent(HomePageInstallerActivity.this, AccountActivityInstaller.class);
                startActivity(intent3);
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(HomePageInstallerActivity.this)
                        .setTitle("LOG OUT")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            SharedPrefManager.getInstance(getApplicationContext()).setKeepMeSignedIn(false);
                            Intent intent4 = new Intent(HomePageInstallerActivity.this, RegisterOrSignInActivity.class);
                            startActivity(intent4);
                            finish();
                        })
                        .setNegativeButton("NO", null)
                        .show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}