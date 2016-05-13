package com.example.peterpan.nearme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.peterpan.nearme.model.User;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.Firebase;

import java.util.Arrays;


/**
 * Created by Peterpan on 5/9/2016 AD.
 */
public class LoginActivity extends AppCompatActivity {
    private Firebase ref;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    private Button loginButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://nearmeapp.firebaseio.com/");

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                saveAccessToken(accessToken.getToken());

                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"Login attempt cancelled.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this,"Login attempt failed.",Toast.LENGTH_SHORT).show();
            }
        };
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                LoginManager.getInstance().registerCallback(callbackManager, callback);
            }
        });
    }

    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);
            Toast.makeText(LoginActivity.this, "FACEBOOK LOGIN NAJA" , Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onError(FacebookException e) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        deleteAccessToken();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void deleteAccessToken() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //User logged out
                    clearToken();
                }
            }
        };
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            String name = profile.getName();
            String imageUrl = profile.getProfilePictureUri(300,300).toString();

            user.setName(name);
            user.setImageUrl(imageUrl);

            Firebase userRef = ref.child("users").child(profile.getId());

            userRef.child("name").setValue(name);
            userRef.child("imageUrl").setValue(imageUrl);
            userRef.child("token").setValue(getToken());

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("user_object",user);
            intent.putExtras(mBundle);
            intent.putExtra("user_id", profile.getId());

            startActivity(intent);

        }
    }

    public void saveAccessToken(String token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString("token", null);
    }

    public void clearToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}