package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {


    ImageView jungleBackgroundImageView, animalCarImageView;
    TextView loginOrSignUpTextView, socialLoginButton;
    TextInputLayout username_textInputLayout, password_textInputLayout;
    TextInputEditText username_textInputEditText, password_textInputEditText;
    MaterialButton loginButton, orSignUpButton;
    boolean loginOrSignUp = true;
    boolean firstRun;
    Intent intent;
    Pair[] pairs;

    String username, password;
    //present button text ---> login - true, signup - false

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


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstRun){
            loginOrSignUpTextView.setText(getString(R.string.signUp));
            loginButton.setText(getString(R.string.signUp));
            orSignUpButton.setText(getString(R.string.orLogin));
            loginOrSignUp = false;
        }else{
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
        if (loginOrSignUp){
            loginOrSignUpTextView.setText(getString(R.string.signUp));
            loginButton.setText(getString(R.string.signUp));
            orSignUpButton.setText(getString(R.string.orLogin));
            loginOrSignUp = false;
        }else{
            loginOrSignUpTextView.setText(getString(R.string.login));
            loginButton.setText(getString(R.string.login));
            orSignUpButton.setText(getString(R.string.or_signup));
            loginOrSignUp = true;
        }
    }

    public void loginOnClick(View view) {

        username = username_textInputEditText.getText().toString();
        password = password_textInputEditText.getText().toString();

        if ((username == null || username.isEmpty()) && (password != null && !password.isEmpty())){
            //Username is only empty
            password_textInputLayout.setError("Please enter a username!");
        }else if ((password == null || password.isEmpty()) && (username != null && !username.isEmpty())){
            //Password is only empty
            password_textInputLayout.setError("Please enter a password!");
        }else if ((username == null || username.isEmpty()) && (password == null || password.isEmpty())){
            //Both Username and password are empty
            password_textInputLayout.setError("Please enter a username and password!");
        }else{
            //We have credentials!
            intent = new Intent(this, PermissionActivity.class);

            if (loginOrSignUp){
                //Login the user...
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null){
                            if (user != null){
                                intent.putExtra("firstRun", firstRun);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "Error, loging you into your account! Please try later", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Error, loging you into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                ///Signup the user
                ParseUser parseUser = new ParseUser();
                parseUser.setUsername(username);
                parseUser.setPassword(password);
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            intent.putExtra("firstRun", firstRun);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Error, loging you into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }

    }
}