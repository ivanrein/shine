package com.shineapptpa.rei.shine;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
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
//import com.parse.LogInCallback;
//import com.parse.Parse;
//import com.parse.ParseException;
//import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
                    Log.d("profilechanged", acsToken.getApplicationId());
                    Log.d("profilechanged", acsToken.getPermissions().toString());
                    Log.d("profilechanged", acsToken.getToken());
                    Log.d("profilechanged", acsToken.getUserId());

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                    try {
                                        Log.d("asd", jsonObject.getString("email"));
                                        syncLaravelUser(jsonObject.getString("email"), profile.getName());
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
//        Parse.initialize(this, "lxgqT6WpdVx74zhmLgv9cZbOyhSkKEvKo22m1T4K",
//                "eAtZqm1s49seXvrvspoFpYx3IVCCfO9vwTvDffQ0");

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

        if(CustomPref.getUserAccessToken(getApplicationContext()) != null){
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        }



        // LOGIN ke backend laravel
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue mQue = Volley.newRequestQueue(getApplicationContext());
                HashMap<String, String> map = new HashMap<String, String>(6);
                map.put("email", email.getText().toString());
                map.put("password", pwd.getText().toString());
                JSONObject userInfo = new JSONObject(map);
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                        getString(R.string.laravel_API_url) + "login", userInfo,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //dapet token, save ke preferences, masuk ke home activity
                                //bentuknya kayak gini {"result":"success","token":"KGucVHEmBytREzb0C1ZnT0mat9YnpQWhsm2aOkXzfqEOFPjf1Vj2neOHNq7I"}
                                //SharedPreferences pref = getSharedPreferences("")
                                try {
                                    Log.d("login", response.toString());
                                    if(response.getString("result").equals("success")) {
                                        String acToken = response.getString("token");
                                        JSONObject userInfos = response.getJSONObject("user");
                                        JSONObject schoolInfo = userInfos.getJSONObject("school");
                                        Log.d("login", schoolInfo.toString());
                                        CustomPref.setUserAccessToken(getApplicationContext(),
                                                acToken);
                                        ShineUser.resetCurrent();
                                        ShineUser.setCurrentUser(userInfos, schoolInfo);
                                        Intent i = new Intent(getApplicationContext(),
                                                HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //tampilin notif error
                                Log.d("login error", error.toString());
                            }
                        }
                );
                mQue.add(req);

            }
        });

        // signup biasa
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
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }else{
                LoginManager.getInstance().logOut();
            }
        }
    }

    // if a user logged in with facebook, check if user exist in Parse database
    private void syncLaravelUser(final String email, final String fullname){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final HashMap<String, String> userInformation = new HashMap<String, String>(6);
        userInformation.put("email", email);
        JSONObject jsonInfo = new JSONObject(userInformation);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                getString(R.string.laravel_API_url) + "CheckUser", jsonInfo,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Log.d("synclaravel", response.toString());
                            String user = response.getString("user");
                            Log.d("synclaravel", user.toString());
                            if(user != "null") {
                                Log.d("sync laravel user", "getting acces token after fb login");
                                getAccessTokenAfterFacebookLogin(email);
                            }else{
                                Log.d("sync laravel user", "go to complete form");
                                Intent i = new Intent(getApplicationContext(), CompleteSignUpActivity.class);
                                i.putExtra(FORM_CODE, FORM_WITH_PASSWORD);
                                i.putExtra(USER_EMAIL, email);
                                i.putExtra(USER_FULLNAME, fullname);
                                startActivityForResult(i, REQUEST_COMPLETE_REGISTRATION);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sync laravel user error", error.toString());
                    }
        });
        requestQueue.add(req);

    }

    private void getAccessTokenAfterFacebookLogin(String email){
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        final HashMap<String, String> userInformation = new HashMap<String, String>(6);
        userInformation.put("email", email);
        JSONObject jsonInfo = new JSONObject(userInformation);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getString(R.string.laravel_API_url) + "facebooklogin", jsonInfo,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("getaccesstoken", "login facebook sukses");
                        Log.d("getaccesstoken", response.toString());
                        try {
                            String acToken =  response.getString("token");
                            JSONObject userInfos = response.getJSONObject("user");
                            JSONObject schoolInfo = userInfos.getJSONObject("school");
                            CustomPref.setUserAccessToken(getApplicationContext(),
                                    acToken);
                            ShineUser.resetCurrent();
                            ShineUser.setCurrentUser(userInfos, schoolInfo);
                            Intent i = new Intent(getApplicationContext(),
                                    HomeActivity.class);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("getaccesstoken", "login facebook error");
                        Log.d("getaccesstoken", error.toString());

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("fbtoken", AccessToken.getCurrentAccessToken().getToken());
                return params;
            }
        };
        que.add(request);

    }


}
