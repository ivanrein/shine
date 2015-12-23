package com.shineapptpa.rei.shine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Marchelino on 23/12/2015.
 */
public class PhotosFragment extends Fragment {

    Button mButtonEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photos_pager, container, false);
        mButtonEdit = (Button) v.findViewById(R.id.btnEditProfile);
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPhotosFragment fragment = new EditPhotosFragment();
                PhotosFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.top_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return v;
    }
}
