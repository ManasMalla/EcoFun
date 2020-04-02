package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.parse.ParseUser;


public class SplashScreenActivity extends AppCompatActivity {

    ImageView jungleBackgroundImageView, animalsCarImageView, cityBackgroundImageView;
    ConstraintLayout appTitleConstraintLayout;
    Pair[] pairs;
    Intent intentToLoginActivity;
    String presentUserContinent, userLevel;
    boolean firstRun;
    PrefManager prefManager;

    //Display related Variables
    float width = 0;
    AnimatorSet animatorSet;
    Animation upToDown, downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefManager = new PrefManager(SplashScreenActivity.this);

        firstRun = prefManager.isFirstTimeLaunch();

        if(firstRun) {
            setContentView(R.layout.activity_splash_screen_firstrun);
        }else{
            setContentView(R.layout.activity_splash_screen);
        }

        jungleBackgroundImageView = findViewById(R.id.jungleBackgroundImageView_splashScreen);
        cityBackgroundImageView = findViewById(R.id.cityBackground_splashScreen);
        animalsCarImageView = findViewById(R.id.animalsCarImageView_splashScreen);

        appTitleConstraintLayout = findViewById(R.id.appTitleConstraintLayout_splashScreen);

        presentUserContinent = prefManager.userLevelDetails()[0];
        userLevel = prefManager.userLevelDetails()[1];

        upToDown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        appTitleConstraintLayout.setVisibility(View.GONE);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        width = outMetrics.widthPixels;

        if (firstRun){
            animateBackground();
        }else{

            appTitleConstraintLayout.setVisibility(View.VISIBLE);
            appTitleConstraintLayout.setAnimation(upToDown);

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    checkForUser();
                }
            };
            handler.postDelayed(runnable, 2000);
        }
    }

    public void animateBackground() {

        //Set up the animator variables
        final ObjectAnimator animateCityBackground = ObjectAnimator.ofFloat(cityBackgroundImageView, "translationX", -width);
        final ObjectAnimator animateJungleBackground = ObjectAnimator.ofFloat(jungleBackgroundImageView, "translationX", 0);
        final ObjectAnimator sendJungleBackgroundOut = ObjectAnimator.ofFloat(jungleBackgroundImageView, "translationX", width);

        //send jungle background out of screen
        sendJungleBackgroundOut.setInterpolator(new LinearInterpolator());
        sendJungleBackgroundOut.setDuration(50);
        sendJungleBackgroundOut.start();

        //setting the animate backgrounds duration and interpolator
        animateCityBackground.setDuration(3200);
        animateJungleBackground.setDuration(3200);
        animateCityBackground.setInterpolator(new LinearInterpolator());
        animateJungleBackground.setInterpolator(new LinearInterpolator());

        //create an animator set
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animateCityBackground, animateJungleBackground);


        //wait for the previous animation of sending out jungle
        Handler animatorHandler = new Handler();
        Runnable runnableAnimator = new Runnable() {
            @Override
            public void run() {

                //Start the animation
               animatorSet.start();

               animatorSet.addListener(new Animator.AnimatorListener() {
                   @Override
                   public void onAnimationStart(Animator animation) {

                   }

                   @Override
                   public void onAnimationEnd(Animator animation) {

                       appTitleConstraintLayout.setVisibility(View.VISIBLE);
                       appTitleConstraintLayout.setAnimation(upToDown);

                       Handler handler = new Handler();
                       Runnable runnable = new Runnable() {
                           @Override
                           public void run() {

                               checkForUser();

                           }
                       };
                       handler.postDelayed(runnable, 1800);

                   }

                   @Override
                   public void onAnimationCancel(Animator animation) {

                   }

                   @Override
                   public void onAnimationRepeat(Animator animation) {

                   }
               });
            }
        };
        animatorHandler.postDelayed(runnableAnimator, 60);

    }


    public void checkForUser(){

        pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(jungleBackgroundImageView, getString(R.string.jungleImageViewTransition));
        pairs[1] = new Pair<View, String>(animalsCarImageView, getString(R.string.animalsCarImageViewTransition));

        if (ParseUser.getCurrentUser() == null){
            //User is not signed in
            intentToLoginActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, pairs);
                startActivity(intentToLoginActivity, activityOptions.toBundle());
            } else {
                startActivity(intentToLoginActivity);
            }
        }else{
            //User signed in
            Intent intentToEarthActivity = new Intent(SplashScreenActivity.this, EarthActivity.class);
            Intent intentToPermissionActivity = new Intent(SplashScreenActivity.this, PermissionActivity.class);
            if (firstRun){
                intentToPermissionActivity.putExtra("firstRun", true);
                startActivity(intentToPermissionActivity);
            }else{
                intentToPermissionActivity.putExtra("firstRun", false);
                PermissionDialogRequester permissionDialogRequester = new PermissionDialogRequester();

                if (presentUserContinent == null || userLevel == null || presentUserContinent.isEmpty() || userLevel.isEmpty()) {
                    //Variables and permissions not available
                    startActivity(intentToPermissionActivity);
                } else {
                    if (permissionDialogRequester.checkPermissions(SplashScreenActivity.this).size() > 0) {
                        //Variables avaiable but no permissions
                        startActivity(intentToPermissionActivity);
                    } else {
                        //Everything are avaialable
                        intentToEarthActivity.putExtra("presentUserContinent", presentUserContinent);
                        intentToEarthActivity.putExtra("userLevel", userLevel);
                        startActivity(intentToEarthActivity);
                    }
                }

            }
        }

    }


}