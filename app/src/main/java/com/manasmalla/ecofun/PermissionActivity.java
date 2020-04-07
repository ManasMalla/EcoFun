package com.manasmalla.ecofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class PermissionActivity extends AppCompatActivity {

    ImageView btnNext;
    boolean firstRun;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private MaterialButton btnSkip;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_CODE = 100;
    private static final int FINE_LOCATION_PERMISSION_CODE = 110;
    private static final int BACKGROUND_LOCATION_PERMISSION_CODE = 120;
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 130;
    PermissionDialogRequester permissionDialogRequester;
    MaterialCardView permission_cardView;
    TextView permission_title, permission_description;
    ImageView permission_icon;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    LocationCallback mLocationCallback;
    GetVariablesForPermissions getVariablesForPermissions;
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnSkip.setVisibility(View.GONE);

                if (ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates
                            (getLocationRequest(), mLocationCallback,
                                    null /* Looper */);
                }
                //btnNext.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

            if (PermissionActivity.this.permissionDialogRequester.checkPermissions(PermissionActivity.this).size() > 0) {
                Log.i("permissionNumber", String.valueOf(PermissionActivity.this.permissionDialogRequester.checkPermissions(PermissionActivity.this).size()));
                if (arg0 == layouts.length - 1) {
                    PermissionActivity.this.viewPager.setCurrentItem(arg0 - 1);;
                    permissionDialogRequester.setLayoutView(PermissionActivity.this, permission_title, permission_description, permission_icon);
                    permission_cardView.setVisibility(View.VISIBLE);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissionDialogRequester.checkPermissions(PermissionActivity.this).size() > 0) {
                    permissionDialogRequester.setLayoutView(PermissionActivity.this, permission_title, permission_description, permission_icon);
                } else {
                    permission_cardView.setVisibility(View.GONE);
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        Toast.makeText(PermissionActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        if (requestCode == FINE_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissionDialogRequester.checkPermissions(PermissionActivity.this).size() > 0) {
                    permissionDialogRequester.setLayoutView(PermissionActivity.this, permission_title, permission_description, permission_icon);
                } else {
                    permission_cardView.setVisibility(View.GONE);
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        Toast.makeText(PermissionActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        if (requestCode == BACKGROUND_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissionDialogRequester.checkPermissions(PermissionActivity.this).size() > 0) {
                    permissionDialogRequester.setLayoutView(PermissionActivity.this, permission_title, permission_description, permission_icon);
                } else {
                    permission_cardView.setVisibility(View.GONE);
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        Toast.makeText(PermissionActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissionDialogRequester.checkPermissions(PermissionActivity.this).size() > 0) {
                    permissionDialogRequester.setLayoutView(PermissionActivity.this, permission_title, permission_description, permission_icon);
                } else {
                    permission_cardView.setVisibility(View.GONE);
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        Toast.makeText(PermissionActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

       */

        setContentView(R.layout.activity_permission);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (MaterialButton) findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        permission_title = findViewById(R.id.permissionHeadingTextView);
        permission_description = findViewById(R.id.permissionRequestDescription);
        permission_icon = findViewById(R.id.permissionIconImageView);
        permission_cardView = findViewById(R.id.permission_cardView);


        getVariablesForPermissions = new GetVariablesForPermissions();

        Intent intent = getIntent();
        firstRun = intent.getBooleanExtra("firstRun", true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLastLocation = locationResult.getLastLocation();
                Log.i("Location", "Lat:  " + mLastLocation.getLatitude() + " Lng: " + mLastLocation.getLongitude());
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                getVariablesForPermissions.getVariablesForPermission(PermissionActivity.this, mLastLocation);
            }
        };

        if (firstRun) {
            layouts = new int[]{
                    R.layout.welcome_slide_problem,
                    R.layout.welcome_slide_green_points,
                    R.layout.welcome_slide_drive_less,
                    R.layout.welcome_slide_ecofriendly,
                    R.layout.welcome_slide_plant_trees,
                    R.layout.welcome_slide_ocean,
                    R.layout.welcome_slide_avengers_assemble,
                    R.layout.welcome_slide_permissions,
                    R.layout.welcome_slide_variables};
        } else {
            layouts = new int[]{
                    R.layout.welcome_slide_permissions,
                    R.layout.welcome_slide_variables};
        }
        // layouts of all welcome sliders
        // add few more layouts if you want

        permissionDialogRequester = new PermissionDialogRequester();

        // adding bottom dots
        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);

                Log.i("getVariables", String.valueOf(current));

                    if (current == layouts.length - 1 && permissionDialogRequester.checkPermissions(PermissionActivity.this).size() > 0){
                        permissionDialogRequester.setLayoutView(PermissionActivity.this, permission_title, permission_description, permission_icon);
                        permission_cardView.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.GONE);
                        findViewById(R.id.view).setVisibility(View.GONE);
                    }else if (current == layouts.length - 1 && !(permissionDialogRequester.checkPermissions(PermissionActivity.this).size() > 0)){
                        btnNext.setVisibility(View.GONE);
                        viewPager.setCurrentItem(current);
                        findViewById(R.id.view).setVisibility(View.GONE);
                    }else{
                        if (current < layouts.length) {
                            // move to next screen
                            viewPager.setCurrentItem(current);
                        }
                    }
            }
        });
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive;
        int[] colorsInactive;

        if (!firstRun) {
            colorsActive = getResources().getIntArray(R.array.array_dot_active_notFirstRun);
            colorsInactive = getResources().getIntArray(R.array.array_dot_inactive_notFirstRun);
        }else {
            colorsActive = getResources().getIntArray(R.array.array_dot_active);
            colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        }

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    /**
     * View pager adapter
     */

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            if (position == layouts.length - 2) {
                Button permission_request_button = view.findViewById(R.id.permission_requested_button);
                Button permission_requester_button = findViewById(R.id.requestPermissionButton);
                permission_title = findViewById(R.id.permissionHeadingTextView);
                permission_description = findViewById(R.id.permissionRequestDescription);
                permission_icon = findViewById(R.id.permissionIconImageView);

                permission_requester_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (permission_title.getText().toString().matches(getString(R.string.ACTIVITY_RECOGNITION))) {
                            ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_PERMISSION_CODE);
                        } else if (permission_title.getText().toString().matches(getString(R.string.FINE_LOCATION))) {
                            ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_CODE);
                        } else if (permission_title.getText().toString().matches(getString(R.string.BACKGROUND_LOCATION))) {
                            ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_CODE);
                        } else if (permission_title.getText().toString().matches(getString(R.string.EXTERNAL_STORAGE))) {
                            ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                });


                permission_request_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionDialogRequester.setLayoutView(PermissionActivity.this, permission_title, permission_description, permission_icon);
                        permission_cardView.setVisibility(View.VISIBLE);
                    }
                });
            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}