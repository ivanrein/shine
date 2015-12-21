package com.shineapptpa.rei.shine;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CompleteSignUpActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> schools;
    Button completeRegistrationBtn;
    RequestQueue mRequestQue;
    Spinner mSchoolSpinner;
    String userEmail;
    String userFullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sign_up);
        userEmail = getIntent().getStringExtra(MainActivity.USER_EMAIL);
        mSchoolSpinner = (Spinner)findViewById(R.id.schools);
        schools = new ArrayList<>();
        completeRegistrationBtn = (Button)findViewById(R.id.submit_btn);

        completeRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getIntExtra(MainActivity.FORM_CODE,
                        MainActivity.FORM_WITHOUT_PASSWORD) == MainActivity.FORM_WITH_PASSWORD){
                    userEmail = getIntent().getStringExtra(MainActivity.USER_EMAIL);
                    userFullName = getIntent().getStringExtra(MainActivity.USER_FULLNAME);

                    ParseUser user = new ParseUser();
                    user.setUsername(userEmail);
                    user.setPassword(userFullName);

                    //sign up the user. this also log the user in.
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                //success sign up
                                Log.d("username", ParseUser.getCurrentUser().getUsername());
                            } else {
                                //failed sign up
                            }
                        }
                    });
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

    private void fetchSchool() {
        mRequestQue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, getString(R.string.laravel_API_url) + "schools", null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray arr = null;
                                try {
                                    arr = response.getJSONArray("school");
                                    for (int i = 0; i < arr.length(); i++) {
                                        schools.add(arr.getJSONObject(i).getString("name"));
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
    }
}
