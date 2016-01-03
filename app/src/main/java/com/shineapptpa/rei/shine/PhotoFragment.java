<<<<<<< HEAD
package com.shineapptpa.rei.shine;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Marchelino on 24/12/2015.
 */
public class PhotoFragment extends Fragment {

    public static final String ARGS_PHOTO="com.shineapptpa.rei.shine.photofragment.photo";
    private ImageView mImageViewPhoto;
    private Bitmap imageResource;
    private int photoPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);

        photoPosition = (int)getArguments().getSerializable(ARGS_PHOTO);
        
        mImageViewPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
        imageResource = ((MyProfileActivity)getParentFragment().getActivity()).getPhoto(photoPosition);
        mImageViewPhoto.setTag("POSITION" + photoPosition);
        Log.d("create photo fragment", "POSITION" + photoPosition);
        mImageViewPhoto.setImageBitmap(imageResource);

        return v;
    }

    public void changeImage(Bitmap bitmap)
    {
        mImageViewPhoto.setImageBitmap(bitmap);
    }

    public ImageView getImageViewPhoto()
    {
        return this.mImageViewPhoto;
    }

    public static Fragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_PHOTO, position);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
=======
package com.shineapptpa.rei.shine;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Marchelino on 24/12/2015.
 */
public class PhotoFragment extends Fragment {

    public static final String ARGS_PHOTO="com.shineapptpa.rei.shine.photofragment.photo";
    private ImageView mImageViewPhoto;
    private int imageResource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);

        mImageViewPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
        imageResource = (int) getArguments().getSerializable(ARGS_PHOTO);
        mImageViewPhoto.setImageResource(imageResource);

        return v;
    }

    public static Fragment newInstance(int imageResource)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_PHOTO, imageResource);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
>>>>>>> be1e82dc5f850c7040bf525d22b4a21309b6fb08
