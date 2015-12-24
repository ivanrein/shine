package com.shineapptpa.rei.shine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends BaseActivity {

    public static ListView mNavbarList;
    private RelativeLayout mNavbarPanel;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mNavbarLayout;
    private ImageView mImageViewProfpic;
    private TextView mTextViewUsername, mTextViewSchool;
    private RelativeLayout mRelativeLayoutUserInfo;
    private ArrayList<NavItem> mNavbarItems = new ArrayList<NavItem>();
    private ArrayList<Integer> mPhotoResources;



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
        mNavbarItems.add(new NavItem("Coba1", "descccaa", R.drawable.com_facebook_button_icon));
        mNavbarItems.add(new NavItem("Coba2", "descccss", R.drawable.com_facebook_button_icon));
        mNavbarItems.add(new NavItem("Coba3", "descccww", R.drawable.com_facebook_button_icon));
        mTextViewUsername.setText("Guest");
        mTextViewUsername.setText("Binus University");

        mNavbarLayout.setDrawerListener(mActionBarDrawerToggle);
        refreshNavbar();
    }

    public void selectMenu(int position)
    {
        //start intent from the nav bar here broh
        Toast.makeText(HomeActivity.this, mNavbarItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
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
}
