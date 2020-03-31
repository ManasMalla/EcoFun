package com.manasmalla.ecofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SocialLoginActivity extends AppCompatActivity {

    private static final String TAG = SocialLoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 001;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    boolean googleSignedIn = false, facebookSignedIn = false, twitterSignedIn = false, instagramSignedIn = false, socialSignedIn;
    private FirebaseAuth mAuth;
    String[] socialSignedInStringArray;
    String socialSignedInProvider;
    ImageView googleSignInButton, facebookSignInButton, twitterSignInButton, instagramSignInButton;
    boolean emailAlreadyAvailable;
    String socialUserIDParse;
    MaterialCardView webViewMaterialCardView;
    WebView webView;

    public static String sentenceCaseForText(String text) {

        if (text == null) return "";

        int pos = 0;
        boolean capitalize = true;
        StringBuilder sb = new StringBuilder(text);

        while (pos < sb.length()) {

            if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {

                sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
            } else if (!capitalize && !Character.isWhitespace(sb.charAt(pos))) {

                sb.setCharAt(pos, Character.toLowerCase(sb.charAt(pos)));
            }

            if (sb.charAt(pos) == '.' || (capitalize && Character.isWhitespace(sb.charAt(pos)))) {

                capitalize = true;
            } else {

                capitalize = false;
            }

            pos++;
        }

        return sb.toString();
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

        setContentView(R.layout.activity_social_login);

        mAuth = FirebaseAuth.getInstance();

        emailAlreadyAvailable = false;

        PrefManager prefManager = new PrefManager(SocialLoginActivity.this);
        socialSignedInStringArray = prefManager.userSocialLoginDetails();

        googleSignedIn = false;
        facebookSignedIn = false;
        twitterSignedIn = false;
        instagramSignedIn = false;

        googleSignInButton = findViewById(R.id.google_sign_in_button);
        facebookSignInButton = findViewById(R.id.facebook_loginButton);
        twitterSignInButton = findViewById(R.id.twitter_loginButton);
        instagramSignInButton = findViewById(R.id.instagram_loginButton);

        webViewMaterialCardView = findViewById(R.id.webViewMaterialCardView_socialLoginActivity);
        webView = findViewById(R.id.webView_socialLoginActivity);

        if (socialSignedInStringArray[1].matches("true")) {
            socialSignedIn = true;
            socialSignedInProvider = socialSignedInStringArray[0];
            if (socialSignedInProvider.matches("GOOGLE")) {
                googleSignedIn = true;
                googleSignInButton.setImageResource(R.drawable.google_logout);
            } else if (socialSignedInProvider.matches("FACEBOOK")) {
                facebookSignedIn = true;
                facebookSignInButton.setImageResource(R.drawable.facebook_logout);
            } else if (socialSignedInProvider.matches("TWITTER")) {
                twitterSignedIn = true;
                twitterSignInButton.setImageResource(R.drawable.twitter_logout);
            } else if (socialSignedInProvider.matches("INSTAGRAM")) {
                instagramSignedIn = true;
                instagramSignInButton.setImageResource(R.drawable.instagram_logout);
            }
            updateUI(mAuth.getCurrentUser(), socialSignedInProvider);
        } else {
            socialSignedIn = false;
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SocialLoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(SocialLoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        facebookSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebookSignedIn) {
                    FirebaseAuth.getInstance().signOut();
                    facebookSignInButton.setImageResource(R.drawable.facebook_login);
                    facebookSignedIn = false;
                    getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE).edit().putBoolean("isFacebookSignedIn", false).apply();
                } else {
                    LoginManager.getInstance().logInWithReadPermissions(SocialLoginActivity.this, Arrays.asList("email", "public_profile"));
                }
            }
        });

    }

    public void instagramSignIn(View view) {

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("instagramRedirect", webView.getUrl());
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.instagram.com/oauth/authorize?client_id=499049874307431&redirect_uri=https://manasmalla.github.io/&scope=user_profile%2Cuser_media&response_type=code");
        webViewMaterialCardView.setVisibility(View.VISIBLE);
        Log.i("instagram", webView.getUrl());


    }

    public void twitterOnClick(View view) {
        if (twitterSignedIn) {
            FirebaseAuth.getInstance().signOut();
            twitterSignInButton.setImageResource(R.drawable.twitter_login);
            twitterSignedIn = false;
            getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE).edit().putBoolean("isTwitterSignedIn", false).apply();
        } else {
            OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

            Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask
                        .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        updateUI(mAuth.getCurrentUser(), "TWITTER");
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure.
                                    }
                                });
            } else {
                mAuth
                        .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                        .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        updateUI(mAuth.getCurrentUser(), "TWITTER");
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure.
                                    }
                                });
            }
        }
    }

    public void googleSignIn(View view) {
        if (googleSignedIn) {
            //GOOGLE logged in
            FirebaseAuth.getInstance().signOut();
            googleSignInButton.setImageResource(R.drawable.signin_with_google);
            googleSignedIn = false;
            getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE).edit().putBoolean("isGoogleSignedIn", false).apply();
        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, "GOOGLE");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null, null);
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, "FACEBOOK");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SocialLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null, null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user, final String identifier) {

        if (user != null) {
            Toast.makeText(this, user.getDisplayName() + " <" + user.getProviderData().get(1).getEmail() + ">", Toast.LENGTH_SHORT).show();
            String userPhotoURL = String.valueOf(user.getPhotoUrl());
            if (identifier.matches("FACEBOOK")) {
                userPhotoURL = userPhotoURL + "?height=512";
            } else if (identifier.matches("TWITTER")) {
                userPhotoURL = userPhotoURL.replace("_normal", "");
            }
            userPhotoURL = userPhotoURL.replace("s96-c", "s512-c");
            Log.i("profileURL", userPhotoURL);

            if (identifier.matches("GOOGLE")) {
                googleSignedIn = true;
                getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE).edit().putBoolean("isGoogleSignedIn", true).apply();
                googleSignInButton.setImageResource(R.drawable.google_logout);
            } else if (identifier.matches("FACEBOOK")) {
                facebookSignedIn = true;
                getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE).edit().putBoolean("isFacebookSignedIn", true).apply();
                facebookSignInButton.setImageResource(R.drawable.facebook_logout);
            } else if (identifier.matches("TWITTER")) {
                twitterSignedIn = true;
                getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE).edit().putBoolean("isTwitterSignedIn", true).apply();
                twitterSignInButton.setImageResource(R.drawable.twitter_logout);
            }

            //Check Email ID
            final String emailID = user.getProviderData().get(1).getEmail();

            final String identifierName = sentenceCaseForText(identifier);
            final String profilePhotoDownloadURL = userPhotoURL;

            ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
            userParseQuery.whereEqualTo("email", emailID);
            userParseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            emailAlreadyAvailable = true;
                            socialUserIDParse = objects.get(0).getUsername();
                        } else {
                            emailAlreadyAvailable = false;
                        }

                        if (emailAlreadyAvailable) {

                            Log.i("userExists", socialUserIDParse);
                            if (socialUserIDParse != null || !socialUserIDParse.isEmpty()) {
                                ParseUser.logInInBackground(socialUserIDParse, "password", new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if (e == null) {
                                            Intent intent = new Intent(SocialLoginActivity.this, PermissionActivity.class);
                                            intent.putExtra("firstRun", true);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SocialLoginActivity.this, "Error, logging you into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {

                            Bitmap bitmap = downloadImage(profilePhotoDownloadURL);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            final ParseFile profilePic = new ParseFile("profile" + identifierName + ".png", byteArray);
                            profilePic.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        ParseUser parseUser = new ParseUser();
                                        parseUser.setUsername(emailID);
                                        parseUser.setPassword("password");
                                        parseUser.setEmail(emailID);
                                        parseUser.put("profilePic", profilePic);
                                        parseUser.signUpInBackground(new SignUpCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(getApplicationContext(), "Please update your username later in Settings Activity! Please dont change your passowrd as that can affect your social account!", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(SocialLoginActivity.this, PermissionActivity.class);
                                                    intent.putExtra("firstRun", true);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(SocialLoginActivity.this, "Error, signing you into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(SocialLoginActivity.this, "Error, saving your photo into your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    } else {
                        Toast.makeText(SocialLoginActivity.this, "Error, finding your account! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    public Bitmap downloadImage(String profileURL) {

        // https://upload.wikimedia.org/wikipedia/en/a/aa/Bart_Simpson_200px.png

        ImageDownloader task = new ImageDownloader();
        Bitmap myImage = null;

        try {
            myImage = task.execute(profileURL).get();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return myImage;
    }

    public static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}