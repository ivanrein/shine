
package com.shineapptpa.rei.shine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Marchelino on 23/12/2015.
 */
public class EditPhotosFragment extends Fragment {

    ArrayList<Photo> mPhotoResources;
    ArrayList<ImageView> mPhotoHolder;
    ArrayList<String> deletedPhotosId;
    ImageView mPhoto0, mPhoto1, mPhoto2, mPhoto3, mPhoto4, mPhoto5;
    final String NOT_EMPTY = "not_empty";
    final String EMPTY = "empty";
    public static final int GET_PHOTO = 177;

    public ArrayList<String> getDeletedPhotosId()
    {
        return this.deletedPhotosId;
    }

    public ArrayList<Photo> getPhotoResources()
    {
        return this.mPhotoResources;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile_photos, container, false);

        mPhotoResources = ((MyProfileActivity)getActivity()).getPhotoList();

        mPhoto0 = (ImageView) v.findViewById(R.id.ivUser0);
        mPhoto1 = (ImageView) v.findViewById(R.id.ivUser1);
        mPhoto2 = (ImageView) v.findViewById(R.id.ivUser2);
        mPhoto3 = (ImageView) v.findViewById(R.id.ivUser3);
        mPhoto4 = (ImageView) v.findViewById(R.id.ivUser4);
        mPhoto5 = (ImageView) v.findViewById(R.id.ivUser5);

        mPhotoHolder = new ArrayList<ImageView>();
        deletedPhotosId = new ArrayList<String>();

        mPhotoHolder.add(mPhoto0);
        mPhotoHolder.add(mPhoto1);
        mPhotoHolder.add(mPhoto2);
        mPhotoHolder.add(mPhoto3);
        mPhotoHolder.add(mPhoto4);
        mPhotoHolder.add(mPhoto5);

        refreshHolders();

        for(int j = 0; j < mPhotoHolder.size(); j++)
        {
            mPhotoHolder.get(j).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = mPhotoHolder.indexOf((ImageView) v);
                    if (mPhotoHolder.get(position).getTag().equals(NOT_EMPTY))
                    {
                        deletedPhotosId.add(mPhotoResources.get(position).getPhotoId());
                        mPhotoResources.remove(position);
                        refreshHolders();
                        Log.d("PHOTOLONGCLICKED", "NOT_EMPTY");
                    }
                    return false;
                }
            });

            mPhotoHolder.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mPhotoHolder.indexOf((ImageView) v);
                    if (mPhotoHolder.get(position).getTag().equals(EMPTY))
                    {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, GET_PHOTO);
                    }
                }
            });
        }

        ((MyProfileActivity) this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((MyProfileActivity) this.getActivity()).getSupportActionBar().show();
        return v;
    }

    private void refreshHolders()
    {
        int i;
        for (i = 0; i < mPhotoResources.size(); i++)
        {
            mPhotoHolder.get(i).setImageBitmap(mPhotoResources.get(i).getBitmap());
            mPhotoHolder.get(i).setTag(NOT_EMPTY);
        }

        for (int j = i; j < mPhotoHolder.size(); j++)
        {
            mPhotoHolder.get(j).setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
            mPhotoHolder.get(i).setTag(EMPTY);

        }
    }

    public void refreshPhotos(ArrayList<Photo> resources)
    {
        this.mPhotoResources = resources;
        refreshHolders();
    }

    public static EditPhotosFragment createInstance()
    {
        EditPhotosFragment fragment = new EditPhotosFragment();
        return fragment;
    }
}