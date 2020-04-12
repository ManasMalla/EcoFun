package com.manasmalla.ecofun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {


    ImageView jungleBackgroundImageView, animalCarImageView;
    TextView loginOrSignUpTextView, socialLoginButton;
    private static final int storageRquestCode = 200;
    TextInputLayout username_textInputLayout, password_textInputLayout, email_textInputLayout;
    MaterialButton loginButton, orSignUpButton;
    TextInputEditText username_textInputEditText, password_textInputEditText, email_textInputEditText;
    MaterialCardView emailMaterialCardView;
    boolean firstRun;
    Intent intent;
    Pair[] pairs;
    boolean loginOrSignUp = true, emailAlreadyAvailable = false;
    FirebaseAuth mAuth;
    Bitmap profileBitmap;
    byte[] byteArray = null;
    ParseFile profileParseFile = null;

    String username, password;
    //present button text ---> login - true, signup - false


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == storageRquestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        }
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

        setContentView(R.layout.activity_login);

        PrefManager prefManager = new PrefManager(getApplicationContext());
        firstRun = prefManager.isFirstTimeLaunch();

        assignUIVariables();

        //clearPreferences();

        mAuth = FirebaseAuth.getInstance();

    }

    private void clearPreferences() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear com.manasmalla.ecofun");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstRun) {
            loginOrSignUpTextView.setText(getString(R.string.signUp));
            loginButton.setText(getString(R.string.signUp));
            orSignUpButton.setText(getString(R.string.orLogin));
            loginOrSignUp = false;
        } else {
            loginOrSignUpTextView.setText(getString(R.string.login));
            loginButton.setText(getString(R.string.login));
            orSignUpButton.setText(getString(R.string.or_signup));
            loginOrSignUp = true;
        }
    }

    private void assignUIVariables() {
        jungleBackgroundImageView = findViewById(R.id.jungleBackgroundImageView_loginActivity);
        animalCarImageView = findViewById(R.id.animalsCarImageView_loginActivity);

        loginOrSignUpTextView = findViewById(R.id.loginTitle_loginActivity);
        socialLoginButton = findViewById(R.id.orLoginSocial_loginActivity);

        username_textInputLayout = findViewById(R.id.username_inputLayout_loginActivity);
        password_textInputLayout = findViewById(R.id.password_textlayout_loginActivity);
        username_textInputEditText = findViewById(R.id.username_editText_loginActivity);
        password_textInputEditText = findViewById(R.id.password_editText_loginActivity);

        loginButton = findViewById(R.id.loginButton_loginActivity);
        orSignUpButton = findViewById(R.id.orLoginButton_loginActivity);

        emailMaterialCardView = findViewById(R.id.emailIDMaterialCardView);
        email_textInputLayout = findViewById(R.id.email_textlayout_loginActivity);
        email_textInputEditText = findViewById(R.id.email_editText_loginActivity);
    }

    public void orLoginSocialOnClick(View view) {

        Intent intentToSocialLoginActivity = new Intent(this, SocialLoginActivity.class);

        pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(jungleBackgroundImageView, getString(R.string.jungleImageViewTransition));
        pairs[1] = new Pair<View, String>(animalCarImageView, getString(R.string.animalsCarImageViewTransition));
        pairs[2] = new Pair<View, String>(findViewById(R.id.socialLogin_loginActivity), getString(R.string.transition_loginWithSocial));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intentToSocialLoginActivity, activityOptions.toBundle());
        } else {
            startActivity(intentToSocialLoginActivity);
        }

    }

    public void orSignUpOnClick(View view) {
        if (loginOrSignUp) {
            loginOrSignUpTextView.setText(getString(R.string.signUp));
            loginButton.setText(getString(R.string.signUp));
            orSignUpButton.setText(getString(R.string.orLogin));
            loginOrSignUp = false;
        } else {
            loginOrSignUpTextView.setText(getString(R.string.login));
            loginButton.setText(getString(R.string.login));
            orSignUpButton.setText(getString(R.string.or_signup));
            loginOrSignUp = true;
        }
    }

    public void loginOnClick(View view) {

        username = username_textInputEditText.getText().toString();
        password = password_textInputEditText.getText().toString();

        if ((username == null || username.isEmpty()) && (password != null && !password.isEmpty())) {
            //Username is only empty
            password_textInputLayout.setError("Please enter a username!");
        } else if ((password == null || password.isEmpty()) && (username != null && !username.isEmpty())) {
            //Password is only empty
            password_textInputLayout.setError("Please enter a password!");
        } else if ((username == null || username.isEmpty()) && (password == null || password.isEmpty())) {
            //Both Username and password are empty
            password_textInputLayout.setError("Please enter a username and password!");
        } else {
            //We have credentials!
            intent = new Intent(this, PermissionActivity.class);

            if (loginOrSignUp) {
                //Login the user...
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            if (user != null) {
                                intent.putExtra("firstRun", firstRun);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error, logging you into your account! Please try later", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error, logging you into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ///Signup the user
                emailMaterialCardView.setVisibility(View.VISIBLE);

            }
        }

    }

    public void updateEmailIdOnClick(View view) {

        if (byteArray == null) {
            Toast.makeText(this, "Please upload a profile pic first!", Toast.LENGTH_SHORT).show();
            getProfilePhoto();
        } else {

            String emailID = email_textInputEditText.getText().toString();

            ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
            userParseQuery.whereEqualTo("email", emailID);
            userParseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            emailAlreadyAvailable = true;
                        } else {
                            emailAlreadyAvailable = false;
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error, Finding your email id! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (emailAlreadyAvailable) {
                email_textInputLayout.setError("Email is already in use! Please login!");
            } else {

                profileParseFile = new ParseFile("profile.png", byteArray);
                profileParseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            ParseUser parseUser = new ParseUser();
                            parseUser.setUsername(username);
                            parseUser.setPassword(password);
                            parseUser.setEmail(email_textInputEditText.getText().toString());
                            parseUser.put("betaTester", true);
                            parseUser.put("profilePic", profileParseFile);
                            parseUser.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        intent.putExtra("firstRun", firstRun);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Error, signing you into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(LoginActivity.this, "Error, saving your photo into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }

    }

    public void getProfilePicture(View view) {
       getProfilePhoto();
    }

    private void getProfilePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storageRquestCode);
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

                CircleImageView circleImageView = findViewById(R.id.profilePicUploadCircleImageView);
                circleImageView.setImageBitmap(profileBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}