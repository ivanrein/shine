
package com.shineapptpa.rei.shine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marchelino on 23/12/2015.
 */
public class PhotosPagerFragment extends Fragment {

    Button mButtonEdit;
    private ViewPager mViewPager;

    public ViewPager getViewPager()
    {
        return this.mViewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static PhotosPagerFragment createInstance()
    {
        PhotosPagerFragment fragment = new PhotosPagerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photos_pager, container, false);

        mViewPager = (ViewPager) v.findViewById(R.id.photos_viewpager);
        FragmentManager fragmentManager = getChildFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Log.d("create pager", "page" + position);
                return PhotoFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 6;
            }
        });


        mButtonEdit = (Button) v.findViewById(R.id.btnEditProfile);
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MyProfileActivity)getActivity()).editTextBio.setVisibility(View.VISIBLE);
                ((MyProfileActivity)getActivity()).mTextViewBio.setVisibility(View.GONE);

                EditPhotosFragment fragment = EditPhotosFragment.createInstance();
                PhotosPagerFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.top_container, fragment, "EDIT")
                        .commit();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyProfileActivity)this.getActivity()).getSupportActionBar().hide();
    }
}

