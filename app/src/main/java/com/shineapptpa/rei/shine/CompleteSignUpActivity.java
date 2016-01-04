package com.shineapptpa.rei.shine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
//import com.parse.ParseException;
//import com.parse.ParseUser;
//import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteSignUpActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> schools;
    ArrayList<Integer> schools_id;
    Button completeRegistrationBtn;
    EditText mFullNameOrPwd;
    EditText mBio;
    RequestQueue mRequestQue;
    Spinner mSchoolSpinner;
    RadioButton male;
    RadioButton female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sign_up);
        setResult(RESULT_CANCELED);
        male = (RadioButton)findViewById(R.id.radMale);
        female = (RadioButton)findViewById(R.id.radFemale);

        mSchoolSpinner = (Spinner)findViewById(R.id.schools);
        mBio = (EditText)findViewById(R.id.bio);
        schools = new ArrayList<>();
        schools_id = new ArrayList<>();
        completeRegistrationBtn = (Button)findViewById(R.id.submit_btn);
        final boolean formWithPassword = getIntent().getIntExtra(MainActivity.FORM_CODE,
                MainActivity.FORM_WITHOUT_PASSWORD) == MainActivity.FORM_WITH_PASSWORD;
        mFullNameOrPwd = (EditText)findViewById(R.id.password_or_fullname);
        if(!formWithPassword){
            mFullNameOrPwd.setHint("Full Name");
            mFullNameOrPwd.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        completeRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRequestQue == null){
                    mRequestQue = Volley.newRequestQueue(getApplicationContext());
                }
                String gender;
                String userFullName;
                String userPassword;
                String bio = mBio.getText().toString();
                String userEmail = getIntent().getStringExtra(MainActivity.USER_EMAIL);

                if(male.isChecked()) gender = "male";
                else gender = "female";
                //FORM WITH PASSWORD means the user sign up with FACEBOOK. This means
                // WE GOT NAME AND EMAIL, BUT NOT PASSWORD. SO WE HAVE TO PROVIDE FORM FOR PASSWORD.
                if(formWithPassword){
                    Log.d("register", "masuk sign up facebook");
                    Log.d("register", AccessToken.getCurrentAccessToken().getToken());
                    userFullName = getIntent().getStringExtra(MainActivity.USER_FULLNAME);
                    userPassword = mFullNameOrPwd.getText().toString();
                    final HashMap<String, String> userInformation = new HashMap<String, String>(6);
                    userInformation.put("email", userEmail);
                    userInformation.put("gender", gender);
                    userInformation.put("bio", bio);
                    userInformation.put("name", userFullName);
                    userInformation.put("password", userPassword);
                    userInformation.put("school_id", schools_id.get((Integer)mSchoolSpinner.getSelectedItemPosition()).toString());

                    JSONObject jsonInfo = new JSONObject(userInformation);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getString(R.string.laravel_API_url) + "facebooklogin", jsonInfo,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("register", "register laravel sukses");
                                    Log.d("register", response.toString());

                                    try {
                                        String acToken = response.getString("token");
                                        JSONObject userInfos = response.getJSONObject("user");
                                        JSONObject schoolInfo = userInfos.getJSONObject("school");
                                        CustomPref.setUserAccessToken(getApplicationContext(),
                                                acToken);
                                        ShineUser.resetCurrent();
                                        ShineUser.setCurrentUser(userInfos, schoolInfo);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("register", "register laravel error");
                                    Log.d("register", error.toString());

                                }
                            }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("fbtoken", AccessToken.getCurrentAccessToken().getToken());
                            return params;
                        }
                    };
                    mRequestQue.add(request);

                }else{
                    //USER SIGN UP TO USUAL BACKEND. WE PROVIDE FORM FOR FULLNAME, BUT NOT PASSWORD.
                    Log.d("register", "masuk sign up biasa");
                    userFullName = mFullNameOrPwd.getText().toString();
                    userPassword = getIntent().getStringExtra(MainActivity.USER_PASSWORD);

                    final HashMap<String, String> userInformation = new HashMap<String, String>(6);
                    userInformation.put("email", userEmail);
                    userInformation.put("gender", gender);
                    userInformation.put("bio", bio);
                    userInformation.put("name", userFullName);
                    userInformation.put("password", userPassword);
                    userInformation.put("school_id", schools_id.get((Integer)mSchoolSpinner.getSelectedItemPosition()).toString());

                    JSONObject jsonInfo = new JSONObject(userInformation);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getString(R.string.laravel_API_url) + "register", jsonInfo,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("register", "register laravel sukses");
                                    Log.d("register", response.toString());
                                    //udah sukses. ga dapet token soalnya kalo register biasa ga langsung ke login. tinggal kasih notif register sukses
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("register", "register laravel error");
                                    Log.d("register", error.toString());
                                    // masuk sini error, biasanya gara" email udah taken. tinggal kasih notif.
                                }
                            }){

                    };
                    mRequestQue.add(request);
                }
            }
        });

        fetchSchool();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete_sign_up, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    private void fetchSchool() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Fetching schools");
        progress.setMessage("Please wait..");
        progress.setCancelable(false);
        mRequestQue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, getString(R.string.laravel_API_url) + "schools", null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray arr = null;
                                try {
                                    arr = response.getJSONArray("schools");
                                    for (int i = 0; i < arr.length(); i++) {
                                        schools.add(arr.getJSONObject(i).getString("name"));
                                        schools_id.add(arr.getJSONObject(i).getInt("id"));
                                    }

                                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, schools);
                                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                    mSchoolSpinner.setAdapter(adapter);
                                    mSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override

                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            parent.setSelection(position);
                                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                            completeRegistrationBtn.setEnabled(true);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                            completeRegistrationBtn.setEnabled(false);
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progress.dismiss();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", "asd");
                            }
                        }

                );
        mRequestQue.add(request);
        progress.show();
    }
}
