package com.shineapptpa.rei.shine;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.service.voice.VoiceInteractionSession;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProfileActivity extends BaseActivity {

    public static final String EXTRA_USER_FULLNAME = "com.shineapptpa.rei.myprofileactivity.fullname";
   // public static final String EXTRA_USER_DOB = "com.shineapptpa.rei.myprofileactivity.dob";
    public static final String EXTRA_USER_GENDER = "com.shineapptpa.rei.myprofileactivity.gender";
    public static final String EXTRA_USER_BIO = "com.shineapptpa.rei.myprofileactivity.bio";
    public static final String EXTRA_USER_SCHOOL = "com.shineapptpa.rei.myprofileactivity.school";
   // public static final String EXTRA_USER_IMAGES = "com.shineapptpa.rei.myprofileactivity.images";
    public static final String EXTRA_USER_EMAIL = "com.shineapptpa.rei.myprofileactivity.email";
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    public TextView mTextViewBio, mTextViewAge, mTextViewUser, mTextViewSchool;
    public EditText editTextBio;
    ImageView mImageViewGender;

  //  ArrayList<Integer> photoResources;
    ArrayList<Bitmap> photoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setToolbar();
        getSupportActionBar().hide();
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.top_container);

        initialize();
        bindView();

        //fetch foto dari db
        RequestQueue q = Volley.newRequestQueue(this);
        HashMap<String, String> email = new HashMap<>();
        email.put("email", (String)getIntent().getSerializableExtra(EXTRA_USER_EMAIL));
        JSONObject json = new JSONObject(email);
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, getString(R.string.laravel_API_url) + "photos", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray photos= response.getJSONArray("photos");
                            for (int i = 0; i < photos.length(); i++){
                                // decode base64 dari DB dlu. ni ntar harus dipindahin ke async task
                                byte[] decodedString = Base64.decode(photos.getJSONObject(i).getString("photo"), Base64.DEFAULT);
                                // dari byte ke Bitmap
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true; // katanya bwt masalah ngecil"in, pake injustdecodebounds true biar ga ditampung memory
                                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,options);
                                // ni insamplesize buat ngecilin gambarnya ceritanya, param 2 sama 3 di calculate itu
                                // harusnya buat dikecilin jadi seberapa, tapi gw ga bisa tau di sini soalnya
                                // ukuran ImageViewnya adanya di PhotoFragment. anu'in dulu enaknya gimana.
                                // method calculate nya ngambil dri web android btw
                                options.inSampleSize = calculateInSampleSize(options, ?,?);
                                options.inJustDecodeBounds = false;

                                //kalo udah beres, baru dimasukin ke photoList, pake bitmapfactory ama options yang baru
                                photoList.add(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("utoken", CustomPref.getUserAccessToken(getApplicationContext()));
                return map;
            }
        };
        q.add(r);
        //fetch foto done

        PhotosPagerFragment temp = PhotosPagerFragment.createInstance();
        if(mFragmentManager.getFragments() == null)
            mFragmentManager.beginTransaction()
                    .add(R.id.top_container, temp)
                    .addToBackStack(null)
                    .commit();

    }

    private void initialize()
    {
        mTextViewAge = (TextView) findViewById(R.id.tvAge);
        mTextViewUser = (TextView) findViewById(R.id.tvFullname);
        mTextViewSchool = (TextView) findViewById(R.id.tvSchool);
        mImageViewGender = (ImageView) findViewById(R.id.ivGender);
        mTextViewBio = (TextView) findViewById(R.id.tvBio);
        editTextBio = (EditText) findViewById(R.id.tvBioEdit);
        photoList = new ArrayList<>();
 //       photoResources = new ArrayList<Integer>();
    }

    @Override
    protected void onDestroy() {
        photoList.clear();
        super.onDestroy();
    }

    //set data di sini
    public void bindView()
    {
        String fullname =(String) getIntent().getSerializableExtra(EXTRA_USER_FULLNAME);
        //Date dob =(Date) getIntent().getSerializableExtra(EXTRA_USER_DOB);
        String gender =(String) getIntent().getSerializableExtra(EXTRA_USER_GENDER);
        String bio =(String) getIntent().getSerializableExtra(EXTRA_USER_BIO);
        String school =(String) getIntent().getSerializableExtra(EXTRA_USER_SCHOOL);
     //   photoResources = (ArrayList<Integer>) getIntent().getSerializableExtra(EXTRA_USER_IMAGES);

//        Calendar today = Calendar.getInstance();
//        Integer age = today.get(Calendar.YEAR) - dob.getYear();
//        mTextViewAge.setText(age.toString());
        mTextViewUser.setText(fullname);
        mTextViewSchool.setText(school);
        mImageViewGender.setImageResource(gender.equals("Male") ? R.drawable.gentleman : R.drawable.ladies);
        mTextViewBio.setText(bio);
        editTextBio.setText(bio);
    }

    //pake intent ini buat bikin intent ke MyProfile
//    public static Intent newIntent(Context context, String fullname, Date dob, String bio,
//                                   String gender, String email, ArrayList<Integer> resources)
//    {
//        Intent intent = new Intent(context, MyProfileActivity.class);
//        intent.putExtra(EXTRA_USER_FULLNAME, fullname);
//        intent.putExtra(EXTRA_USER_DOB, dob);
//        intent.putExtra(EXTRA_USER_BIO, bio);
//        intent.putExtra(EXTRA_USER_GENDER, gender);
//        intent.putExtra(EXTRA_USER_IMAGES, resources);
//        intent.putExtra(EXTRA_USER_EMAIL, email);
//        return intent;
//    }

    /**
     * Create intent and let MyProfileActivity fetch user's photos
     * @param context
     * @param user
     * @return
     */
    public static Intent newIntent(Context context, HashMap<String, String> userMap ){
        Intent intent = new Intent(context, MyProfileActivity.class);
        intent.putExtra(EXTRA_USER_BIO, userMap.get(ShineUser.MAP_USER_BIO));
        intent.putExtra(EXTRA_USER_FULLNAME, userMap.get(ShineUser.MAP_USER_NAME));
        intent.putExtra(EXTRA_USER_GENDER, userMap.get(ShineUser.MAP_USER_GENDER));
        intent.putExtra(EXTRA_USER_SCHOOL, userMap.get(ShineUser.MAP_USER_SCHOOL));
        intent.putExtra(EXTRA_USER_EMAIL, userMap.get(ShineUser.MAP_USER_EMAIL));
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(MyProfileActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();

        switch(item.getItemId()) {
            case R.id.action_done: {
                //SAVE PROFILE HABIS EDIT DI SINI, SAVE KE API, DLL

                mTextViewBio.setText(editTextBio.getText().toString());
                EditPhotosFragment temp = (EditPhotosFragment) mFragmentManager.
                        findFragmentById(R.id.top_container);

      //          photoResources = temp.getPhotoResources();

            }
            break;

//            case R.id.action_cancel: {
//                mTextViewBio.setText(editTextBio.getText().toString());
//                EditPhotosFragment temp = (EditPhotosFragment) mFragmentManager.
//                        findFragmentById(R.id.top_container);
//
//                photoResources = temp.getNotEdited();
//            }
//            break;
        }

        mTextViewBio.setVisibility(View.VISIBLE);
        editTextBio.setVisibility(View.GONE);

        PhotosPagerFragment fragment = PhotosPagerFragment.createInstance();
        mFragmentManager.beginTransaction()
                .replace(R.id.top_container, fragment)
                .commit();

        return super.onOptionsItemSelected(item);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
