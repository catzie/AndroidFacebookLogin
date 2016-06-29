package net.catzie.fb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.MessageDialog;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    public String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());


        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"/*, "user_birthday", "user_friends"*/));

        callbackManager = CallbackManager.Factory.create();

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "loginButton onclick listener fired!");
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "FBLogin Success. loginResult=" + loginResult.toString());

                        /**
                         * Facebook Access Token
                         */

                        final AccessToken accessToken = loginResult.getAccessToken();

                        GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                Log.d(TAG, user.optString("email"));
                                Log.d(TAG, user.optString("name"));
                                Log.d(TAG, user.optString("id"));
                                Log.d(TAG, user.toString());
                                Log.d(TAG, graphResponse.toString());


                            }
                        }).executeAsync();

                        Profile profile = Profile.getCurrentProfile();
                        String firstName = profile.getName();
                        Log.d(TAG, "profile pic URL = " + profile.getProfilePictureUri(40,40));
                        Log.d(TAG, "profile URL = " + profile.getLinkUri());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "FBLogin onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "FBLogin exception=" + exception.toString());
                    }
                });

        Log.d(TAG, "onCreate end ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "result!");
    }
}
