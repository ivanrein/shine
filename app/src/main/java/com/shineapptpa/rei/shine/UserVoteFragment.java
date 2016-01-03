package com.shineapptpa.rei.shine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Marchelino on 03/01/2016.
 */
public class UserVoteFragment extends Fragment {

    private ImageView mImageViewGender, mImageViewUserPhoto;
    private TextView mTextViewUsername, mTextViewSchool;
    private Button mButtonOne, mButtonTwo, mButtonThree, mButtonFour, mButtonFive, mButtonSix;
    private Button mButtonSeven, mButtonEight, mButtonNine, mButtonTen;
    private ArrayList<ShineUser> mShineUsers;
    int curr_position = 0;
    int width = 0, height = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_vote, container, false);

        initializeRateButtons(v);

        mImageViewGender = (ImageView)v.findViewById(R.id.ivUserGender);
        mImageViewUserPhoto = (ImageView)v.findViewById(R.id.ivUserPhoto);
        mImageViewUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buka profilenya di sini
                ((HomeActivity)getActivity()).onProfilePictureClicked(mShineUsers.get(curr_position).getUser());
            }
        });
        mTextViewUsername = (TextView)v.findViewById(R.id.tvUserVoteName);
        mTextViewSchool = (TextView)v.findViewById(R.id.tvUserSchool);

        mShineUsers = ((HomeActivity)getActivity()).getShineUsers();
        height = ((ImageView)v.findViewById(R.id.ivUserPhoto)).getHeight();
        width = ((ImageView)v.findViewById(R.id.ivUserPhoto)).getWidth();

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/myfont.ttf");
        mTextViewUsername.setTypeface(tf);

        bindUser(0);
        return v;
    }

    private void initializeRateButtons(View v)
    {
        mButtonOne = (Button)v.findViewById(R.id.btn1);
        mButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "1");
                bindUser(curr_position);
            }
        });
        mButtonTwo = (Button)v.findViewById(R.id.btn2);
        mButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "2");
                bindUser(curr_position);

            }
        });
        mButtonThree = (Button)v.findViewById(R.id.btn3);
        mButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "3");
                bindUser(curr_position);

            }
        });
        mButtonFour = (Button)v.findViewById(R.id.btn4);
        mButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "4");
                bindUser(curr_position);

            }
        });
        mButtonFive = (Button)v.findViewById(R.id.btn5);
        mButtonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "5");
                bindUser(curr_position);

            }
        });
        mButtonSix = (Button)v.findViewById(R.id.btn6);
        mButtonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "6");
                bindUser(curr_position);

            }
        });
        mButtonSeven = (Button)v.findViewById(R.id.btn7);
        mButtonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "7");
                bindUser(curr_position);

            }
        });
        mButtonEight = (Button)v.findViewById(R.id.btn8);
        mButtonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "8");
                bindUser(curr_position);

            }
        });
        mButtonNine = (Button)v.findViewById(R.id.btn9);
        mButtonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "9");
                bindUser(curr_position);

            }
        });
        mButtonTen = (Button)v.findViewById(R.id.btn10);
        mButtonTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mShineUsers.get(curr_position).getUser().get(ShineUser.MAP_USER_ID);
                ((HomeActivity)getActivity()).voteRequest(id, "10");
                bindUser(curr_position);
            }
        });
    }

    private void bindUser(int position)
    {
        mImageViewGender.setImageResource(mShineUsers.get(position).getUser().get(ShineUser.MAP_USER_GENDER).equals("Male") ? R.drawable.gentleman : R.drawable.ladies);

        Log.d("bitmapuser", mShineUsers.get(position).getUser().get(ShineUser.MAP_USER_BITMAP));
        byte[] bytes = Base64.decode(mShineUsers.get(position).getUser().get(ShineUser.MAP_USER_BITMAP), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        options.inSampleSize = ImageProcessingHelper.calculateInSampleSize(options,
                width,
                height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options);
        mImageViewUserPhoto.setImageBitmap(bitmap);

        mTextViewSchool.setText(mShineUsers.get(position).getUser().get(ShineUser.MAP_USER_SCHOOL));
        mTextViewUsername.setText(mShineUsers.get(position).getUser().get(ShineUser.MAP_USER_NAME));
        mShineUsers.remove(position);
        curr_position = position;
    }


    public static UserVoteFragment createFragment()
    {
        UserVoteFragment fragment = new UserVoteFragment();
        return fragment;
    }

}
