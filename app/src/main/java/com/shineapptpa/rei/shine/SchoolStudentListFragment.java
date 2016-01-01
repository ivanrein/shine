package com.shineapptpa.rei.shine;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marchelino on 02/01/2016.
 */
public class SchoolStudentListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ShineUserAdapter mStudentAdapter;
    public static final String ARGS_SCHOOL_ID="com.shineapptpa.rei.shine.schoolstudentlistfragment.school_id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void getShineUserData()
    {
        //Tar fetch data pake volley di sini lu

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shineuser_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.shineuser_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    public static SchoolStudentListFragment createFragment(Context context, int schoolId)
    {
        SchoolStudentListFragment fragment = new SchoolStudentListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_SCHOOL_ID, schoolId);
        return fragment;
    }

    public void updateUI()
    {
        //dummy data
        List<ShineUser> mShineUsers = new ArrayList<ShineUser>();
        for (int i = 0; i < 25; i++)
        {
            mShineUsers.add(new ShineUser("User "+i, 9, "ngentodss"));
        }

        mStudentAdapter = new ShineUserAdapter(mShineUsers);
        mRecyclerView.setAdapter(mStudentAdapter);
    }

    private class ShineUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mTextViewName, mTextViewRate, mTextViewBio;
        public ImageView mImageViewPhoto;
        private ShineUser mShineUser;

        public ShineUserHolder(View itemView) {
            super(itemView);
            mTextViewName = (TextView) itemView.findViewById(R.id.tvShineUserName);
            mTextViewRate = (TextView) itemView.findViewById(R.id.tvShineUserRate);
            mTextViewBio = (TextView) itemView.findViewById(R.id.tvShinerUserBio);
            mImageViewPhoto = (ImageView) itemView.findViewById(R.id.ivShineUserPhoto);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //tinggal bikin intent buat buka page shineusernya di sini
//            Intent intent = MyProfileActivity.newIntent(getActivity());
//            startActivity(intent);
        }

        public void bindShineUser(ShineUser shineUser)
        {
            mShineUser = shineUser;
            mTextViewName.setText("Erick");
            mTextViewRate.setText(90 + "");
            if (90 > 85)
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.red));
            else if(90 > 75)
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.green));
            else if(90 > 65)
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.yellow));
            else
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.blue));
            mImageViewPhoto.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
            mTextViewBio.setText("\"Biosss\"");
        }
    }

    private class ShineUserAdapter extends RecyclerView.Adapter<ShineUserHolder>
    {
        private List<ShineUser> mShineUsers;

        public ShineUserAdapter(List<ShineUser> shineUsers) {
            mShineUsers = shineUsers;
        }

        @Override
        public ShineUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.layout_list_shineuser, parent, false);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((HomeActivity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displaymetrics);
            view.setMinimumHeight(displaymetrics.heightPixels/2);


            return new ShineUserHolder(view);
        }

        @Override
        public void onBindViewHolder(ShineUserHolder holder, int position) {
            holder.bindShineUser(mShineUsers.get(position));
        }

        @Override
        public int getItemCount() {
            return mShineUsers.size();
        }
    }
}

