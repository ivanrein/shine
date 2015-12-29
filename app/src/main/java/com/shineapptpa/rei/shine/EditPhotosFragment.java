package com.shineapptpa.rei.shine;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    public static final String ARGS_PHOTO_RESOURCES = "com.shineapptpa.rei.shine.editphotoresources.photos";
    ArrayList<Integer> mPhotoResources;
    ArrayList<Integer> mNotEdited;
    ArrayList<ImageView> mPhotoHolder;
    ImageView mPhoto0, mPhoto1, mPhoto2, mPhoto3, mPhoto4, mPhoto5;
    final int EMPTY  = R.drawable.com_facebook_profile_picture_blank_square;

    public ArrayList<Integer> getPhotoResources()
    {
        return mPhotoResources;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ArrayList<Integer> getNotEdited()
    {
        return mNotEdited;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile_photos, container, false);

        mPhotoResources = (ArrayList<Integer>) getArguments().getSerializable(ARGS_PHOTO_RESOURCES);
        mNotEdited = mPhotoResources;

        mPhoto0 = (ImageView) v.findViewById(R.id.ivUser0);
        mPhoto1 = (ImageView) v.findViewById(R.id.ivUser1);
        mPhoto2 = (ImageView) v.findViewById(R.id.ivUser2);
        mPhoto3 = (ImageView) v.findViewById(R.id.ivUser3);
        mPhoto4 = (ImageView) v.findViewById(R.id.ivUser4);
        mPhoto5 = (ImageView) v.findViewById(R.id.ivUser5);

        mPhotoHolder = new ArrayList<ImageView>();

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
                    if (!mPhotoHolder.get(position).getDrawable().getConstantState()
                            .equals(
                            getResources()
                                    .getDrawable(R.drawable.com_facebook_profile_picture_blank_square)
                                    .getConstantState()
                            )
                            ) {
                        mPhotoResources.remove(position);
                        refreshHolders();
                    }
                    return false;
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
            mPhotoHolder.get(i).setImageResource(mPhotoResources.get(i));
        }

        for (int j = i; j < mPhotoHolder.size(); j++)
        {
            mPhotoHolder.get(j).setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        }
    }

    public static EditPhotosFragment createInstance(ArrayList<Integer> photoResources)
    {
        Bundle args = new Bundle();
        args.putSerializable(EditPhotosFragment.ARGS_PHOTO_RESOURCES, photoResources);
        EditPhotosFragment fragment = new EditPhotosFragment();
        fragment.setArguments(args);
        return fragment;
    }




}
