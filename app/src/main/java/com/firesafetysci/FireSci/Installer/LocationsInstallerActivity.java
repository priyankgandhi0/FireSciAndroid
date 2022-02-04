package com.firesafetysci.FireSci.Installer;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.firesafetysci.FireSci.Main.CommonFunctions;
import com.firesafetysci.FireSci.Main.Location;
import com.firesafetysci.FireSci.Main.LocationAdapter;
import com.firesafetysci.FireSci.Main.SharedPrefManager;
import com.firesafetysci.FireSci.R;
import com.firesafetysci.FireSci.Main.RequestHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocationsInstallerActivity extends AppCompatActivity implements LocationAdapter.OnLocationClickListener {
    private RecyclerView locationsRecyclerViewInstaller;
    private ImageButton addLocationImageButton;
    private ImageView noLocationsFoundImageView;
    private LinearLayout progressBar;
    private ArrayList<Location> locationsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_installer);

        initViews();
        setOnClickListeners();

        Toolbar locationsActivityToolbar = findViewById(R.id.locationsActivityToolbar);
        locationsActivityToolbar.setTitle("");
        setSupportActionBar(locationsActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        clearRecyclerView();

        if (!CommonFunctions.isNetworkConnected(LocationsInstallerActivity.this)) {
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
        locationsRecyclerViewInstaller = findViewById(R.id.locationsRecyclerViewInstaller);
        addLocationImageButton = findViewById(R.id.addLocationImageButton);
        noLocationsFoundImageView = findViewById(R.id.noLocationsFoundImageView);
        progressBar = findViewById(R.id.progressBarLocations);
        locationsArrayList = new ArrayList<>();
    }

    private void setOnClickListeners() {
        addLocationImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationsInstallerActivity.this, AddNewLocationInstallerActivity.class);
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
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider)));
        locationsRecyclerViewInstaller.addItemDecoration(decoration);
    }

    @Override
    public void onItemClick(int position) {
        Location location = LocationAdapter.getLocationAt(position);

        Intent intent = new Intent(getApplicationContext(), SystemsInstallerActivity.class);
        startActivity(intent);
        SystemsInstallerActivity.location = location;
    }
}