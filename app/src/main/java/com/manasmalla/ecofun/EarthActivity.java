package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class EarthActivity extends AppCompatActivity {

    ImageView cityImageView, earthMap, earthSmile, outerSpaceFrame;
    float dpMapHeight, dpMapWidth, dpScreenHeight, dpScreenWidth, scaleMap;
    boolean isMapUpdated = false, isShowingLevelCard = false;
    MaterialCardView levelDetailsCardView;
    String mapUserContinent = "Africa", presentUserContinent, userLevel;
    ImageView pin1, pin2, pin3;
    ConstraintLayout pinLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_earth);

        assignUI();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        dpScreenHeight = outMetrics.heightPixels;
        dpScreenWidth = outMetrics.widthPixels;
        earthMap.getLayoutParams().width = (int) dpScreenWidth;
        earthMap.requestLayout();
        earthMap.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                earthMap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dpMapHeight = earthMap.getHeight();
                dpMapWidth = earthMap.getWidth();
                scaleMap = dpScreenWidth / 1080.0f;
                Log.i("Screen Width", String.valueOf(dpScreenWidth));
                Log.i("Screen Height", String.valueOf(dpScreenHeight));
                Log.i("Map Width", String.valueOf(dpMapWidth));
                Log.i("Map Height", String.valueOf(dpMapHeight));
                Log.i("scale", String.valueOf(scaleMap));
                updateMap(presentUserContinent);
            }
        });

        earthMap.setOnTouchListener(new OnSwipeTouchListener(EarthActivity.this){
            @Override
            public void onSwipeTop() {
                Log.i("Direction", "Top");
                if (mapUserContinent.matches("Asia")){
                    updateMap("Australia");
                }else if (mapUserContinent.matches("Australia")){
                    updateMap("Antarctica");
                }else if (mapUserContinent.matches("Africa")){
                    updateMap("Antarctica");
                }else if (mapUserContinent.matches("Europe")){
                    updateMap("Africa");
                }else if (mapUserContinent.matches("North America")){
                    updateMap("South America");
                }else if (mapUserContinent.matches("South America")){
                    updateMap("Antarctica");
                }else if (mapUserContinent.matches("Antarctica")){
                    updateMap("Europe");
                }else if (mapUserContinent.matches("Arctic")){
                    updateMap("Atlantic Ocean");
                }else if (mapUserContinent.matches("Atlantic Ocean")){
                    updateMap("South America");
                }else if (mapUserContinent.matches("Pacific Ocean")){
                    updateMap("Antarctica");
                }

            }

            @Override
            public void onSwipeLeft() {
                Log.i("Direction", "Left");

                if (mapUserContinent.matches("Asia")){
                    updateMap("North America");
                }else if (mapUserContinent.matches("Australia")){
                    updateMap("Pacific Ocean");
                }else if (mapUserContinent.matches("Africa")){
                    updateMap("Asia");
                }else if (mapUserContinent.matches("Europe")){
                    updateMap("Asia");
                }else if (mapUserContinent.matches("North America")){
                    updateMap("Atlantic Ocean");
                }else if (mapUserContinent.matches("South America")){
                    updateMap("Africa");
                }else if (mapUserContinent.matches("Antarctica")){
                    updateMap("Antarctica");
                }else if (mapUserContinent.matches("Arctic")){
                    updateMap("Europe");
                }else if (mapUserContinent.matches("Atlantic Ocean")){
                    updateMap("Africa");
                }else if (mapUserContinent.matches("Pacific Ocean")){
                    updateMap("South America");
                }

            }

            @Override
            public void onSwipeBottom() {
                //rotateEarth(earth, 1, 4);
                Log.i("Direction", "Bottom");
                if (mapUserContinent.matches("Asia")){
                    updateMap("Australia");
                }else if (mapUserContinent.matches("Australia")){
                    updateMap("Asia");
                }else if (mapUserContinent.matches("Africa")){
                    updateMap("Europe");
                }else if (mapUserContinent.matches("Europe")){
                    updateMap("Antarctica");
                }else if (mapUserContinent.matches("North America")){
                    updateMap("Arctic");
                }else if (mapUserContinent.matches("South America")){
                    updateMap("North America");
                }else if (mapUserContinent.matches("Antarctica")){
                    updateMap("Africa");
                }else if (mapUserContinent.matches("Arctic")){
                    updateMap("Antarctica");
                }else if (mapUserContinent.matches("Atlantic Ocean")){
                    updateMap("Arctic");
                }else if (mapUserContinent.matches("Pacific Ocean")){
                    updateMap("North America");
                }
            }

            @Override
            public void onSwipeRight() {
                //rotateEarth(earth, 1, 5);
                Log.i("Direction", "Right");
                if (mapUserContinent.matches("Asia")){
                    updateMap("Africa");
                }else if (mapUserContinent.matches("Australia")){
                    updateMap("Africa");
                }else if (mapUserContinent.matches("Africa")){
                    updateMap("South America");
                }else if (mapUserContinent.matches("Europe")){
                    updateMap("Atlantic Ocean");
                }else if (mapUserContinent.matches("North America")){
                    updateMap("Asia");
                }else if (mapUserContinent.matches("South America")){
                    updateMap("Pacific Ocean");
                }else if (mapUserContinent.matches("Antarctica")){
                    updateMap("Antarctica");
                }else if (mapUserContinent.matches("Arctic")){
                    updateMap("North America");
                }else if (mapUserContinent.matches("Atlantic Ocean")){
                    updateMap("North America");
                }else if (mapUserContinent.matches("Pacific Ocean")){
                    updateMap("Australia");
                }
            }

        });

    }
    public void updateMap(String toContinent){
        if (toContinent.matches("Africa")){
            earthMap.setScaleX(2.7f);
            earthMap.setScaleY(2.7f);
            earthMap.animate().translationY(50.0f * scaleMap).translationX(0);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer_africa);
            mapUserContinent = "Africa";
        }else if (toContinent.matches("Asia") && !(userLevel.matches("Indonesia"))){
            this.earthMap.setScaleX(2.7f);
            this.earthMap.setScaleY(2.7f);
            this.earthMap.animate().translationX(-550.0f * scaleMap).translationY(450.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer_africa);
            this.mapUserContinent = "Asia";
        }else if (toContinent.matches("Australia") || userLevel.matches("Indonesia")){
            this.earthMap.setScaleX(3.5f);
            this.earthMap.setScaleY(3.5f);
            this.earthMap.animate().translationX(-1300.0f * scaleMap).translationY(-175.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer_africa);
            mapUserContinent = "Australia";
        }else if (toContinent.matches("North America")){
            earthMap.setScaleY(2.7f);
            earthMap.setScaleX(2.7f);
            earthMap.animate().translationX(1025.0f * scaleMap).translationY(450.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer_americas);
            mapUserContinent = "North America";
        }else if (toContinent.matches("South America")){
            this.earthMap.setScaleY(3.0f);
            this.earthMap.setScaleX(3.0f);
            this.earthMap.animate().translationX(650.0f * scaleMap).translationY(-225.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer_americas);
            mapUserContinent = "South America";
        }else if (toContinent.matches("Europe")){
            earthMap.setScaleX(4.0f);
            earthMap.setScaleY(4.0f);
            earthMap.animate().translationX(0.0f).translationY(900.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer);
            mapUserContinent = "Europe";
        }else if (toContinent.matches("Antarctica")){
            earthMap.setScaleY(2.7f);
            earthMap.setScaleX(2.7f);
            earthMap.animate().translationX(-750.0f * scaleMap).translationY(-600.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer);
            mapUserContinent = "Antarctica";
        }else if (toContinent.matches("Arctic")){
            earthMap.setScaleY(5.0f);
            earthMap.setScaleX(5.0f);
            earthMap.animate().translationX(1200.0f * scaleMap).translationY(1550.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer);
            mapUserContinent = "Arctic";
        }else if (toContinent.matches("Atlantic Ocean")){
            this.earthMap.setScaleY(2.7f);
            this.earthMap.setScaleX(2.7f);
            this.earthMap.animate().translationX(500.0f * scaleMap).translationY(450.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer);
            mapUserContinent = "Atlantic Ocean";
        }else if (toContinent.matches("Pacific Ocean")){
            this.earthMap.setScaleY(2.7f);
            this.earthMap.setScaleX(2.7f);
            this.earthMap.animate().translationX(1000.0f * scaleMap).translationY(-300.0f * scaleMap);
            outerSpaceFrame.setImageResource(R.drawable.space_earth_layer);
            mapUserContinent = "Pacific Ocean";
        }

       // updatePins(mapUserContinent, userLevel);
    }

    /*
    public void updatePins(String presentUserContinent, String userLevel) {
        //String str3 = str; -- prsntUserContinent
        //String str4 = str2; --- userLevel
        int pin1ID = pin1.getId();
        int pin2Id = pin2.getId();
        int pin3Id = pin3.getId();
        int pinLayoutId = pinLayout.getId();
        int dimension = (int) getResources().getDimension(R.dimen.pinDP);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(pinLayout);
        constraintSet.clear(pin1ID);
        constraintSet.clear(pin2Id);
        constraintSet.clear(pin3Id);
        constraintSet.constrainWidth(pin1ID, dimension);
        constraintSet.constrainHeight(pin1ID, dimension);
        constraintSet.constrainWidth(pin2Id, dimension);
        constraintSet.constrainHeight(pin2Id, dimension);
        constraintSet.constrainWidth(pin3Id, dimension);
        constraintSet.constrainHeight(pin3Id, dimension);
        if (presentUserContinent.matches("Asia") && !userLevel.matches("Indonesia")) {
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);
            constraintSet.connect(pin1Id, 3, pinLayoutID, 3, (int) (this.scaleMap * 350.0f));
            constraintSet.connect(pin1ID, 4, pinLayoutId, 4, 0);
            constraintSet.connect(pin1ID, 6, pinLayoutId, 6, (int) (this.scaleMap * 80.0f));
            constraintSet.connect(pin1ID, 7, pinLayoutId, 7, 0);
            constraintSet.connect(pin2Id, 4, i4, 3, (int) (this.scaleMap * 50.0f));
            constraintSet.connect(i3, 6, i4, 7, (int) (this.scaleMap * 30.0f));
            constraintSet.applyTo(this.pinLayout);
        } else if (str3.matches("Australia") || str4.matches("Indonesia")) {
            ConstraintSet constraintSet4 = constraintSet;
            int i5 = id;
            int i6 = id4;
            constraintSet4.connect(i5, 3, i6, 3, 0);
            constraintSet4.connect(i5, 4, i6, 4, (int) (this.scaleMap * 500.0f));
            constraintSet4.connect(i5, 6, i6, 6, 0);
            constraintSet.connect(i5, 7, i6, 7, (int) (this.scaleMap * 150.0f));
            ConstraintSet constraintSet5 = constraintSet;
            int i7 = id2;
            int i8 = id4;
            constraintSet5.connect(i7, 3, i8, 3, (int) (this.scaleMap * 350.0f));
            constraintSet5.connect(i7, 4, i8, 4, 0);
            constraintSet5.connect(i7, 6, i8, 6, 0);
            constraintSet5.connect(i7, 7, i8, 7, (int) (this.scaleMap * 100.0f));
            int i9 = id3;
            int i10 = id2;
            constraintSet5.connect(i9, 3, i10, 3, (int) (this.scaleMap * 40.0f));
            constraintSet5.connect(i9, 6, i10, 7, (int) (this.scaleMap * 175.0f));
            constraintSet.applyTo(this.pinLayout);
        } else if (str3.matches("Africa")) {
            constraintSet.setVisibility(id3, 8);
            ConstraintSet constraintSet6 = constraintSet;
            int i11 = id;
            int i12 = id4;
            constraintSet6.connect(i11, 3, i12, 3, 0);
            constraintSet6.connect(i11, 4, i12, 4, (int) (this.scaleMap * 500.0f));
            constraintSet6.connect(i11, 6, i12, 6, (int) (this.scaleMap * 200.0f));
            constraintSet6.connect(i11, 7, i12, 7, 0);
            ConstraintSet constraintSet7 = constraintSet;
            int i13 = id2;
            int i14 = id4;
            constraintSet7.connect(i13, 3, i14, 3, (int) (this.scaleMap * 425.0f));
            constraintSet7.connect(i13, 4, i14, 4, 0);
            constraintSet7.connect(i13, 6, i14, 6, (int) (this.scaleMap * 575.0f));
            constraintSet7.connect(i13, 7, i14, 7, 0);
            constraintSet.applyTo(this.pinLayout);
        } else if (str3.matches("Europe")) {
            constraintSet.setVisibility(id3, 8);
            ConstraintSet constraintSet8 = constraintSet;
            int i15 = id;
            int i16 = id4;
            constraintSet8.connect(i15, 3, i16, 3, (int) (this.scaleMap * 100.0f));
            constraintSet8.connect(i15, 4, i16, 4, 0);
            constraintSet8.connect(i15, 6, i16, 6, 0);
            ConstraintSet constraintSet9 = constraintSet;
            constraintSet9.connect(i15, 7, i16, 7, (int) (this.scaleMap * 275.0f));
            int i17 = id2;
            int i18 = id;
            constraintSet9.connect(i17, 4, i18, 4, (int) (this.scaleMap * 100.0f));
            constraintSet9.connect(i17, 7, i18, 7, (int) (this.scaleMap * 50.0f));
            constraintSet.applyTo(this.pinLayout);
        } else if (str3.matches("North America")) {
            constraintSet.setVisibility(id3, 8);
            ConstraintSet constraintSet10 = constraintSet;
            int i19 = id;
            int i20 = id4;
            constraintSet10.connect(i19, 3, i20, 3, 0);
            constraintSet10.connect(i19, 4, i20, 4, (int) (this.scaleMap * 100.0f));
            constraintSet10.connect(i19, 6, i20, 6, 0);
            constraintSet10.connect(i19, 7, i20, 7, (int) (this.scaleMap * 200.0f));
            ConstraintSet constraintSet11 = constraintSet;
            int i21 = id2;
            int i22 = id;
            constraintSet11.connect(i21, 4, i22, 3, (int) (this.scaleMap * 20.0f));
            constraintSet11.connect(i21, 6, i22, 6, (int) (this.scaleMap * 100.0f));
            constraintSet.applyTo(this.pinLayout);
        } else if (str3.matches("South America")) {
            constraintSet.setVisibility(id3, 8);
            ConstraintSet constraintSet12 = constraintSet;
            int i23 = id;
            int i24 = id4;
            constraintSet12.connect(i23, 3, i24, 3, (int) (this.scaleMap * 100.0f));
            constraintSet12.connect(i23, 4, i24, 4, 0);
            ConstraintSet constraintSet13 = constraintSet;
            constraintSet13.connect(i23, 6, i24, 6, (int) (this.scaleMap * 0.0f));
            constraintSet13.connect(i23, 7, i24, 7, 0);
            int i25 = id2;
            int i26 = id;
            constraintSet13.connect(i25, 4, i26, 3, (int) (this.scaleMap * 75.0f));
            constraintSet13.connect(i25, 7, i26, 6, 0);
            constraintSet.applyTo(this.pinLayout);
        } else if (str4.matches("Arctic")) {
            constraintSet.setVisibility(id2, 8);
            constraintSet.setVisibility(id3, 8);
            ConstraintSet constraintSet14 = constraintSet;
            int i27 = id;
            int i28 = id4;
            constraintSet14.connect(i27, 3, i28, 3, (int) (this.scaleMap * 100.0f));
            constraintSet14.connect(i27, 4, i28, 4, 0);
            constraintSet14.connect(i27, 6, i28, 6, (int) (this.scaleMap * 100.0f));
            constraintSet14.connect(i27, 7, i28, 7, 0);
            constraintSet.applyTo(this.pinLayout);
        } else if (str4.matches("Antarctica")) {
            constraintSet.setVisibility(id2, 8);
            constraintSet.setVisibility(id3, 8);
            ConstraintSet constraintSet15 = constraintSet;
            int i29 = id;
            int i30 = id4;
            constraintSet15.connect(i29, 3, i30, 3, (int) (this.scaleMap * 300.0f));
            constraintSet15.connect(i29, 4, i30, 4, 0);
            constraintSet15.connect(i29, 6, i30, 6, 0);
            constraintSet15.connect(i29, 7, i30, 7, (int) (this.scaleMap * 100.0f));
            constraintSet.applyTo(this.pinLayout);
        } else if (str4.matches("Atlantic Ocean")) {
            constraintSet.setVisibility(id2, 8);
            constraintSet.setVisibility(id3, 8);
            int i31 = id;
            int i32 = id4;
            constraintSet.connect(i31, 3, i32, 3, 0);
            constraintSet.connect(i31, 4, i32, 4, (int) (this.scaleMap * 60.0f));
            ConstraintSet constraintSet16 = constraintSet;
            constraintSet16.connect(i31, 6, i32, 6, (int) (this.scaleMap * 125.0f));
            constraintSet16.connect(i31, 7, i32, 7, 0);
            constraintSet.applyTo(this.pinLayout);
        } else if (str4.matches("Pacific Ocean")) {
            constraintSet.setVisibility(id2, 8);
            constraintSet.setVisibility(id3, 8);
            ConstraintSet constraintSet17 = constraintSet;
            int i33 = id;
            int i34 = id4;
            constraintSet17.connect(i33, 3, i34, 3, 0);
            constraintSet17.connect(i33, 4, i34, 4, (int) (this.scaleMap * 50.0f));
            constraintSet17.connect(i33, 6, i34, 6, (int) (this.scaleMap * 75.0f));
            constraintSet17.connect(i33, 7, i34, 7, 0);
            constraintSet.applyTo(this.pinLayout);
        }
    }

     */

    private void assignUI() {
        earthMap = findViewById(R.id.earthMapImageView);
        outerSpaceFrame = findViewById(R.id.outerSpaceFrameImageView_earthActivity);
        //levelDetailsCardView
        //cityImageView
        //earthSmile
        Intent previousIntent = getIntent();
        presentUserContinent = previousIntent.getStringExtra("presentUserContinent");
        userLevel = previousIntent.getStringExtra("userLevel");
        pin1 = findViewById(R.id.pinMap);
        pin2 = findViewById(R.id.pinMap2);
        pin3 = findViewById(R.id.pinMap3);
        pinLayout = findViewById(R.id.pinLayout);
    }

    public void onBackPressed() {
        super.onBackPressed();
        ParseUser.logOutInBackground(new LogOutCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    startActivity(new Intent(EarthActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(EarthActivity.this, "Error, logging you out! " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showDetailsCard(View view){

    }
}