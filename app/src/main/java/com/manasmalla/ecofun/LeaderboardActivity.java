package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardActivity extends AppCompatActivity {

    ListView leaderboardListView;
    Bitmap profile_bitmap;
    boolean isUserTop10;
    EcoFunDashboardListAdapter ecoFunLeaderboardListAdapter;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> level = new ArrayList<>();
    private ArrayList<Bitmap> profile_image = new ArrayList<>();
    private ArrayList<String> value = new ArrayList<>();
    private ArrayList<String> character = new ArrayList<>();
    private ArrayList<Integer> char_image = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_leaderboard);

        leaderboardListView = findViewById(R.id.leaderboard_listView);

        isUserTop10 = false;

        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
        parseUserParseQuery.orderByDescending("greenPoints");
        parseUserParseQuery.setLimit(10);

        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects != null && objects.size() > 0) {
                        ParseUser parseUserLeaderboard = ParseUser.getCurrentUser();
                        for (ParseUser userAvailable : objects) {
                            if (userAvailable.getObjectId().matches(parseUserLeaderboard.getObjectId())) {
                                isUserTop10 = true;
                                updateCardViewDashboard(userAvailable);
                            }
                        }

                        if (isUserTop10) {
                            Log.i("userAvaialble", "yes");
                            for (final ParseUser parseUser : objects) {
                                if (objects.indexOf(parseUser) <= 2) {
                                    addDetails(parseUser, objects.indexOf(parseUser) + 1, true, true);
                                } else {
                                    addDetails(parseUser, objects.indexOf(parseUser) + 1, false, true);
                                }
                            }
                        } else {
                            Log.i("userAvaialble", "no");
                            ParseUser parseUserCurrent = ParseUser.getCurrentUser();
                            parseUserCurrent.fetchInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        addDetails((ParseUser) object, 100, false, false);
                                    }
                                }
                            });
                            for (final ParseUser parseUser : objects) {
                                if (objects.indexOf(parseUser) <= 2) {
                                    addDetails(parseUser, objects.indexOf(parseUser) + 1, true, true);
                                } else {
                                    addDetails(parseUser, objects.indexOf(parseUser) + 1, false, true);
                                }
                            }

                        }

                    } else {
                        Toast.makeText(LeaderboardActivity.this, "Couldn't get beta testers! ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LeaderboardActivity.this, "Couldn't get beta testers! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ecoFunLeaderboardListAdapter = new EcoFunDashboardListAdapter(LeaderboardActivity.this, name, level, profile_image, value, character, char_image);

        leaderboardListView.setAdapter(ecoFunLeaderboardListAdapter);

    }

    public void addDetails(final ParseUser parseUser, final int positionLeaderboard, final boolean isTopThree, final boolean isInList) {
        ParseFile file = parseUser.getParseFile("profilePic");

        if (file != null) {
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                    if (e == null && data != null) {
                        profile_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        profile_image.add(profile_bitmap);

                        if (isInList) {
                            if (isTopThree) {
                                name.add(positionLeaderboard + ". \uD83C\uDFC6" + parseUser.getUsername());
                            } else {
                                name.add(positionLeaderboard + ". " + parseUser.getUsername());
                            }
                        } else {
                            name.add("\uD83E\uDDD1 . " + parseUser.getUsername());
                        }
                        level.add(parseUser.getString("userLevel"));
                        value.add(String.valueOf(parseUser.get("greenPoints")));
                        character.add("Blu");
                        char_image.add(R.drawable.elephant);

                        ecoFunLeaderboardListAdapter.notifyDataSetChanged();
                        Log.i("nameList: ", name.toString());
                    }
                }
            });

        } else {
            profile_image.add(BitmapFactory.decodeResource(LeaderboardActivity.this.getResources(), R.drawable.add_profilepic));
            if (isInList) {
                if (isTopThree) {
                    name.add(positionLeaderboard + ". \uD83C\uDFC6" + parseUser.getUsername());
                } else {
                    name.add(positionLeaderboard + ". " + parseUser.getUsername());
                }
            } else {
                name.add("\uD83E\uDDD1 . " + parseUser.getUsername());
            }
            level.add(parseUser.getString("userLevel"));
            value.add(String.valueOf(parseUser.get("greenPoints")));
            character.add("Blu");
            char_image.add(R.drawable.elephant);

            Log.i("nameList: ", name.toString());

            ecoFunLeaderboardListAdapter.notifyDataSetChanged();
        }

    }

    public void updateCardViewDashboard(final ParseUser parseUserRetrieved) {
        ParseFile file = parseUserRetrieved.getParseFile("profilePic");

        if (file != null) {
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                    if (e == null && data != null) {
                        profile_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        CircleImageView profileCircleImageView = findViewById(R.id.profileImageViewLeaderBoard);
                        profileCircleImageView.setImageBitmap(profile_bitmap);
                        TextView usernameTextViewLeaderboard, levelTextViewLeaderboard, greenPointsTextViewLeaderboard, characterTextViewLeaderboard;
                        usernameTextViewLeaderboard = findViewById(R.id.usernameTextView_dashboardActivity);
                        levelTextViewLeaderboard = findViewById(R.id.levelTextView_leaderboardActivity);
                        greenPointsTextViewLeaderboard = findViewById(R.id.greenPointsLeaderboardTextView);
                        characterTextViewLeaderboard = findViewById(R.id.characterDashboardTextView);
                        usernameTextViewLeaderboard.setText(parseUserRetrieved.getUsername());
                        levelTextViewLeaderboard.setText(parseUserRetrieved.getString("userLevel"));
                        greenPointsTextViewLeaderboard.setText(String.valueOf(parseUserRetrieved.get("greenPoints")));
                        characterTextViewLeaderboard.setText("Blu");
                    }
                }
            });
        }
    }


    public void shareDashboardOnClick(View view){

    }
}