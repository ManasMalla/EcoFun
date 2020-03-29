package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class SocialLoginActivity extends AppCompatActivity {

    private static final String TAG = SocialLoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 001;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    boolean googleSignedIn = false, facebookSignedIn = false, twitterSignedIn = false, instagramSignedIn = false, socialSignedIn;
    private FirebaseAuth mAuth;
    String[] socialSignedInStringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_social_login);

        PrefManager prefManager = new PrefManager(SocialLoginActivity.this);
        socialSignedInStringArray = prefManager.userSocialLoginDetails();
        if (socialSignedInStringArray[1].matches("true")){
            socialSignedIn = true;
        }else{
            socialSignedIn = false;
        }

        Log.i("socialSignedIn", String.valueOf(socialSignedIn));

    }
}