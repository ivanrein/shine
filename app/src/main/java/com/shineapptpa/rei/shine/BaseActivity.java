package com.shineapptpa.rei.shine;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Marchelino on 23/12/2015.
 */
public class BaseActivity extends AppCompatActivity {

    Toolbar mToolbar;

    protected void setToolbar()
    {
        Toolbar toolbar = (Toolbar) (findViewById(R.id.mainToolbar));
        setSupportActionBar(toolbar);
    }
}
