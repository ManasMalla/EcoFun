package com.manasmalla.ecofun;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionDialogRequester extends Application {
    List<String> permissionsToBeRequested;

    public List<String> checkPermissions(Context context) {

        permissionsToBeRequested = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToBeRequested.add("ACTIVITY_RECOGNITION");
            } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToBeRequested.add("LOCATION");
            } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToBeRequested.add("BACKGROUND_LOCATION");
            } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToBeRequested.add("STORAGE");
            }
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToBeRequested.add("LOCATION");
            } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToBeRequested.add("STORAGE");
            }
        }
        return permissionsToBeRequested;
    }

    public void setLayoutView(Context context, TextView title, TextView description, ImageView icon) {
        permissionsToBeRequested = checkPermissions(context);
        if (permissionsToBeRequested.get(0).matches("ACTIVITY_RECOGNITION")) {
            title.setText(R.string.ACTIVITY_RECOGNITION);
            description.setText(R.string.activity_recognition_description);
            icon.setImageResource(R.drawable.round_directions_run_24);
        } else if (permissionsToBeRequested.get(0).matches("LOCATION")) {
            title.setText(R.string.FINE_LOCATION);
            description.setText(R.string.location_description);
            icon.setImageResource(R.drawable.round_location_on_24);
        } else if (permissionsToBeRequested.get(0).matches("BACKGROUND_LOCATION")) {
            title.setText(R.string.BACKGROUND_LOCATION);
            description.setText(R.string.location_background_description);
            icon.setImageResource(R.drawable.round_my_location_24);
        } else if (permissionsToBeRequested.get(0).matches("STORAGE")) {
            title.setText(R.string.EXTERNAL_STORAGE);
            description.setText(R.string.storage_description);
            icon.setImageResource(R.drawable.round_camera_alt_24);
        }
    }

}
