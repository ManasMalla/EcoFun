package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;

public class InfoActivityScreen extends AppCompatActivity {

    boolean isShowingYoutubeCardView = false;

    Pair[] pairs;
    ConstraintLayout appTitleConstraintLayout;
    ImageView jungleBackgroundImageView;
    MaterialCardView mallaCardView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InfoActivityScreen.this, EarthActivity.class);
        intent.putExtra("presentUserContinent", getIntent().getStringExtra("presentUserContinent"));
        intent.putExtra("userLevel", getIntent().getStringExtra("userLevel"));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_info_screen);


        jungleBackgroundImageView = findViewById(R.id.jungleBackground_infoActivity);
        mallaCardView = findViewById(R.id.materialCardView);
        appTitleConstraintLayout = findViewById(R.id.appTitleConstraintLayout_infoActivity);



        pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(jungleBackgroundImageView, getString(R.string.jungleBackgroundSocialUpdates));
        pairs[1] = new Pair<View, String>(mallaCardView, getString(R.string.description_manasmalla_transition));
        pairs[2] = new Pair<View, String>(appTitleConstraintLayout, getString(R.string.appTitleTransitionSocialUpdates));

    }

    public void socialUpdatesOnClick(View view){

        Intent intent = new Intent(this, SocialUpdatesActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(InfoActivityScreen.this, pairs);
            startActivity(intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void youtubeOnClick(View view){
        if (isShowingYoutubeCardView){
            MaterialCardView youtubeMaterialCardView = findViewById(R.id.youtubeMaterialCardView);
            youtubeMaterialCardView.setVisibility(View.GONE);
            isShowingYoutubeCardView = false;
        }else {
            MaterialCardView youtubeMaterialCardView = findViewById(R.id.youtubeMaterialCardView);
            WebView webView = findViewById(R.id.youtubeWebView);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://www.youtube.com/channel/UCi-yplNduB04uW2rzB40CRA");
            youtubeMaterialCardView.setVisibility(View.VISIBLE);
            isShowingYoutubeCardView = true;
        }
    }

    public void betaTestersOnClick(View view){

        Intent intent = new Intent(this, BetaTestersActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(InfoActivityScreen.this, pairs);
            startActivity(intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }
    public void liscencesOnClick(View view){

        Intent intent = new Intent(this, LiscencesActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(InfoActivityScreen.this, pairs);
            startActivity(intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }
}