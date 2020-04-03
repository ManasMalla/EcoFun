package com.manasmalla.ecofun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private static final int storageRquestCode = 240;
    Bitmap profileBitmap;
    byte[] byteArray = null;
    ParseFile profileParseFile = null;
    CircleImageView circleImageView;
    String username, password;
    TextInputLayout password_textInputLayout;
    TextInputEditText username_textInputEditText, password_textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_settings);

        circleImageView = findViewById(R.id.profilepicImageView_settingsActivity);
        username_textInputEditText = findViewById(R.id.up_username_edit_text_settingsActivity);
        password_textInputEditText = findViewById(R.id.up_password_edit_text_settingsActivity);
        password_textInputLayout = findViewById(R.id.up_password_text_input_settingsActivity);

        String sharedPrefProfilePic = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).getString("profileImage", null);

        if (sharedPrefProfilePic != null && !(sharedPrefProfilePic.isEmpty())){
            byte[] b = Base64.decode(sharedPrefProfilePic, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
           circleImageView.setImageBitmap(bitmap);
        }else {

            ParseUser currentParseUser = ParseUser.getCurrentUser();
            currentParseUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        ParseFile file = object.getParseFile("profilePic");

                        if (file != null) {
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e == null && data != null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);


                                        String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);

                                        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                                        SharedPreferences.Editor edit = shre.edit();
                                        edit.putString("profileImage", encodedImage);
                                        edit.apply();

                                        circleImageView.setImageBitmap(bitmap);

                                    }
                                }
                            });

                        }
                    } else {
                        Toast.makeText(SettingsActivity.this, "Error, getting your photo  from your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void updateUserProfileOnClick(View view) {
        username = username_textInputEditText.getText().toString();
        password = password_textInputEditText.getText().toString();

        ParseUser parseUser = ParseUser.getCurrentUser();

        if ((username == null || username.isEmpty()) && (password != null && !password.isEmpty())) {
            //Username is only empty so update password
            parseUser.setPassword(password);
            parseUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(SettingsActivity.this, "Successfully updated your login credentials, you can use them from now onwards!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SettingsActivity.this, "Error updating your login credentials! Please try later! "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if ((password == null || password.isEmpty()) && (username != null && !username.isEmpty())) {
            //Password is only empty so update username
            parseUser.setUsername(username);
            parseUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(SettingsActivity.this, "Successfully updated your login credentials, you can use them from now onwards!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SettingsActivity.this, "Error updating your login credentials! Please try later! "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if ((username == null || username.isEmpty()) && (password == null || password.isEmpty())) {
            //Both Username and password are empty
            password_textInputLayout.setError("Please enter a username and password!");
        } else {
            //Update both
            parseUser.setUsername(username);
            parseUser.setPassword(password);
            parseUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(SettingsActivity.this, "Successfully updated your login credentials, you can use them from now onwards!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SettingsActivity.this, "Error updating your login credentials! Please try later! "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void getProfilePhotoSettingsActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storageRquestCode);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {

                profileBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                profileBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byteArray = stream.toByteArray();


                profileParseFile = new ParseFile("profile.png", byteArray);
                profileParseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            ParseUser parseUser = ParseUser.getCurrentUser();
                            parseUser.put("profilePic", profileParseFile);
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null){
                                        circleImageView.setImageBitmap(profileBitmap);

                                    }else{
                                        Toast.makeText(SettingsActivity.this, "Error, saving your photo into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(SettingsActivity.this, "Error, saving your photo into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void uploadProfilePicOnClick(View view) {

        getProfilePhotoSettingsActivity();

    }

    public void deleteUserOnClick(View view) {

        ParseUser user = ParseUser.getCurrentUser();
        user.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {

                    PrefManager prefManager = new PrefManager(SettingsActivity.this);
                    prefManager.setFirstTimeLaunch(false);
                    //sharedPreferences Maybe needed to close the storage because this sharedoredfences affcts the login to parse casing invalid seession
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SettingsActivity.this, "Couldn't delete the account please try later :)", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}