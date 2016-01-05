
package edu.bluejack151.Shine.shine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    Location lastLocation;
    private LocationManager locManager;
    private LocationListener locListener;
    private FragmentManager mFragmentManager;
    boolean hasLocationSinceStarted;
    private ArrayList<ShineUser> mShineUsers = new ArrayList<ShineUser>();
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("home", "home on create ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFragmentManager = getSupportFragmentManager();


        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeNavbar();

        Intent NotifPoolIntent = new Intent(getApplicationContext(), NotifPoolService.class);
        startService(NotifPoolIntent);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("location changed", "asdasd");
                if(lastLocation == null || haversine(lastLocation.getLatitude(), lastLocation.getLongitude(),
                        location.getLatitude(), location.getLongitude()) > 2) {
                    lastLocation = location;
                    RequestQueue mRequestQue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                            getString(R.string.laravel_API_url)
                                    + "users?lat=" + lastLocation.getLatitude()
                                    + "&long=" + lastLocation.getLongitude(), null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    if (!hasLocationSinceStarted) {
                                        hasLocationSinceStarted = true;
                                        Log.d("users respon", response.toString());
                                        try {
                                            JSONArray userArray = response.getJSONArray("users");
                                            for (int i = 0; i < userArray.length(); i++) {
                                                String encodedBitmap = userArray.getJSONObject(i).getString("photo");
                                                String name = userArray.getJSONObject(i).getString("name");
                                                String gender = userArray.getJSONObject(i).getString("gender");
                                                String id = userArray.getJSONObject(i).getString("id");
                                                String schoolName = userArray.getJSONObject(i).getString("school_name");
                                                String email = userArray.getJSONObject(i).getString("email");
                                                String bio = userArray.getJSONObject(i).getString("bio");
                                                ShineUser baru = new ShineUser(id, name, schoolName, gender, encodedBitmap);
                                                baru.updateUser(ShineUser.MAP_USER_EMAIL, email);
                                                baru.updateUser(ShineUser.MAP_USER_BIO, bio);
                                                mShineUsers.add(baru);
                                            }

                                        } catch (JSONException e) {
                                        }
                                        UserVoteFragment fragment = UserVoteFragment.createFragment();
                                        mFragmentManager.beginTransaction()
                                                .add(R.id.fragment_container, fragment, "USER_VOTE")
                                                .commit();
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
                    locManager.removeUpdates(this);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("users respon", "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("users respon", "onProviderEnabled");

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("asd", "provider is disabled");
            }
        };
    }

    public ArrayList<ShineUser> getShineUsers()
    {
        return this.mShineUsers;
    }

    /*
    * ini tadi mau buat kalo usernya habis, minta request lagi gitu
    * tapi gajadi haha
    * */
    public ArrayList<ShineUser> newRequest()
    {
        RequestQueue mRequestQue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getString(R.string.laravel_API_url)
                        + "users?lat="+21//lastLocation.getLatitude()
                        +"&long="+22/*lastLocation.getLongitude()*/, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (!hasLocationSinceStarted) {
                            hasLocationSinceStarted = true;
                            Log.d("users respon", response.toString());
                            try {
                                JSONArray userArray = response.getJSONArray("users");
                                mShineUsers.clear();
                                for (int i = 0; i < userArray.length(); i ++){
                                    String encodedBitmap = userArray.getJSONObject(i).getString("photo");
                                    String name = userArray.getJSONObject(i).getString("name");
                                    String gender = userArray.getJSONObject(i).getString("gender");
                                    String id = userArray.getJSONObject(i).getString("id");
                                    String email = userArray.getJSONObject(i).getString("email");
                                    String schoolName = userArray.getJSONObject(i).getString("school_name");
                                    mShineUsers.add(new ShineUser(id, name, schoolName, gender, encodedBitmap));

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
        return this.mShineUsers;
    }

    private void setUser(String name, String school, int profilePicture) {
        mTextViewUsername.setText(name);
        mTextViewSchool.setText(school);
        mImageViewProfpic.setImageResource(profilePicture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        Log.d("homeac", "home activity created");
    }

    @Override
    protected void onStart() {
        Log.d("onstart", "home on start called");

        lastLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(locManager.getLastKnownLocation("gps") != null){
            lastLocation = locManager.getLastKnownLocation("gps");
        }


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
                    setUser(ShineUser.getCurrentUser().get(ShineUser.MAP_USER_NAME),
                            ShineUser.getCurrentUser().get(ShineUser.MAP_USER_SCHOOL),
                            R.drawable.com_facebook_profile_picture_blank_portrait);
                }
            });
        }else{
            setUser(ShineUser.getCurrentUser().get(ShineUser.MAP_USER_NAME),
                    ShineUser.getCurrentUser().get(ShineUser.MAP_USER_SCHOOL),
                    R.drawable.com_facebook_profile_picture_blank_portrait);
        }
        hasLocationSinceStarted = false;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission", "some permission not configured");
        } else
            locManager.requestLocationUpdates("gps", 3600000, 5000f, locListener);

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

        mNavbarItems.add(new NavItem("MyProfile", "Set your profile setting and more", R.drawable.com_facebook_button_icon));
        mNavbarItems.add(new NavItem("Top School", "Most popular school", R.drawable.com_facebook_button_icon));
        mNavbarItems.add(new NavItem("Logout", "Logout from Shine", R.drawable.com_facebook_button_icon));

        mNavbarLayout.setDrawerListener(mActionBarDrawerToggle);
        refreshNavbar();
    }

    public void selectMenu(int position) {
        //start intent from the nav bar here broh
        Toast.makeText(HomeActivity.this, mNavbarItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if (position == 1) { // ke SchoolList
            SchoolListFragment fragment = SchoolListFragment.createFragment(HomeActivity.this);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, "School_List")
                    .addToBackStack(null)
                    .commit();
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
    public void onProfilePictureClicked(final HashMap<String,String> userInfo){
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
    public void voteRequest(String object_id, String rate){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> body = new HashMap<>();
        body.put("object_id", object_id);
        body.put("rate", rate);
        JSONObject postBody = new JSONObject(body);
        Log.d("json", postBody.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, getString(R.string.laravel_API_url) + "vote", postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("vote", response.toString());
                        Toast.makeText(HomeActivity.this, "Vote success", Toast.LENGTH_SHORT).show();
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

    private double haversine(double lat1, double lon1, double lat2, double lon2){
        double R = 6371;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }
}