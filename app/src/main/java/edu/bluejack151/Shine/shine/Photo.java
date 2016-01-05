package edu.bluejack151.Shine.shine;

import android.graphics.Bitmap;

/**
 * Created by Marchelino on 04/01/2016.
 */
public class Photo {
    private Bitmap mBitmap;
    private String mPhotoId;

    public Photo(Bitmap bitmap, String photoId) {
        mBitmap = bitmap;
        mPhotoId = photoId;
    }

    public Photo(String photoId) {
        mPhotoId = photoId;
    }

    public Photo(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getPhotoId() {
        return mPhotoId;
    }

    public void setPhotoId(String photoId) {
        mPhotoId = photoId;
    }
}
