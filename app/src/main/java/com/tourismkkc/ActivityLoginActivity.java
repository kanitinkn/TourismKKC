package com.tourismkkc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ActivityLoginActivity extends Activity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;

    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;

    private String keyHash;
    private String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        initInstances();
        initCallbackManager();
        getUserInfo();

        profileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                updateUI();

            }

        };

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

    }

    private void initCallbackManager() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                updateUI();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                Log.d("LOG", "response:- " + response.toString());
                                String email = null;
                                try {
                                    email = response.getJSONObject().get("email").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("LOG", "email:- " + email);
                            }
                        }
                );
                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,name,email,gender");

            }

            @Override
            public void onCancel() {

                updateUI();

            }

            @Override
            public void onError(FacebookException error) {

                updateUI();

            }
        });

    }

    private void initInstances() {

        editTextEmail = (EditText) findViewById(R.id.login_edit_email);
        editTextPassword = (EditText) findViewById(R.id.login_edit_password);
        buttonLogin = (Button) findViewById(R.id.login_btn_login);
        buttonRegister = (Button) findViewById(R.id.login_btn_register);

    }

    private void getUserInfo() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.tourismkkc", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash += Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d(TAG, keyHash);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }

    }

    private void updateUI() {

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (loggedIn && (profile != null)) {

            // get images
            System.out.println(profile.getProfilePictureUri(180, 180));
            System.out.println(profile.getLinkUri());
        } else {

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_btn_login:

                editStr();
                DataLogin dataLogin = new DataLogin(getStrEmail(), getStrPassword());
                LoadApi loadApi = new LoadApi();
                loadApi.execute(dataLogin);

                break;
            case R.id.login_btn_register:
                Intent intent = new Intent(getApplicationContext(), ActivityRegisterActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void editStr() {

        setStrEmail(editTextEmail.getText().toString());
        setStrPassword(editTextPassword.getText().toString());

    }

    @Override
    protected void onResume() {

        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

    }

    @Override
    protected void onPause() {

        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        profileTracker.stopTracking();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    private String strEmail, strPassword;

    class LoadApi extends AsyncTask<DataLogin, Void, APIStatus> {

        APIConnect apiConnect = new APIConnect();
        APIStatus apiStatus = new APIStatus();

        @Override
        protected APIStatus doInBackground(DataLogin... params) {

            return apiStatus = apiConnect.login(params[0]);

        }

        @Override
        protected void onPostExecute(APIStatus apiStatus) {

            super.onPostExecute(apiStatus);
            Toast.makeText(getApplicationContext(), apiStatus.getReason(), Toast.LENGTH_SHORT).show();

            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        }
    }

}
