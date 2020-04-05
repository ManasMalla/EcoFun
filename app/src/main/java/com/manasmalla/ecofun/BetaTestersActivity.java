package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class BetaTestersActivity extends AppCompatActivity {

    ListView betaTestersListView;
    Bitmap profile_bitmap;
    EcoFunDashboardListAdapter ecoFunDashboardListAdapter;
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

        setContentView(R.layout.activity_beta_testers);

        betaTestersListView = findViewById(R.id.betaTestersListView_betaTestersActivity);

        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
        parseUserParseQuery.whereEqualTo("betaTester", true);
        parseUserParseQuery.orderByAscending("createdAt");
        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    if (objects != null && objects.size() > 0){
                        for (final ParseUser parseUser: objects){
                            ParseFile file = parseUser.getParseFile("profilePic");

                            if (file != null) {
                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {

                                        if (e == null && data != null) {
                                            Log.i("username", parseUser.getUsername());
                                            profile_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            profile_image.add(profile_bitmap);
                                            name.add(parseUser.getUsername());
                                            level.add(parseUser.getString("userLevel"));
                                            value.add(String.valueOf(parseUser.get("greenPoints")));
                                            character.add("Blu");
                                            char_image.add(R.drawable.elephant);

                                            ecoFunDashboardListAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }else{
                        Toast.makeText(BetaTestersActivity.this, "Couldn't get beta testers! ", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(BetaTestersActivity.this, "Couldn't get beta testers! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ecoFunDashboardListAdapter = new EcoFunDashboardListAdapter(BetaTestersActivity.this, name, level, profile_image, value, character, char_image);

        betaTestersListView.setAdapter(ecoFunDashboardListAdapter);
    }
}