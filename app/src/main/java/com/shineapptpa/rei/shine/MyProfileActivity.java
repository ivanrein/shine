package com.shineapptpa.rei.shine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MyProfileActivity extends BaseActivity {

    FragmentManager mFragmentManager;
    Fragment mFragment;
    TextView mTextViewBio, mTextViewAge, mTextViewUser, mTextViewGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setToolbar();

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.top_container);

        initialize();

        bindView("Erick Marchelino", new Date(1995, 3, 17), "bla bla bla bla bla", "Male");


        PhotosFragment temp = new PhotosFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.top_container,temp)
                .commit();

    }

    private void initialize()
    {
        mTextViewAge = (TextView) findViewById(R.id.tvAge);
        mTextViewUser = (TextView) findViewById(R.id.tvFullname);
        mTextViewGender = (TextView) findViewById(R.id.tvGender);
        mTextViewBio = (TextView) findViewById(R.id.tvBio);
    }


    //set data di sini van
    public void bindView(String name, Date dob, String bio, String gender)
    {
        Calendar today = Calendar.getInstance();

        Integer age = today.get(Calendar.YEAR) - dob.getYear();

        mTextViewAge.setText(age.toString());
        mTextViewUser.setText(name);
        mTextViewGender.setText(gender);
        mTextViewBio.setText(bio);
    }

    //pake intent ini buat bikin intent ke MyProfile
    public static Intent newIntent(Context context)
    {
        Intent intent = new Intent(context, MyProfileActivity.class);
        return intent;
    }


}
