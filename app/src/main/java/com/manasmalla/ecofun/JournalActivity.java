package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class JournalActivity extends AppCompatActivity {

    ListView journalListView;
    ArrayList<String> activity_name = new ArrayList<>();
    ArrayList<String> activity_distance = new ArrayList<>();
    ArrayList<Integer> activity_image = new ArrayList<>();
    ArrayList<String> activity_value = new ArrayList<>();
    ArrayList<String> activity_date = new ArrayList<>();
    EcoFunListAdapter ecoFunListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_journal);

        journalListView = findViewById(R.id.journalListView);

        getDatabaseJournal();

        ecoFunListAdapter = new EcoFunListAdapter(JournalActivity.this,activity_name,activity_distance,activity_image,activity_value,activity_date);

        journalListView.setAdapter(ecoFunListAdapter);

    }

    public void getDatabaseJournal(){
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Activities");
        parseQuery.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());
        parseQuery.orderByDescending("updatedAt");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //Log.i("ValueOfObjects", objects.toString());
                    for (ParseObject parseObject : objects) {

                        Log.i("DidGetData", parseObject.getString("activity"));
                        activity_name.add(parseObject.getString("activity"));
                        activity_distance.add(String.valueOf(parseObject.getInt("distance")) + " m");
                        if (parseObject.getInt("GreenPoints") == 0){
                            activity_value.add(String.valueOf(parseObject.getInt("CO2")) + " CO2Points");
                        }else if (parseObject.getInt("CO2") == 0){
                            activity_value.add(String.valueOf(parseObject.getInt("GreenPoints")) + " GP");
                        }else{
                            if (parseObject.getString("activity").matches("In Vehicle")){
                                activity_value.add(String.valueOf(parseObject.getInt("CO2")) + " CO2Points");
                            }else {
                                activity_value.add(String.valueOf(parseObject.getInt("GreenPoints")) + " GP");
                            }
                        }
                        Log.i("Date",parseObject.getCreatedAt().toString());
                        activity_date.add(parseObject.getCreatedAt().toString());
                        if (parseObject.getString("activity").matches("Walking")){
                            activity_image.add(R.drawable.round_directions_walk_24);
                        }else  if (parseObject.getString("activity").matches("In Vehicle")){
                            activity_image.add(R.drawable.round_directions_car_24);
                        }else  if (parseObject.getString("activity").matches("On Bicycle")){
                            activity_image.add(R.drawable.round_directions_bike_24);
                        }else  if (parseObject.getString("activity").matches("Running")){
                            activity_image.add(R.drawable.round_directions_run_24);
                        }
                        //journalArrayList.add(parseObject.getString("activity") + " \n" + parseObject.getInt("distance") + "m \n" + parseObject.getInt("CO2") + " CO\u2082Points\n" + parseObject.getInt("GreenPoints") + " GreenPoints");
                        ecoFunListAdapter.notifyDataSetChanged();

                    }
                }else{
                    Toast.makeText(JournalActivity.this, "Error, getting details, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void reloadDatabaseOnClick(View view){

        getDatabaseJournal();

    }
}