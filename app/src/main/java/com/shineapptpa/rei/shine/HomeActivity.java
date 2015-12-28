package com.shineapptpa.rei.shine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.AddressConstants;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    public static ListView mNavbarList;
    private final static int GOOGLE_PLAY_UPDATE_REQUEST = 10;
    private RelativeLayout mNavbarPanel;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mNavbarLayout;
    private ImageView mImageViewProfpic;
    private TextView mTextViewUsername, mTextViewSchool;
    private RelativeLayout mRelativeLayoutUserInfo;
    private ArrayList<NavItem> mNavbarItems = new ArrayList<NavItem>();
    private ArrayList<Integer> mPhotoResources;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, String> currUser;
    Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPhotoResources = new ArrayList<Integer>();
        mPhotoResources.add(R.drawable.dummy_1);
        mPhotoResources.add(R.drawable.dummy_2);
        mPhotoResources.add(R.drawable.dummy_6);
        mPhotoResources.add(R.drawable.dummy_4);
        mPhotoResources.add(R.drawable.dummy_5);
        mPhotoResources.add(R.drawable.dummy_5);
        initializeNavbar();
        setUser("Erick Marchelino", "Binus University", R.drawable.com_facebook_profile_picture_blank_square);
    }

    private void setUser(String name, String school, int profilePicture)
    {
        mTextViewUsername.setText(name);
        mTextViewSchool.setText(school);
        mImageViewProfpic.setImageResource(profilePicture);

        currUser = ShineUser.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(true); // ini emang di comment
//        if(mGoogleApiClient == null){
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//            Log.d("homeac", "google api client is null");
//        }
        Log.d("homeac", "home activity created");
        initializeNavbar();


    }

    @Override
    protected void onStart() {
//        if(lastLocation == null) {
//            mGoogleApiClient.connect();
//        }
        super.onStart();
    }

    @Override
    protected void onStop() {
    //    mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void initializeNavbar()
    {
        mTextViewUsername = (TextView) findViewById(R.id.tvUsername);
        mTextViewSchool = (TextView) findViewById(R.id.tvSchool);
        mImageViewProfpic = (ImageView) findViewById(R.id.avatar);
        mRelativeLayoutUserInfo = (RelativeLayout) findViewById(R.id.userinfo);

        mRelativeLayoutUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MyProfileActivity.newIntent(
                        HomeActivity.this,
                        "Erick Marchelino",
                        new Date(1995, 3, 17),
                        "nanannaa",
                        "Male",
                        mPhotoResources
                        );
                startActivity(intent);
            }
        });


        mNavbarLayout  = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavbarList = (ListView) findViewById(R.id.lvNavBar);
        mNavbarPanel = (RelativeLayout) findViewById(R.id.NavBar_panel);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mNavbarLayout, R.string.navbar_open,
                R.string.navbar_close)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mNavbarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectMenu(position);
            }
        });

        //dummy data
        mNavbarItems.add(new NavItem("MyProfile", "Set your profile setting and more", R.drawable.com_facebook_button_icon));
        mNavbarItems.add(new NavItem("Coba2", "descccss", R.drawable.com_facebook_button_icon));
        mNavbarItems.add(new NavItem("Logout", "Logout from Shine", R.drawable.com_facebook_button_icon));
        mTextViewUsername.setText("Guest");
        mTextViewUsername.setText("Binus University");

        mNavbarLayout.setDrawerListener(mActionBarDrawerToggle);
        refreshNavbar();
    }

    public void selectMenu(int position)
    {
        //start intent from the nav bar here broh
        Toast.makeText(HomeActivity.this, mNavbarItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if(position == 0){ // ke myprofile

        }
        if(position == 2){ // logout
            if(CustomPref.resetAccessToken(getApplicationContext())){
                LoginManager.getInstance().logOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    public void refreshNavbar()
    {
        NavBarAdapter adapter = new NavBarAdapter(this, mNavbarItems);
        mNavbarList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mActionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onConnected(Bundle bundle) {
//        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if(lastLocation != null){
//            Log.d("location", "location not null");
//            // fetch user accordingly
//            RequestQueue mRequestQue = Volley.newRequestQueue(getApplicationContext());
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.laravel_API_url) + "schools", null,
//                    new Response.Listener<JSONObject>(){
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                Log.d("token", response.getJSONObject("token").toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                Log.d("connection", "Volley GET failed");
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("connection", error.getMessage().toString());
//                        }
//                    }){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String>  params = new HashMap<String, String>();
//                    params.put("token", "asd");
//                    return super.getHeaders();
//                }
//            };
//        }else{
//            //display error message: can't get location
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("connection", (" " + connectionResult.getErrorCode()));
        GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, GOOGLE_PLAY_UPDATE_REQUEST).show();
    }

    //handling connection google API error
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == GOOGLE_PLAY_UPDATE_REQUEST){
//            if(resultCode == RESULT_OK){
//                mGoogleApiClient.connect();
//            }
//        }
    }
}
