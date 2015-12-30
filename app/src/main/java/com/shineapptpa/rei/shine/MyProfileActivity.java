package com.shineapptpa.rei.shine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyProfileActivity extends BaseActivity {

    public static final String EXTRA_USER_FULLNAME = "com.shineapptpa.rei.myprofileactivity.fullname";
   // public static final String EXTRA_USER_DOB = "com.shineapptpa.rei.myprofileactivity.dob";
    public static final String EXTRA_USER_GENDER = "com.shineapptpa.rei.myprofileactivity.gender";
    public static final String EXTRA_USER_BIO = "com.shineapptpa.rei.myprofileactivity.bio";
    public static final String EXTRA_USER_SCHOOL = "com.shineapptpa.rei.myprofileactivity.school";
    public static final String EXTRA_USER_IMAGES = "com.shineapptpa.rei.myprofileactivity.images";
   // public static final String EXTRA_USER_EMAIL = "com.shineapptpa.rei.myprofileactivity.email";
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    public TextView mTextViewBio, mTextViewAge, mTextViewUser, mTextViewSchool;
    public EditText editTextBio;
    ImageView mImageViewGender;

  //  ArrayList<Integer> photoResources;

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
 //       photoResources = new ArrayList<Integer>();
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
                .replace(R.id.top_container,fragment)
                .commit();

        return super.onOptionsItemSelected(item);
    }
}
