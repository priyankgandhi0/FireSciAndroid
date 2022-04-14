package com.firesafetysci.FireSci.Installer;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.AccountRegistration.RegisterOrSignInActivity;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.ContactActivity;
import com.firesafetysci.FireSci.Main.Location;
import com.firesafetysci.FireSci.Main.LocationAdapter;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.firesafetysci.FireSci.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class HomePageInstallerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationAdapter.OnLocationClickListener {
    private ImageButton btnNav;
    private ImageButton btnInfo;
    private AdvanceDrawerLayout drawerLayout;
    private TextView welcomeTextView;
    private TextView navHomeTextView, navNewLocationTextView, navViewAccountTextView, navContactTextView, navLogoutTextView;
    private FloatingActionButton addANewLocation;
    private RecyclerView locationsRecyclerViewInstaller;
    private ImageView noLocationsFoundImageView;
    private LinearLayout progressBar;
    private ArrayList<Location> locationsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_installer);

        initViews();
        setOnClickListeners();
        
        String firstName = SharedPrefManager.getInstance(getApplicationContext()).getFirstName();
        welcomeTextView.setText((String.format(Locale.US, "Welcome, %s!", firstName)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        clearRecyclerView();

        if (!CommonFunctions.isNetworkConnected(HomePageInstallerActivity.this)) {
            Snackbar.make(findViewById(R.id.addLocationImageButton), "Please connect to the internet!", 1250)
                    .setAction("Action", null)
                    .setActionTextColor(Color.WHITE)
                    .setBackgroundTint(getResources().getColor(R.color.snackbarColor))
                    .show();
        } else {
            getLocationsFromDatabase();
        }
    }

    private void initViews() {
        btnNav = findViewById(R.id.btnNav);
        btnInfo = findViewById(R.id.btnInfo);
        drawerLayout = findViewById(R.id.drawer_layout);
        locationsRecyclerViewInstaller = findViewById(R.id.locationsRecyclerViewInstaller);
        progressBar = findViewById(R.id.progressBarLocationsInstaller);
        noLocationsFoundImageView = findViewById(R.id.noLocationsFoundImageView);
        welcomeTextView = findViewById(R.id.welcomeTextViewInstaller);
        navHomeTextView = findViewById(R.id.navHomeTextView);
        navNewLocationTextView = findViewById(R.id.navNewLocationTextView);
        navViewAccountTextView = findViewById(R.id.navViewAccountTextView);
        navContactTextView = findViewById(R.id.navContactTextView);
        navLogoutTextView = findViewById(R.id.navLogoutTextView);
        addANewLocation = findViewById(R.id.addANewLocation);

        drawerLayout.setViewScale(Gravity.START, 0.9f);
        drawerLayout.setRadius(Gravity.START, 35f);
        drawerLayout.setViewElevation(Gravity.START, 20f);

        locationsArrayList = new ArrayList<>();
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void setOnClickListeners() {
        btnNav.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        btnInfo.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.info_dialog_layout, null);
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setView(dialogView);
            dialogView.findViewById(R.id.btn_dialog).setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
        });

        navHomeTextView.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));

        navNewLocationTextView.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageInstallerActivity.this, AddNewLocationInstallerActivity.class);
            startActivity(intent);
        });

        navViewAccountTextView.setOnClickListener(v -> {
            Intent intent3 = new Intent(HomePageInstallerActivity.this, AccountActivityInstaller.class);
            startActivity(intent3);
        });

        navContactTextView.setOnClickListener(v -> {
            Intent intent4 = new Intent(HomePageInstallerActivity.this, ContactActivity.class);
            startActivity(intent4);
        });

        navLogoutTextView.setOnClickListener(v -> new AlertDialog.Builder(HomePageInstallerActivity.this)
                .setTitle("LOG OUT")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("YES", (dialog, which) -> {
                    SharedPrefManager.getInstance(getApplicationContext()).setKeepMeSignedIn(false);
                    Intent intent4 = new Intent(HomePageInstallerActivity.this, RegisterOrSignInActivity.class);
                    startActivity(intent4);
                    finish();
                })
                .setNegativeButton("NO", null)
                .show());

        addANewLocation.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageInstallerActivity.this, AddNewLocationInstallerActivity.class);
            startActivity(intent);
        });
    }

    private void getLocationsFromDatabase() {
        progressBar.setVisibility(View.VISIBLE);

        String fireSciPin = SharedPrefManager.getInstance(getApplicationContext()).getFireSciPin();
        String URL = "http://firesafetysci.com/android_app/api/get_locations_by_installer_pin.php?installer_firesci_pin=" + fireSciPin;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL,
                result -> {
                    Log.i("Result", result);
                    progressBar.setVisibility(View.GONE);

                    if (result.isEmpty()) {
                        noLocationsFoundImageView.setVisibility(View.VISIBLE);
                        clearRecyclerView();

                    } else {
                        noLocationsFoundImageView.setVisibility(View.GONE);
                        locationsArrayList.clear();

                        try {
                            JSONArray jsonArray = new JSONArray(result);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Location location = new Location();
                                location.setId(Integer.parseInt(jsonObject.getString("id")));
                                location.setInstallerFiresciPin(jsonObject.getString("installer_firesci_pin"));
                                location.setCustomerFiresciPin(jsonObject.getString("customer_firesci_pin"));
                                location.setCustomerFiresciPin2(jsonObject.getString("customer_firesci_pin_2"));
                                location.setCustomerFiresciPin3(jsonObject.getString("customer_firesci_pin_3"));
                                location.setCompanyName(jsonObject.getString("company_name"));
                                location.setCity(jsonObject.getString("city"));
                                location.setStateProvince(jsonObject.getString("state_province"));
                                location.setCountry(jsonObject.getString("country"));
                                location.setAddress(jsonObject.getString("address"));
                                location.setZipcode(jsonObject.getString("zipcode"));
                                location.setLocationDescription(jsonObject.getString("location_description"));

                                locationsArrayList.add(location);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        populateRecyclerView(locationsArrayList);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.addLocationImageButton), "Failed! Please try again!!!", 1250)
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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void clearRecyclerView() {
        locationsArrayList.clear();
        locationsRecyclerViewInstaller.setAdapter(new LocationAdapter(locationsArrayList, this, this));
    }

    private void populateRecyclerView(List<Location> locationList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        locationsRecyclerViewInstaller.setLayoutManager(layoutManager);
        LocationAdapter adapter = new LocationAdapter(locationList, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        locationsRecyclerViewInstaller.setLayoutManager(linearLayoutManager);
        locationsRecyclerViewInstaller.setItemAnimator(new DefaultItemAnimator());
        locationsRecyclerViewInstaller.setAdapter(adapter);
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

    @Override
    public void onItemClick(int position) {
        Location location = LocationAdapter.getLocationAt(position);

        Intent intent = new Intent(getApplicationContext(), SystemsInstallerActivity.class);
        startActivity(intent);
        SystemsInstallerActivity.location = location;
    }
}