package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.sax.EndElementListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class EarthActivity extends AppCompatActivity {

    ImageView cityImageView, earthMap, earthSmile, outerSpaceFrame, characterImageView_levelDetailsCard;
    float dpMapHeight, dpMapWidth, dpScreenHeight, dpScreenWidth, scaleMap;
    boolean isShowingLevelCard = false;
    MaterialCardView levelDetailsCardView;
    String mapUserContinent = "Africa", presentUserContinent, userLevel;
    ImageView pin1, pin2, pin3;
    ConstraintLayout pinLayout;
    String[] arrayLevelDetails;
    TextView levelTitleTextView_levelDetailsCard, characterNameTextView_levelDetailsCardView;

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
            earthMap.setScaleX(3.5f);
            earthMap.setScaleY(3.5f);
            earthMap.animate().translationX(-1300.0f * scaleMap).translationY(-175.0f * scaleMap);
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

       updatePins(mapUserContinent, userLevel);
    }


    public void updatePins(String presentUserContinent, String userLevel) {

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
            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, (int) (this.scaleMap * 350.0f));
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, (int) (scaleMap * 80.0f));
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, 0);

            constraintSet.connect(pin2Id, ConstraintSet.BOTTOM, pin1ID, ConstraintSet.TOP, (int) (scaleMap * 50.0f));
            constraintSet.connect(pin2Id, ConstraintSet.START,pin1ID, ConstraintSet.END, (int) (scaleMap * 30.0f));
            constraintSet.applyTo(this.pinLayout);

        } else if (presentUserContinent.matches("Australia") || userLevel.matches("Indonesia")) {
            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, 0);
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, (int) (this.scaleMap * 500.0f));
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, 0);
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, (int) (this.scaleMap * 150.0f));

            constraintSet.connect(pin2Id, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, (int) (this.scaleMap * 350.0f));
            constraintSet.connect(pin2Id, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(pin2Id, ConstraintSet.START, pinLayoutId, ConstraintSet.START, 0);
            constraintSet.connect(pin2Id, ConstraintSet.END, pinLayoutId, ConstraintSet.END, (int) (this.scaleMap * 100.0f));

            constraintSet.connect(pin3Id, ConstraintSet.TOP, pin2Id, ConstraintSet.TOP, (int) (this.scaleMap * 40.0f));
            constraintSet.connect(pin3Id, ConstraintSet.START, pin2Id, ConstraintSet.END, (int) (this.scaleMap * 175.0f));
            constraintSet.setVisibility(pin3Id, ConstraintSet.VISIBLE);
            constraintSet.applyTo(this.pinLayout);
        } else if (presentUserContinent.matches("Africa")) {
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);
            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, 0);
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, (int) (this.scaleMap * 500.0f));
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, (int) (this.scaleMap * 200.0f));
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, 0);

            constraintSet.connect(pin2Id, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, (int) (this.scaleMap * 425.0f));
            constraintSet.connect(pin2Id, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(pin2Id, ConstraintSet.START, pinLayoutId, ConstraintSet.START, (int) (this.scaleMap * 575.0f));
            constraintSet.connect(pin2Id, ConstraintSet.END, pinLayoutId, ConstraintSet.END, 0);
            constraintSet.applyTo(this.pinLayout);
        } else if (presentUserContinent.matches("Europe")) {
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);
            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, (int) (this.scaleMap * 100.0f));
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, 0);
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, (int) (this.scaleMap * 275.0f));

            constraintSet.connect(pin2Id, ConstraintSet.BOTTOM, pin1ID, ConstraintSet.BOTTOM, (int) (this.scaleMap * 100.0f));
            constraintSet.connect(pin2Id, ConstraintSet.END, pin1ID, ConstraintSet.END, (int) (this.scaleMap * 50.0f));
            constraintSet.applyTo(this.pinLayout);
        } else if (presentUserContinent.matches("North America")) {
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);
            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, 0);
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, (int) (this.scaleMap * 100.0f));
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, 0);
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, (int) (this.scaleMap * 200.0f));

            constraintSet.connect(pin2Id, ConstraintSet.BOTTOM, pin1ID, ConstraintSet.TOP, (int) (this.scaleMap * 20.0f));
            constraintSet.connect(pin2Id, ConstraintSet.START, pin1ID, ConstraintSet.START, (int) (this.scaleMap * 100.0f));
            constraintSet.applyTo(this.pinLayout);
        } else if (presentUserContinent.matches("South America")) {
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);
            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, (int) (this.scaleMap * 100.0f));
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, (int) (this.scaleMap * 0.0f));
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, 0);

            constraintSet.connect(pin2Id, ConstraintSet.BOTTOM, pin1ID, ConstraintSet.TOP, (int) (this.scaleMap * 75.0f));
            constraintSet.connect(pin2Id, ConstraintSet.END, pin1ID, ConstraintSet.START, 0);
            constraintSet.applyTo(this.pinLayout);
        } else if (presentUserContinent.matches("Arctic")) {
            constraintSet.setVisibility(pin2Id, ConstraintSet.GONE);
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);

            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, (int) (this.scaleMap * 100.0f));
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, (int) (this.scaleMap * 100.0f));
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, 0);
            constraintSet.applyTo(this.pinLayout);
        } else if (presentUserContinent.matches("Antarctica")) {
            constraintSet.setVisibility(pin2Id, ConstraintSet.GONE);
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);

            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, (int) (this.scaleMap * 300.0f));
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, 0);
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, (int) (this.scaleMap * 100.0f));
            constraintSet.applyTo(this.pinLayout);

        } else if (presentUserContinent.matches("Atlantic Ocean")) {
            constraintSet.setVisibility(pin2Id, ConstraintSet.GONE);
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);
            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, 0);
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, (int) (this.scaleMap * 60.0f));
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, (int) (this.scaleMap * 125.0f));
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, 0);
            constraintSet.applyTo(this.pinLayout);

        } else if (presentUserContinent.matches("Pacific Ocean")) {
            constraintSet.setVisibility(pin2Id, ConstraintSet.GONE);
            constraintSet.setVisibility(pin3Id, ConstraintSet.GONE);

            constraintSet.connect(pin1ID, ConstraintSet.TOP, pinLayoutId, ConstraintSet.TOP, 0);
            constraintSet.connect(pin1ID, ConstraintSet.BOTTOM, pinLayoutId, ConstraintSet.BOTTOM, (int) (this.scaleMap * 50.0f));
            constraintSet.connect(pin1ID, ConstraintSet.START, pinLayoutId, ConstraintSet.START, (int) (this.scaleMap * 75.0f));
            constraintSet.connect(pin1ID, ConstraintSet.END, pinLayoutId, ConstraintSet.END, 0);
            constraintSet.applyTo(this.pinLayout);
        }

    }


    private void assignUI() {
        earthMap = findViewById(R.id.earthMapImageView);
        outerSpaceFrame = findViewById(R.id.outerSpaceFrameImageView_earthActivity);
        levelDetailsCardView = findViewById(R.id.levelDetailsCard);
        cityImageView = findViewById(R.id.cityImageView_earthActivity);
        earthSmile = findViewById(R.id.earth_smile);

        characterImageView_levelDetailsCard = findViewById(R.id.levelImageView);
        characterNameTextView_levelDetailsCardView = findViewById(R.id.levelCharacterName);
        levelTitleTextView_levelDetailsCard = findViewById(R.id.levelNation);

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

        levelConstants levelConstants = new levelConstants();

        ConstraintSet constraintSet =  new ConstraintSet();
        constraintSet.clone(pinLayout);

        if (view.getId() == pin1.getId()){
            Log.i("Pin", String.valueOf(1));
           arrayLevelDetails = levelConstants.levelArray(mapUserContinent, 1);
           constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.BOTTOM, pin1.getId(), ConstraintSet.TOP, (int) getResources().getDimension(R.dimen.bottomLevelCard));
           constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.START, pin1.getId(), ConstraintSet.START, 0);
            constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.END, pin1.getId(), ConstraintSet.END, 0);
            constraintSet.applyTo(pinLayout);
        }else if (view.getId() == pin2.getId()){
            Log.i("Pin", String.valueOf(2));
            arrayLevelDetails = levelConstants.levelArray(mapUserContinent, 2);
            if (mapUserContinent.matches("Africa")){
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.BOTTOM, pin2.getId(), ConstraintSet.TOP, (int) getResources().getDimension(R.dimen.bottomLevelCard));
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.END, pin2.getId(), ConstraintSet.END, 0);
                constraintSet.clear(levelDetailsCardView.getId(), ConstraintSet.START);
                constraintSet.applyTo(pinLayout);
            }else{
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.BOTTOM, pin2.getId(), ConstraintSet.TOP, (int) getResources().getDimension(R.dimen.bottomLevelCard));
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.START, pin2.getId(), ConstraintSet.START, 0);
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.END, pin2.getId(), ConstraintSet.END, 0);
                constraintSet.applyTo(pinLayout);
            }
        }else if (view.getId() == pin3.getId()){
            Log.i("Pin", String.valueOf(3));
            arrayLevelDetails = levelConstants.levelArray(mapUserContinent, 3);
            if (mapUserContinent.matches("Australia") || userLevel.matches("Indonesia")){
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.BOTTOM, pin3.getId(), ConstraintSet.TOP, (int) getResources().getDimension(R.dimen.bottomLevelCard));
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.END, pin3.getId(), ConstraintSet.END, 0);
                constraintSet.clear(levelDetailsCardView.getId(), ConstraintSet.START);
                constraintSet.applyTo(pinLayout);
            }else {
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.BOTTOM, pin3.getId(), ConstraintSet.TOP, (int) getResources().getDimension(R.dimen.bottomLevelCard));
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.START, pin3.getId(), ConstraintSet.START, 0);
                constraintSet.connect(levelDetailsCardView.getId(), ConstraintSet.END, pin3.getId(), ConstraintSet.END, 0);
                constraintSet.applyTo(pinLayout);
            }
        }
        if (arrayLevelDetails == null){
            Toast.makeText(this, "Sorry Couldnt get level details!", Toast.LENGTH_SHORT).show();
        }else {
            String userLevelTitle = arrayLevelDetails[0];
            String characterName = arrayLevelDetails[1];
            Log.i(userLevelTitle, characterName);
            int levelCharacterImageViewDrawableID = Integer.parseInt(arrayLevelDetails[2]);
            levelTitleTextView_levelDetailsCard.setText(userLevelTitle);
            characterNameTextView_levelDetailsCardView.setText(characterName);
            characterImageView_levelDetailsCard.setImageResource(levelCharacterImageViewDrawableID);
        }

        if (isShowingLevelCard){
            levelDetailsCardView.setVisibility(View.GONE);
            isShowingLevelCard = false;
        }else{
            levelDetailsCardView.setVisibility(View.VISIBLE);
            isShowingLevelCard = true;
        }

    }

    public void settingsOnClick(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void infoOnClick(View view){
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("presentUserContinent", presentUserContinent);
        intent.putExtra("userLevel", userLevel);
        startActivity(intent);
    }
    public void leaderboardOnClick(View view){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }
    public void plantTreesOnClick(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            } else {
                getPhoto();
            }
        } else {
            getPhoto();
        }
    }

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 250);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 250 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                Log.i("Photo", "Received");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ParseFile profilePic = new ParseFile("plantedTree"+  ParseUser.getCurrentUser().getUsername() +".png", byteArray);
                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseObject plantedTree = new ParseObject("plantingTrees");
                plantedTree.put("plantedTree", profilePic);
                plantedTree.put("username", currentUser.getUsername());
                plantedTree.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Photo saved", "true");
                            Toast.makeText(EarthActivity.this, "Uploaded Your Pic With Plant! Verifying! Will add Points once verified!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("Photo saved", "false");
                            Toast.makeText(EarthActivity.this, "Pic with Plant couldn't be uploaded - please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void mainActivityOnClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void journalOnClick(View view){
        Intent intent = new Intent(this, JournalActivity.class);
        startActivity(intent);
    }
}