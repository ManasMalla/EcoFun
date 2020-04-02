package com.manasmalla.ecofun;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GetVariablesForPermissions extends Application {

    String presentUserContinent, userLevel, userLevelParse, userContinent;
    Date displayingLayoutDate;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mLocationCallback;
    String json_continent = "{\"AD\":\"Europe\",\"AE\":\"Asia\",\"AF\":\"Asia\",\"AG\":\"North America\",\"AI\":\"North America\",\"AL\":\"Europe\",\"AM\":\"Asia\",\"AN\":\"North America\",\"AO\":\"Africa\",\"AQ\":\"Antarctica\",\"AR\":\"South America\",\"AS\":\"Australia\",\"AT\":\"Europe\",\"AU\":\"Australia\",\"AW\":\"North America\",\"AZ\":\"Asia\",\"BA\":\"Europe\",\"BB\":\"North America\",\"BD\":\"Asia\",\"BE\":\"Europe\",\"BF\":\"Africa\",\"BG\":\"Europe\",\"BH\":\"Asia\",\"BI\":\"Africa\",\"BJ\":\"Africa\",\"BM\":\"North America\",\"BN\":\"Asia\",\"BO\":\"South America\",\"BR\":\"South America\",\"BS\":\"North America\",\"BT\":\"Asia\",\"BW\":\"Africa\",\"BY\":\"Europe\",\"BZ\":\"North America\",\"CA\":\"North America\",\"CC\":\"Asia\",\"CD\":\"Africa\",\"CF\":\"Africa\",\"CG\":\"Africa\",\"CH\":\"Europe\",\"CI\":\"Africa\",\"CK\":\"Australia\",\"CL\":\"South America\",\"CM\":\"Africa\",\"CN\":\"Asia\",\"CO\":\"South America\",\"CR\":\"North America\",\"CU\":\"North America\",\"CV\":\"Africa\",\"CX\":\"Asia\",\"CY\":\"Asia\",\"CZ\":\"Europe\",\"DE\":\"Europe\",\"DJ\":\"Africa\",\"DK\":\"Europe\",\"DM\":\"North America\",\"DO\":\"North America\",\"DZ\":\"Africa\",\"EC\":\"South America\",\"EE\":\"Europe\",\"EG\":\"Africa\",\"EH\":\"Africa\",\"ER\":\"Africa\",\"ES\":\"Europe\",\"ET\":\"Africa\",\"FI\":\"Europe\",\"FJ\":\"Australia\",\"FK\":\"South America\",\"FM\":\"Australia\",\"FO\":\"Europe\",\"FR\":\"Europe\",\"GA\":\"Africa\",\"GB\":\"Europe\",\"GD\":\"North America\",\"GE\":\"Asia\",\"GF\":\"South America\",\"GG\":\"Europe\",\"GH\":\"Africa\",\"GI\":\"Europe\",\"GL\":\"North America\",\"GM\":\"Africa\",\"GN\":\"Africa\",\"GP\":\"North America\",\"GQ\":\"Africa\",\"GR\":\"Europe\",\"GS\":\"Antarctica\",\"GT\":\"North America\",\"GU\":\"Australia\",\"GW\":\"Africa\",\"GY\":\"South America\",\"HK\":\"Asia\",\"HN\":\"North America\",\"HR\":\"Europe\",\"HT\":\"North America\",\"HU\":\"Europe\",\"ID\":\"Asia\",\"IE\":\"Europe\",\"IL\":\"Asia\",\"IM\":\"Europe\",\"IN\":\"Asia\",\"IO\":\"Asia\",\"IQ\":\"Asia\",\"IR\":\"Asia\",\"IS\":\"Europe\",\"IT\":\"Europe\",\"JE\":\"Europe\",\"JM\":\"North America\",\"JO\":\"Asia\",\"JP\":\"Asia\",\"KE\":\"Africa\",\"KG\":\"Asia\",\"KH\":\"Asia\",\"KI\":\"Australia\",\"KM\":\"Africa\",\"KN\":\"North America\",\"KP\":\"Asia\",\"KR\":\"Asia\",\"KW\":\"Asia\",\"KY\":\"North America\",\"KZ\":\"Asia\",\"LA\":\"Asia\",\"LB\":\"Asia\",\"LC\":\"North America\",\"LI\":\"Europe\",\"LK\":\"Asia\",\"LR\":\"Africa\",\"LS\":\"Africa\",\"LT\":\"Europe\",\"LU\":\"Europe\",\"LV\":\"Europe\",\"LY\":\"Africa\",\"MA\":\"Africa\",\"MC\":\"Europe\",\"MD\":\"Europe\",\"ME\":\"Europe\",\"MG\":\"Africa\",\"MH\":\"Australia\",\"MK\":\"Europe\",\"ML\":\"Africa\",\"MM\":\"Asia\",\"MN\":\"Asia\",\"MO\":\"Asia\",\"MP\":\"Australia\",\"MQ\":\"North America\",\"MR\":\"Africa\",\"MS\":\"North America\",\"MT\":\"Europe\",\"MU\":\"Africa\",\"MV\":\"Asia\",\"MW\":\"Africa\",\"MX\":\"North America\",\"MY\":\"Asia\",\"MZ\":\"Africa\",\"NA\":\"Africa\",\"NC\":\"Australia\",\"NE\":\"Africa\",\"NF\":\"Australia\",\"NG\":\"Africa\",\"NI\":\"North America\",\"NL\":\"Europe\",\"NO\":\"Europe\",\"NP\":\"Asia\",\"NR\":\"Australia\",\"NU\":\"Australia\",\"NZ\":\"Australia\",\"OM\":\"Asia\",\"PA\":\"North America\",\"PE\":\"South America\",\"PF\":\"Australia\",\"PG\":\"Australia\",\"PH\":\"Asia\",\"PK\":\"Asia\",\"PL\":\"Europe\",\"PM\":\"North America\",\"PN\":\"Australia\",\"PR\":\"North America\",\"PS\":\"Asia\",\"PT\":\"Europe\",\"PW\":\"Australia\",\"PY\":\"South America\",\"QA\":\"Asia\",\"RE\":\"Africa\",\"RO\":\"Europe\",\"RS\":\"Europe\",\"RU\":\"Europe\",\"RW\":\"Africa\",\"SA\":\"Asia\",\"SB\":\"Australia\",\"SC\":\"Africa\",\"SD\":\"Africa\",\"SE\":\"Europe\",\"SG\":\"Asia\",\"SH\":\"Africa\",\"SI\":\"Europe\",\"SJ\":\"Europe\",\"SK\":\"Europe\",\"SL\":\"Africa\",\"SM\":\"Europe\",\"SN\":\"Africa\",\"SO\":\"Africa\",\"SR\":\"South America\",\"ST\":\"Africa\",\"SV\":\"North America\",\"SY\":\"Asia\",\"SZ\":\"Africa\",\"TC\":\"North America\",\"TD\":\"Africa\",\"TF\":\"Antarctica\",\"TG\":\"Africa\",\"TH\":\"Asia\",\"TJ\":\"Asia\",\"TK\":\"Australia\",\"TM\":\"Asia\",\"TN\":\"Africa\",\"TO\":\"Australia\",\"TR\":\"Asia\",\"TT\":\"North America\",\"TV\":\"Australia\",\"TW\":\"Asia\",\"TZ\":\"Africa\",\"UA\":\"Europe\",\"UG\":\"Africa\",\"US\":\"North America\",\"UY\":\"South America\",\"UZ\":\"Asia\",\"VC\":\"North America\",\"VE\":\"South America\",\"VG\":\"North America\",\"VI\":\"North America\",\"VN\":\"Asia\",\"VU\":\"Australia\",\"WF\":\"Australia\",\"WS\":\"Australia\",\"YE\":\"Asia\",\"YT\":\"Africa\",\"ZA\":\"Africa\",\"ZM\":\"Africa\",\"ZW\":\"Africa\"}";
    Context mContext;
    ParseUser parseUser;
    PrefManager prefManager;

    public void getVariablesForPermission(final Context context, final Location mLocation) {

        mContext = context;
        prefManager = new PrefManager(context);

        ParseUser user = ParseUser.getCurrentUser();
        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    presentUserContinent = object.getString("presentUserContinent");
                    userLevel = object.getString("userLevel");
                    if (presentUserContinent == null || presentUserContinent.isEmpty() || userLevel == null || userLevel.isEmpty()) {
                        getVariablesFromDevice(mLocation);
                    } else {
                        prefManager.addVariablesToSharedPreferences(presentUserContinent, userLevel);
                        goToEarthActivity(context);
                    }
                } else {
                    getVariablesFromDevice(mLocation);
                }
            }
        });

    }

    private void goToEarthActivity(Context context) {

        PrefManager prefManager = new PrefManager(GetVariablesForPermissions.this.mContext);
        prefManager.setFirstTimeLaunch(false);

        Intent intentToEarthActivity = new Intent(context, EarthActivity.class);
        intentToEarthActivity.putExtra("presentUserContinent", presentUserContinent);
        intentToEarthActivity.putExtra("userLevel", userLevel);
        mContext.startActivity(intentToEarthActivity);
    }

    @SuppressLint("MissingPermission")
    private void getVariablesFromDevice(Location deviceLocation) {

        getContinent(deviceLocation);

    }

    //Check

    private void getContinent(Location userLocation) {
        try {
            JSONObject jsonObject = new JSONObject(json_continent);
            Geocoder geocoder = new Geocoder(mContext, Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                // getCountryCode from Address
                String countryCode = fetchedAddress.getCountryCode();
                // get continentName here
                userContinent = jsonObject.getString(countryCode);
                Log.i("Contintent is", userContinent);
                Toast.makeText(mContext, "You are from continent " + userContinent + "!", Toast.LENGTH_SHORT).show();
                getUserLevel(userContinent, mContext);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserLevel(final String userContinent, final Context mContext) {
        userLevelParse = null;
        parseUser = ParseUser.getCurrentUser();
        parseUser.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    int userGP = object.getInt("greenPoints");
                    Log.i("userGP", String.valueOf(userGP));
                    if (userContinent.matches("Asia")) {
                        if (userGP >= 0 && userGP <= levelConstants.level1) {
                            userLevelParse = "India";
                        } else if (userGP > levelConstants.level1 && userGP <= levelConstants.level2) {
                            userLevelParse = "China";
                        } else if (userGP > levelConstants.level2 && userGP <= levelConstants.level3) {
                            userLevelParse = "Indonesia";
                        } else if (userGP > levelConstants.level3 && userGP <= levelConstants.level4) {
                            userLevelParse = "Australian Desert";
                        } else if (userGP > levelConstants.level4 && userGP <= levelConstants.level5) {
                            userLevelParse = "Australian Forest";
                        } else if (userGP > levelConstants.level5 && userGP <= levelConstants.level6) {
                            userLevelParse = "Antarctica";
                        } else if (userGP > levelConstants.level6 && userGP <= levelConstants.level7) {
                            userLevelParse = "Egypt";
                        } else if (userGP > levelConstants.level7 && userGP <= levelConstants.level8) {
                            userLevelParse = "Madagascar";
                        } else if (userGP > levelConstants.level8 && userGP <= levelConstants.level9) {
                            userLevelParse = "U.K.";
                        } else if (userGP > levelConstants.level9 && userGP <= levelConstants.level10) {
                            userLevelParse = "France";
                        } else if (userGP > levelConstants.level10 && userGP <= levelConstants.level11) {
                            userLevelParse = "Arctic";
                        } else if (userGP > levelConstants.level11 && userGP <= levelConstants.level12) {
                            userLevelParse = "Pacific Ocean";
                        } else if (userGP > levelConstants.level12 && userGP <= levelConstants.level13) {
                            userLevelParse = "Atlantic Ocean";
                        } else if (userGP > levelConstants.level13 && userGP <= levelConstants.level14) {
                            userLevelParse = "California";
                        } else if (userGP > levelConstants.level14 && userGP <= levelConstants.level15) {
                            userLevelParse = "Canada";
                        } else if (userGP > levelConstants.level15 && userGP <= levelConstants.level16) {
                            userLevelParse = "Argentina";
                        } else if (userGP > levelConstants.level16) {
                            userLevelParse = "Amazon Rainforest";
                        }
                    } else if (userContinent.matches("Australia")) {
                        if (userGP >= 0 && userGP <= levelConstants.level1) {
                            userLevelParse = "Australian Desert";
                        } else if (userGP > levelConstants.level1 && userGP <= levelConstants.level2) {
                            userLevelParse = "Australian Forest";
                        } else if (userGP > levelConstants.level2 && userGP <= levelConstants.level3) {
                            userLevelParse = "Indonesia";
                        } else if (userGP > levelConstants.level3 && userGP <= levelConstants.level4) {
                            userLevelParse = "India";
                        } else if (userGP > levelConstants.level4 && userGP <= levelConstants.level5) {
                            userLevelParse = "China";
                        } else if (userGP > levelConstants.level5 && userGP <= levelConstants.level6) {
                            userLevelParse = "Antarctica";
                        } else if (userGP > levelConstants.level6 && userGP <= levelConstants.level7) {
                            userLevelParse = "Egypt";
                        } else if (userGP > levelConstants.level7 && userGP <= levelConstants.level8) {
                            userLevelParse = "Madagascar";
                        } else if (userGP > levelConstants.level8 && userGP <= levelConstants.level9) {
                            userLevelParse = "U.K.";
                        } else if (userGP > levelConstants.level9 && userGP <= levelConstants.level10) {
                            userLevelParse = "France";
                        } else if (userGP > levelConstants.level10 && userGP <= levelConstants.level11) {
                            userLevelParse = "Arctic";
                        } else if (userGP > levelConstants.level11 && userGP <= levelConstants.level12) {
                            userLevelParse = "Pacific Ocean";
                        } else if (userGP > levelConstants.level12 && userGP <= levelConstants.level13) {
                            userLevelParse = "Atlantic Ocean";
                        } else if (userGP > levelConstants.level13 && userGP <= levelConstants.level14) {
                            userLevelParse = "California";
                        } else if (userGP > levelConstants.level14 && userGP <= levelConstants.level15) {
                            userLevelParse = "Canada";
                        } else if (userGP > levelConstants.level15 && userGP <= levelConstants.level16) {
                            userLevelParse = "Argentina";
                        } else if (userGP > levelConstants.level16) {
                            userLevelParse = "Amazon Rainforest";
                        }
                    } else if (userContinent.matches("Antarctica")) {
                        if (userGP >= 0 && userGP <= levelConstants.level1) {
                            userLevelParse = "Antarctica";
                        } else if (userGP > levelConstants.level1 && userGP <= levelConstants.level2) {
                            userLevelParse = "Arctic";
                        } else if (userGP > levelConstants.level2 && userGP <= levelConstants.level3) {
                            userLevelParse = "Indonesia";
                        } else if (userGP > levelConstants.level3 && userGP <= levelConstants.level4) {
                            userLevelParse = "Australian Desert";
                        } else if (userGP > levelConstants.level4 && userGP <= levelConstants.level5) {
                            userLevelParse = "Australian Forest";
                        } else if (userGP > levelConstants.level5 && userGP <= levelConstants.level6) {
                            userLevelParse = "China";
                        } else if (userGP > levelConstants.level6 && userGP <= levelConstants.level7) {
                            userLevelParse = "Egypt";
                        } else if (userGP > levelConstants.level7 && userGP <= levelConstants.level8) {
                            userLevelParse = "Madagascar";
                        } else if (userGP > levelConstants.level8 && userGP <= levelConstants.level9) {
                            userLevelParse = "U.K.";
                        } else if (userGP > levelConstants.level9 && userGP <= levelConstants.level10) {
                            userLevelParse = "France";
                        } else if (userGP > levelConstants.level10 && userGP <= levelConstants.level11) {
                            userLevelParse = "India";
                        } else if (userGP > levelConstants.level11 && userGP <= levelConstants.level12) {
                            userLevelParse = "Pacific Ocean";
                        } else if (userGP > levelConstants.level12 && userGP <= levelConstants.level13) {
                            userLevelParse = "Atlantic Ocean";
                        } else if (userGP > levelConstants.level13 && userGP <= levelConstants.level14) {
                            userLevelParse = "California";
                        } else if (userGP > levelConstants.level14 && userGP <= levelConstants.level15) {
                            userLevelParse = "Canada";
                        } else if (userGP > levelConstants.level15 && userGP <= levelConstants.level16) {
                            userLevelParse = "Argentina";
                        } else if (userGP > levelConstants.level16) {
                            userLevelParse = "Amazon Rainforest";
                        }
                    } else if (userContinent.matches("Africa")) {
                        if (userGP >= 0 && userGP <= levelConstants.level1) {
                            userLevelParse = "Egypt";
                        } else if (userGP > levelConstants.level1 && userGP <= levelConstants.level2) {
                            userLevelParse = "Madagascar";
                        } else if (userGP > levelConstants.level2 && userGP <= levelConstants.level3) {
                            userLevelParse = "Indonesia";
                        } else if (userGP > levelConstants.level3 && userGP <= levelConstants.level4) {
                            userLevelParse = "Australian Desert";
                        } else if (userGP > levelConstants.level4 && userGP <= levelConstants.level5) {
                            userLevelParse = "Australian Forest";
                        } else if (userGP > levelConstants.level5 && userGP <= levelConstants.level6) {
                            userLevelParse = "Antarctica";
                        } else if (userGP > levelConstants.level6 && userGP <= levelConstants.level7) {
                            userLevelParse = "India";
                        } else if (userGP > levelConstants.level7 && userGP <= levelConstants.level8) {
                            userLevelParse = "China";
                        } else if (userGP > levelConstants.level8 && userGP <= levelConstants.level9) {
                            userLevelParse = "U.K.";
                        } else if (userGP > levelConstants.level9 && userGP <= levelConstants.level10) {
                            userLevelParse = "France";
                        } else if (userGP > levelConstants.level10 && userGP <= levelConstants.level11) {
                            userLevelParse = "Arctic";
                        } else if (userGP > levelConstants.level11 && userGP <= levelConstants.level12) {
                            userLevelParse = "Pacific Ocean";
                        } else if (userGP > levelConstants.level12 && userGP <= levelConstants.level13) {
                            userLevelParse = "Atlantic Ocean";
                        } else if (userGP > levelConstants.level13 && userGP <= levelConstants.level14) {
                            userLevelParse = "California";
                        } else if (userGP > levelConstants.level14 && userGP <= levelConstants.level15) {
                            userLevelParse = "Canada";
                        } else if (userGP > levelConstants.level15 && userGP <= levelConstants.level16) {
                            userLevelParse = "Argentina";
                        } else if (userGP > levelConstants.level16) {
                            userLevelParse = "Amazon Rainforest";
                        }
                    } else if (userContinent.matches("Europe")) {
                        if (userGP >= 0 && userGP <= levelConstants.level1) {
                            userLevelParse = "U.K.";
                        } else if (userGP > levelConstants.level1 && userGP <= levelConstants.level2) {
                            userLevelParse = "France";
                        } else if (userGP > levelConstants.level2 && userGP <= levelConstants.level3) {
                            userLevelParse = "Indonesia";
                        } else if (userGP > levelConstants.level3 && userGP <= levelConstants.level4) {
                            userLevelParse = "Australian Desert";
                        } else if (userGP > levelConstants.level4 && userGP <= levelConstants.level5) {
                            userLevelParse = "Australian Forest";
                        } else if (userGP > levelConstants.level5 && userGP <= levelConstants.level6) {
                            userLevelParse = "Antarctica";
                        } else if (userGP > levelConstants.level6 && userGP <= levelConstants.level7) {
                            userLevelParse = "Egypt";
                        } else if (userGP > levelConstants.level7 && userGP <= levelConstants.level8) {
                            userLevelParse = "Madagascar";
                        } else if (userGP > levelConstants.level8 && userGP <= levelConstants.level9) {
                            userLevelParse = "India";
                        } else if (userGP > levelConstants.level9 && userGP <= levelConstants.level10) {
                            userLevelParse = "China";
                        } else if (userGP > levelConstants.level10 && userGP <= levelConstants.level11) {
                            userLevelParse = "Arctic";
                        } else if (userGP > levelConstants.level11 && userGP <= levelConstants.level12) {
                            userLevelParse = "Pacific Ocean";
                        } else if (userGP > levelConstants.level12 && userGP <= levelConstants.level13) {
                            userLevelParse = "Atlantic Ocean";
                        } else if (userGP > levelConstants.level13 && userGP <= levelConstants.level14) {
                            userLevelParse = "California";
                        } else if (userGP > levelConstants.level14 && userGP <= levelConstants.level15) {
                            userLevelParse = "Canada";
                        } else if (userGP > levelConstants.level15 && userGP <= levelConstants.level16) {
                            userLevelParse = "Argentina";
                        } else if (userGP > levelConstants.level16) {
                            userLevelParse = "Amazon Rainforest";
                        }
                    } else if (userContinent.matches("North America")) {
                        if (userGP >= 0 && userGP <= levelConstants.level1) {
                            userLevelParse = "Canada";
                        } else if (userGP > levelConstants.level1 && userGP <= levelConstants.level2) {
                            userLevelParse = "California";
                        } else if (userGP > levelConstants.level2 && userGP <= levelConstants.level3) {
                            userLevelParse = "Indonesia";
                        } else if (userGP > levelConstants.level3 && userGP <= levelConstants.level4) {
                            userLevelParse = "Australian Desert";
                        } else if (userGP > levelConstants.level4 && userGP <= levelConstants.level5) {
                            userLevelParse = "Australian Forest";
                        } else if (userGP > levelConstants.level5 && userGP <= levelConstants.level6) {
                            userLevelParse = "Antarctica";
                        } else if (userGP > levelConstants.level6 && userGP <= levelConstants.level7) {
                            userLevelParse = "Egypt";
                        } else if (userGP > levelConstants.level7 && userGP <= levelConstants.level8) {
                            userLevelParse = "Madagascar";
                        } else if (userGP > levelConstants.level8 && userGP <= levelConstants.level9) {
                            userLevelParse = "U.K.";
                        } else if (userGP > levelConstants.level9 && userGP <= levelConstants.level10) {
                            userLevelParse = "France";
                        } else if (userGP > levelConstants.level10 && userGP <= levelConstants.level11) {
                            userLevelParse = "Arctic";
                        } else if (userGP > levelConstants.level11 && userGP <= levelConstants.level12) {
                            userLevelParse = "Pacific Ocean";
                        } else if (userGP > levelConstants.level12 && userGP <= levelConstants.level13) {
                            userLevelParse = "Atlantic Ocean";
                        } else if (userGP > levelConstants.level13 && userGP <= levelConstants.level14) {
                            userLevelParse = "India";
                        } else if (userGP > levelConstants.level14 && userGP <= levelConstants.level15) {
                            userLevelParse = "China";
                        } else if (userGP > levelConstants.level15 && userGP <= levelConstants.level16) {
                            userLevelParse = "Argentina";
                        } else if (userGP > levelConstants.level16) {
                            userLevelParse = "Amazon Rainforest";
                        }
                    } else if (userContinent.matches("South America")) {
                        if (userGP >= 0 && userGP <= levelConstants.level1) {
                            userLevelParse = "Argentina";
                        } else if (userGP > levelConstants.level1 && userGP <= levelConstants.level2) {
                            userLevelParse = "Amazon Rainforest";
                        } else if (userGP > levelConstants.level2 && userGP <= levelConstants.level3) {
                            userLevelParse = "Indonesia";
                        } else if (userGP > levelConstants.level3 && userGP <= levelConstants.level4) {
                            userLevelParse = "Australian Desert";
                        } else if (userGP > levelConstants.level4 && userGP <= levelConstants.level5) {
                            userLevelParse = "Australian Forest";
                        } else if (userGP > levelConstants.level5 && userGP <= levelConstants.level6) {
                            userLevelParse = "Antarctica";
                        } else if (userGP > levelConstants.level6 && userGP <= levelConstants.level7) {
                            userLevelParse = "Egypt";
                        } else if (userGP > levelConstants.level7 && userGP <= levelConstants.level8) {
                            userLevelParse = "Madagascar";
                        } else if (userGP > levelConstants.level8 && userGP <= levelConstants.level9) {
                            userLevelParse = "U.K.";
                        } else if (userGP > levelConstants.level9 && userGP <= levelConstants.level10) {
                            userLevelParse = "France";
                        } else if (userGP > levelConstants.level10 && userGP <= levelConstants.level11) {
                            userLevelParse = "Arctic";
                        } else if (userGP > levelConstants.level11 && userGP <= levelConstants.level12) {
                            userLevelParse = "Pacific Ocean";
                        } else if (userGP > levelConstants.level12 && userGP <= levelConstants.level13) {
                            userLevelParse = "Atlantic Ocean";
                        } else if (userGP > levelConstants.level13 && userGP <= levelConstants.level14) {
                            userLevelParse = "California";
                        } else if (userGP > levelConstants.level14 && userGP <= levelConstants.level15) {
                            userLevelParse = "Canada";
                        } else if (userGP > levelConstants.level15 && userGP <= levelConstants.level16) {
                            userLevelParse = "China";
                        } else if (userGP > levelConstants.level16) {
                            userLevelParse = "India";
                        }
                    }

                    Log.i("userLevelDetected", userLevelParse);
                    saveDataToParse(userContinent, userLevelParse);
                } else {
                    Toast.makeText(mContext, "Error getting your level, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveDataToParse(String userContinentSaved, String userLevelParseSaved) {

        prefManager.addVariablesToSharedPreferences(userContinentSaved, userLevelParseSaved);
        Log.i("userLevel", userLevelParseSaved);

        parseUser = ParseUser.getCurrentUser();
        parseUser.put("presentUserContinent", userContinentSaved);
        parseUser.put("userLevel", userLevelParseSaved);
        parseUser.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    goToEarthActivity(GetVariablesForPermissions.this.mContext);
                } else {
                    Toast.makeText(GetVariablesForPermissions.this.mContext, "Error, saving your data! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}