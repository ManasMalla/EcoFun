package com.manasmalla.ecofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    LocationListener locationListener;
    boolean isActivityTransitioned, isCheckingForLocation;

    BroadcastReceiver broadcastReceiver;
    SharedPreferences sharedPreferences;
    MaterialButton btnTracking;
    private String TAG = MainActivity.class.getSimpleName();
    private TextView txtActivity, txtConfidence;
    private ImageView imgActivity;
    BottomNavigationView bottomNavigationView;
    Pair[] pairs;
    boolean isTracking = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = this.getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE);
        sharedPreferences.edit().putString("activityRecognised", "Unknown").apply();

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();;
                LatLng userLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocationLatLng).title("You are here"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocationLatLng, 18f));
                Log.i("Hello", String.valueOf(location));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    private void assignUI() {
        txtActivity = findViewById(R.id.txt_activity);
        txtConfidence = findViewById(R.id.distanceTextView_MainActivity);
        imgActivity = findViewById(R.id.img_activity);
        btnTracking = findViewById(R.id.btn_tracking);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        pairs = new Pair[1];
        pairs[0] = new Pair(bottomNavigationView, getString(R.string.navbarTransition));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.game_menu_id) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                        startActivity(intent, activityOptions.toBundle());
                    }

                }
                return false;
            }
        });

        isTracking = sharedPreferences.getBoolean("isTracking", true);

        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (isTracking){
                   isTracking = false;
                   sharedPreferences.edit().putBoolean("isTracking", isTracking).commit();
                   btnTracking.setText("Start Tracking");
                   stopTracking();
               }else{
                    isTracking = true;
                   sharedPreferences.edit().putBoolean("isTracking", isTracking).commit();
                   btnTracking.setText("Stop Tracking");
                   startTracking();
               }
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)) {
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    handleUserActivity(type, confidence);
                }
            }
        };

        startTracking();
    }


    private void handleUserActivity(int type, int confidence) {
        String label = getString(R.string.activity_unknown);
        int icon = R.drawable.ic_still;

        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = getString(R.string.activity_in_vehicle);
                icon = R.drawable.round_directions_car_24;
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = getString(R.string.activity_on_bicycle);
                icon = R.drawable.round_directions_bike_24;
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = getString(R.string.activity_on_foot);
                icon = R.drawable.round_directions_walk_24;
                break;
            }
            case DetectedActivity.RUNNING: {
                label = getString(R.string.activity_running);
                icon = R.drawable.round_directions_run_24;
                break;
            }
            case DetectedActivity.STILL: {
                label = getString(R.string.activity_still);
                break;
            }
            case DetectedActivity.TILTING: {
                label = getString(R.string.activity_tilting);
                icon = R.drawable.ic_tilting;
                break;
            }
            case DetectedActivity.WALKING: {
                label = getString(R.string.activity_walking);
                icon = R.drawable.round_directions_walk_24;
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = getString(R.string.activity_unknown);
                icon = R.drawable.ic_unknown;
                break;
            }
        }

        Log.e(TAG, "User activity: " + label + ", Confidence: " + confidence);

        if (confidence > Constants.CONFIDENCE) {
                txtActivity.setText(label.toLowerCase());
                txtConfidence.setText(confidence);
                imgActivity.setImageResource(icon);
                sharedPreferences.edit().putString("activityRecognised", label).apply();
                if (label.matches("Still")){
                    locationManager.removeUpdates(locationListener);
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        assignUI();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           // ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, locationListener);
    }

    private void startTracking() {
        Intent intent = new Intent(MainActivity.this, BackgroundDetectedActivitiesService.class);
        startService(intent);
        sharedPreferences.edit().putString("activityRecognised", "Unknown").apply();
    }

    private void stopTracking() {
        Intent intent = new Intent(MainActivity.this, BackgroundDetectedActivitiesService.class);
        stopService(intent);
        sharedPreferences.edit().putString("activityRecognised", "Unknown").apply();
    }

}