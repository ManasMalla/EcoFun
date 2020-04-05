package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LiscencesActivity extends AppCompatActivity {

        ListView listViewLiscences;
        EcoFunLiscenceListAdapter listviewLiscencesAdapter;
        ArrayList<String> arrayStringLiscences, arrayStringDescription;
        ArrayList<Integer> arrayImageViewLiscences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_liscences);
        arrayStringLiscences = new ArrayList<>();
        arrayStringDescription = new ArrayList<>();
        arrayImageViewLiscences = new ArrayList<>();
        arrayStringLiscences.add("Svaga/ Shutterstock.com ");
        arrayStringLiscences.add("Whatarethe7continents.com©");
        arrayStringLiscences.add("Parse Server");
        arrayStringLiscences.add("Amazon Web Services");
        arrayStringDescription.add(getString(R.string.shutterstockLiscence));
        arrayStringDescription.add(getString(R.string.mapContinentLiscence));
        arrayStringDescription.add(getString(R.string.ParseString));
        arrayStringDescription.add(getString(R.string.AWSString));
        arrayImageViewLiscences.add(R.drawable.shutterstock);
        arrayImageViewLiscences.add(R.drawable.continentsseven);
        arrayImageViewLiscences.add(R.drawable.parse);
        arrayImageViewLiscences.add(R.drawable.aws);
        listviewLiscencesAdapter = new EcoFunLiscenceListAdapter(LiscencesActivity.this, arrayStringLiscences, arrayStringDescription, arrayImageViewLiscences);
        listViewLiscences = findViewById(R.id.liscencesListView_liscencesActivity);
        listViewLiscences.setAdapter(listviewLiscencesAdapter);
        listviewLiscencesAdapter.notifyDataSetChanged();
    }
}