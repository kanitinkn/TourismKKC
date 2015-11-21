package com.tourismkkc;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    ProfilePictureView profilePictureView;

    private TextView userName;
    private Button postLinkButton;
    private Button postPictureButton;

    private static final String PERMISSION = "publish_actions";

    // ชนิดของการโพสต์ที่รอดำเนินการ
    private enum PendingAction {
        NONE,
        POST_LINK,
        POST_PICTURE
    }

    // การโพสต์ที่รอดำเนินการ ซึ่งจะดำเนินการหลังจากได้รับสิทธิ์ publish_actions แล้ว
    private PendingAction pendingAction = PendingAction.NONE;

    // เมธอดเริ่มต้นกระบวนการโพสต์
    private void performPublish(PendingAction action) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    private void handlePendingAction() {
        PendingAction oldPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;

        switch (oldPendingAction) {
            case NONE:
                break;
            case POST_LINK:
                postLink();
                break;
            case POST_PICTURE:
                break;
        }
    }

    private void postLink() {
        Profile profile = Profile.getCurrentProfile();

        //รายละเอียดของลิงค์ที่จะโพสต์ลงเฟสบุ๊ค
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("idevbear.com")
                .setContentDescription("ทดสอบโปรแกรม")
                .setContentUrl(Uri.parse("http://www.idevbear.com"))
                .setImageUrl(Uri.parse("http://postto.me/1b/2rc.jpg"))
                .build();

        if (profile != null && hasPublishPermission()) {
            ShareApi.share(content, shareCallback);
        } else {
            pendingAction = PendingAction.POST_LINK;
            LoginManager.getInstance().logInWithReadPermissions(this,
                    Arrays.asList(
                            "publish_actions",
                            "public_profile",
                            "user_friends",
                            "email",
                            "user_birthday",
                            "user_education_history",
                            "user_hometown",
                            "user_location"
                    ));
        }
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null &&
                accessToken.getPermissions().contains("publish_actions");
    }

    private FacebookCallback<Sharer.Result> shareCallback =
            new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    if (result.getPostId() != null) {
                        String title = "";
                        String id = result.getPostId();
                        String msg = String.format("Post ID: %s", id);
                        showResult(title, msg);
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    String title = "เกิดข้อผิดพลาดในการโพสต์";
                    String msg = error.getMessage();
                    showResult(title, msg);
                }

                private void showResult(String title, String alertMessage) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(title)
                            .setMessage(alertMessage)
                            .setPositiveButton("OK", null)
                            .show();
                }
            };


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handlePendingAction();
                        updateUI();
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

//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "user_education_history", "user_hometown", "user_location"));
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d("LOG", "loginResult: " + loginResult.toString());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("LOG", "onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("LOG", "error: " + error.toString());
//            }
//        });

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userName = (TextView) findViewById(R.id.user_name);
        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture);
        postLinkButton = (Button) findViewById(R.id.post_link_button);
        postPictureButton = (Button) findViewById(R.id.post_picture_button);


        // create profileTracker
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,
                                                   Profile currentProfile) {
                updateUI();
            }
        };

        /*accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                Log.d("LOG", "oldAccessToken: " + oldAccessToken.toString());
                Log.d("LOG", "currentAccessToken: " + currentAccessToken.toString());
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();*/

        postLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPublish(PendingAction.POST_LINK);
            }
        });

    }

    private void updateUI() {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (loggedIn && (profile != null)) {
            profilePictureView.setProfileId(profile.getId());
            userName.setText(profile.getName());
            postLinkButton.setEnabled(true);
            postPictureButton.setEnabled(true);
        } else {
            profilePictureView.setProfileId(null);
            userName.setText(null);
            postLinkButton.setEnabled(false);
            postPictureButton.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
