package com.manasmalla.ecofun;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String[] userLevelDetails(){
        String presentUserContinent, userLevel;
        presentUserContinent = pref.getString("presentUserContinent", null);
        userLevel = pref.getString("userLevel", null);
        return new String[]{presentUserContinent, userLevel};
    }

    public String[] userSocialLoginDetails(){
        boolean googleSignedIn, facebookSignedIn, twitterSignedIn, instagramSignedIn, socialSignedIn;
        String socialMediaPlatformSignedIn;
        googleSignedIn = pref.getBoolean("isGoogleSignedIn", false);
        facebookSignedIn = pref.getBoolean("isFacebookSignedIn", false);
        twitterSignedIn = pref.getBoolean("isTwitterSignedIn", false);
        instagramSignedIn = pref.getBoolean("isInstagramSignedIn", false);
        if (googleSignedIn){
            socialMediaPlatformSignedIn = "GOOGLE";
            socialSignedIn = true;
        }else if (facebookSignedIn){
            socialMediaPlatformSignedIn = "FACEBOOK";
            socialSignedIn = true;
        }else if (twitterSignedIn){
            socialMediaPlatformSignedIn = "TWITTER";
            socialSignedIn = true;
        }else if (instagramSignedIn){
            socialMediaPlatformSignedIn = "INSTAGRAM";
            socialSignedIn = true;
        }else{
            socialMediaPlatformSignedIn = null;
            socialSignedIn = false;
        }
        return new String[]{socialMediaPlatformSignedIn, String.valueOf(socialSignedIn)};
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

}