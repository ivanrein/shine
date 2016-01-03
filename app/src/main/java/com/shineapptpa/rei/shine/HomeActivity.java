package com.shineapptpa.rei.shine;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity {


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
    private HashMap<String, String> currUser;
    Location lastLocation;

    private LocationManager locManager;
    private LocationListener locListener;

    boolean hasLocationSinceStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("home", "home on create ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //ShineUser.setCurrentUser();
        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeNavbar();

        setUser("aa",
                "Binus University",
                R.drawable.com_facebook_profile_picture_blank_square);

        Intent NotifPoolIntent = new Intent(getApplicationContext(), NotifPoolService.class);
        startService(NotifPoolIntent);


        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
                RequestQueue mRequestQue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        getString(R.string.laravel_API_url)
                                + "users?lat="+lastLocation.getLatitude()
                                +"&long="+lastLocation.getLongitude(), null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if (!hasLocationSinceStarted) {
                                    hasLocationSinceStarted = true;
                                    Log.d("users respon", response.toString());
                                    /* ini dapetnya udah jsonarray namanya users dalam bentuk list user yang bener
                                    ( belom di vote sama sekolahnya relevan), ditambah profpicnya. bentuknya kayak gini
                                    "users":[{"id":3,"name":"Dr. Allen Boyer","email":"hStiedemann@Nicolas.com","gender":"Female","school_id":1,"photo_id":null,"photo":null},
                                    {"id":5,"name":"Santiago Halvorson","email":"Demarco61@Franecki.com","gender":"Female","school_id":1,"photo_id":null,"photo":null},
                                    {"id":8,"name":"Miss Eleonore Rempel","email":"Ryder55@gmail.com","gender":"Male","school_id":1,"photo_id":null,"photo":null}
                                    ]}

                                    buat ngambilnya, kayak kodingan di bwh
                                     */
                                    try {
                                        JSONArray userArray = response.getJSONArray("users");
                                        for (int i = 0; i < userArray.length(); i ++){
                                            String encodedBitmap = userArray.getJSONObject(i).getString("photo");
                                            String name = userArray.getJSONObject(i).getString("name");
                                            String gender = userArray.getJSONObject(i).getString("gender");
                                            String email = userArray.getJSONObject(i).getString("email");
                                            String id = userArray.getJSONObject(i).getString("id");
                                            String schoolName = userArray.getJSONObject(i).getString("school_name");
                                            String bio = userArray.getJSONObject(i).getString("bio");

                                        }
                                    } catch (JSONException e) {

                                    }




                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("connection", error.toString());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("utoken", CustomPref.getUserAccessToken(getApplicationContext()));
                        return params;
                    }

                };
                mRequestQue.add(request);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("asd", "provider is disabled");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission", "some permission not configured");
        } else
            locManager.requestLocationUpdates("gps", 3600000, 5000f, locListener);
    }

    private void setUser(String name, String school, int profilePicture) {
        mTextViewUsername.setText(name);
        mTextViewSchool.setText(school);
        mImageViewProfpic.setImageResource(profilePicture);

        
//        currUser = ShineUser.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(true); // ini emang di comment

        Log.d("homeac", "home activity created");
        initializeNavbar();
    }

    @Override
    protected void onStart() {
        Log.d("onstart", "home on start called");
        super.onStart();
        if (CustomPref.getUserAccessToken(this) == null) {
            LoginManager.getInstance().logOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        if (ShineUser.getCurrentUser() == null) {
            Log.d("onstart home", "currentuser null");
            ShineUser.fetchCurrentUser(this, new CustomCallback() {
                @Override
                public void callback() {

                }
            });
        }
        hasLocationSinceStarted = false;
        if(lastLocation == null){
            // kasih animasi loading mungkin
        }
        if (locManager.getProvider("gps") == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("permission", "permission not configured.");
            }
            locManager.requestLocationUpdates("gps", 3600000, 5000f, locListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initializeNavbar() {
        mTextViewUsername = (TextView) findViewById(R.id.tvUsername);
        mTextViewSchool = (TextView) findViewById(R.id.tvSchool);
        mImageViewProfpic = (ImageView) findViewById(R.id.avatar);
        mRelativeLayoutUserInfo = (RelativeLayout) findViewById(R.id.userinfo);

        mRelativeLayoutUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ShineUser.getCurrentUser() != null) {
                    Intent intent = MyProfileActivity.newIntent(HomeActivity.this, ShineUser.getCurrentUser());
                    startActivity(intent);
                } else {
                    if (!ShineUser.fetching) {
                        ShineUser.fetchCurrentUser(getApplicationContext(), new CustomCallback() {
                            @Override
                            public void callback() {

                            }
                        });
                    }
                }
            }
        });


        mNavbarLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavbarList = (ListView) findViewById(R.id.lvNavBar);
        mNavbarPanel = (RelativeLayout) findViewById(R.id.NavBar_panel);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mNavbarLayout, R.string.navbar_open,
                R.string.navbar_close) {
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

    public void selectMenu(int position) {
        //start intent from the nav bar here broh
        Toast.makeText(HomeActivity.this, mNavbarItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if (position == 0) { // ke myprofile

        }
        if (position == 2) { // logout
            if (CustomPref.resetAccessToken(getApplicationContext())) {
                LoginManager.getInstance().logOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    public void refreshNavbar() {
        NavBarAdapter adapter = new NavBarAdapter(this, mNavbarItems);
        mNavbarList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mActionBarDrawerToggle.onOptionsItemSelected(item))
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

    /**
     * ini ngehubungin ke MyProfileActivity doank sih. abis yg gambar" itu jadi baru ini dipanggil di clicklistenernya
     * @param userInfo
     */
    private void onProfilePictureClicked(final HashMap<String,String> userInfo){
        Intent e = MyProfileActivity.newIntent(this, userInfo);
        startActivity(e);
    }
    /**tinggal dipanggil tiap x tombol vote diteken, ga usah diubah" lagi
     *
     * @param object_id
     * object ID = id user yang di vote.
     * @param rate
     * rate = rate nya berapa
     *
     * ga usah pake currentuser id soalnya pake token di header
     */
    private void voteRequest(Integer object_id, Integer rate){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, Integer> body = new HashMap<>();
        body.put("object_id", object_id);
        body.put("rate", rate);
        JSONObject postBody = new JSONObject(body);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, getString(R.string.laravel_API_url) + "vote", postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("vote", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("vote", error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("utoken", CustomPref.getUserAccessToken(getApplicationContext()));
                return params;
            }
        };
        queue.add(req);

    }

}