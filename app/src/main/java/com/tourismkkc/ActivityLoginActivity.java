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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class ActivityLoginActivity extends Activity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private LoginButton loginButton;
    private List<String> PERMISSIONS = Arrays.asList("public_profile", "email");
    private CallbackManager callbackManager;
    private String keyHash = "";
    private String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initial();
        initInstances();
        initCallbackManager();
        getUserInfo();
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    private void initial() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
    }

    private void initInstances() {
        editTextEmail = (EditText) findViewById(R.id.login_edit_email);
        editTextPassword = (EditText) findViewById(R.id.login_edit_password);
        buttonLogin = (Button) findViewById(R.id.login_btn_login);
        buttonRegister = (Button) findViewById(R.id.login_btn_register);
        loginButton = (LoginButton) findViewById(R.id.login_button);
    }

    private void initCallbackManager() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(PERMISSIONS);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "initCallbackManager : " + loginResult.toString());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        Log.d(TAG, "getDataFromFacebook : " + response.toString());
                        try {
                            String strEmail = response.getJSONObject().get("email").toString();
                            String strFirstName = response.getJSONObject().get("first_name").toString();
                            String strLastName = response.getJSONObject().get("last_name").toString();
                            String strID = response.getJSONObject().get("id").toString();
                            Log.d(TAG, "getDataFromFacebook str : " + strEmail + " : " + strFirstName + " : " + strLastName + " : " + strID);
//                            DataRegister dataRegister = new DataRegister(strEmail, "", strFirstName, strLastName, strID);
//                            LoadAPIFacebookRegister loadAPIFacebookRegister = new LoadAPIFacebookRegister();
//                            loadAPIFacebookRegister.execute(dataRegister);
                            new LoadAPIFacebookRegister().execute(new DataRegister(strEmail, "", strFirstName, strLastName, strID));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "initCallbackManager : onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "initCallbackManager : " + error.toString());
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                editStr();
//                DataLogin dataLogin = new DataLogin(getStrEmail(), getStrPassword());
//                LoadApi loadApi = new LoadApi();
//                loadApi.execute(dataLogin);
                new LoadApi().execute(new DataLogin(getStrEmail(), getStrPassword()));
                break;
            case R.id.login_btn_register:
//                Intent intent = new Intent(getApplicationContext(), ActivityRegisterActivity.class);
//                startActivity(intent);
                startActivity(new Intent(getApplicationContext(), ActivityRegisterActivity.class));
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
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    }

    class LoadAPIFacebookRegister extends AsyncTask<DataRegister, Void, APIStatus> {

        private APIConnect apiConnect = new APIConnect();
        private APIStatus apiStatus = new APIStatus();

        @Override
        protected APIStatus doInBackground(DataRegister... params) {
            return apiStatus = apiConnect.register(params[0]);
        }

        @Override
        protected void onPostExecute(APIStatus result) {
            Toast.makeText(getApplicationContext(), apiStatus.getReason(), Toast.LENGTH_LONG).show();
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    }
}
