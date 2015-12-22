package com.shineapptpa.rei.shine;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    Button mLogin;
    Button mRegis;
    EditText email;
    EditText pwd;
    private CallbackManager mCallbackManager;
    public static Profile profile;
    public final static String FORM_CODE = "form_code";
    public final static int FORM_WITH_PASSWORD = 1;
    public final static int FORM_WITHOUT_PASSWORD = 2;
    public final static String USER_EMAIL = "user_email";
    public final static String USER_FULLNAME = "user_fullname";
    public final static String USER_PASSWORD = "user_password";
    public final static int REQUEST_COMPLETE_REGISTRATION = 9;
    // FUNGSI DIPANGGIL ABIS FB LOGIN
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();

            ProfileTracker mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                    AccessToken acsToken = loginResult.getAccessToken();
                    profile = newProfile;
                    Log.d("profilechanged", profile.getFirstName());

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                    try {
                                        Log.d("asd", jsonObject.getString("email"));
                                        syncParseUser(jsonObject.getString("email"), profile.getName());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email");
                    request.setParameters(parameters);
                    request.executeAsync();


                    this.stopTracking();
                }
            };
            mProfileTracker.startTracking();
        }

        @Override
        public void onCancel() {
            Log.d("cancelled", "asdasd");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("error", e.getMessage());
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init parse. required for parse shits
        Parse.initialize(this, "lxgqT6WpdVx74zhmLgv9cZbOyhSkKEvKo22m1T4K", "eAtZqm1s49seXvrvspoFpYx3IVCCfO9vwTvDffQ0");

        // init facebook SDK. required for displaying facebook shits
        FacebookSdk.sdkInitialize(getApplicationContext());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.shineapptpa.rei.shine",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(mCallbackManager, mCallback);
        Log.d("asd", "wei");
        mLogin = (Button)findViewById(R.id.login_btn);
        mRegis = (Button)findViewById(R.id.regis_btn);
        email = (EditText)findViewById(R.id.email);
        pwd = (EditText)findViewById(R.id.pwd);


        if(ParseUser.getCurrentUser() != null){
            Log.d("user not null", "start new acti");
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            //ParseUser.logOut();
        }
        // LOGIN PAKE PARSE
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ParseUser.logInInBackground(email.getText().toString(), pwd.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null) {
                            //success login, lanjut ke home activity

                            ShineUser.setCurrentUser(ParseUser.getCurrentUser().get("username").toString(),
                                    ParseUser.getCurrentUser().get("school").toString(),
                                    ParseUser.getCurrentUser().get("gender").toString(),
                                    ParseUser.getCurrentUser().get("name").toString());
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);
                        } else {

                        }
                    }
                });
            }
        });

        // signup with Parse
        mRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lanjut ke complete registration.
                Intent i = new Intent(getApplicationContext(),CompleteSignUpActivity.class);
                i.putExtra(FORM_CODE, FORM_WITHOUT_PASSWORD);
                i.putExtra(USER_EMAIL, email.getText().toString());
                i.putExtra(USER_PASSWORD, pwd.getText().toString());
                startActivity(i);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_COMPLETE_REGISTRATION){
            if(resultCode == Activity.RESULT_OK){
                Log.d("logout", "logout result ok");
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }else{
                Log.d("logout", "logout result cancelled");
                LoginManager.getInstance().logOut();

            }
        }
    }

    // if a user logged in with facebook, check if user exist in Parse database
    private void syncParseUser(final String email, final String fullname){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", email);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // ga ada error
                    if(objects.size() <= 0){
                        //user login pake fb, tapi email ga ada di database PARSE, lanjut complete registration
                        Log.d("fb login", "daftar baru");
                        Intent i = new Intent(getApplicationContext(), CompleteSignUpActivity.class);
                        i.putExtra(FORM_CODE, FORM_WITH_PASSWORD);
                        i.putExtra(USER_EMAIL, email);
                        i.putExtra(USER_FULLNAME, fullname);
                        startActivityForResult(i, REQUEST_COMPLETE_REGISTRATION);
                    }else if(objects.size() > 0){
                        //user login pake fb, email udah ada di Parse, berarti data lengkap,
                        // login'in juga pake PARSE, terus masuk ke HomeActivity,
                        Log.d("fb login", "login");
                        ShineUser.setCurrentUser(objects.get(0).get("username").toString(),
                                objects.get(0).get("school").toString(),
                                objects.get(0).get("gender").toString(),
                                objects.get(0).get("name").toString());
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                    }
                } else {
                    // ada error
                    Log.d("fb login", e.getMessage());
                }
            }
        });
    }


}
