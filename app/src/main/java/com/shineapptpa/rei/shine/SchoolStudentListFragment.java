package com.shineapptpa.rei.shine;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marchelino on 02/01/2016.
 */
public class SchoolStudentListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ShineUserAdapter mStudentAdapter;
    public static final String ARGS_SCHOOL_ID="com.shineapptpa.rei.shine.schoolstudentlistfragment.school_id";
    private ArrayList<ShineUser> mShineUsers;
    int school_id, width = 0, height = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void getShineUserData()
    {
        RequestQueue q = Volley.newRequestQueue(getActivity());
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, getString(R.string.laravel_API_url)
                + "getTopStudents?school_id=" + school_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray userArray = response.getJSONArray("users");
                            mShineUsers.clear();
                            for (int i = 0; i < userArray.length(); i ++){
                                String encodedBitmap = userArray.getJSONObject(i).getString("photo");
                                String name = userArray.getJSONObject(i).getString("name");
                                String gender = userArray.getJSONObject(i).getString("gender");
                                String id = userArray.getJSONObject(i).getString("id");
                                String schoolName = userArray.getJSONObject(i).getString("school_name");
                                String rate = userArray.getJSONObject(i).getString("rate");
                                mShineUsers.add(new ShineUser(id, name, schoolName, gender, encodedBitmap, rate));
                            }
                            mStudentAdapter = new ShineUserAdapter(mShineUsers);
                            mRecyclerView.setAdapter(mStudentAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.getLocalizedMessage());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("utoken", CustomPref.getUserAccessToken(getContext()));
                return map;
            }
        };
        q.add(r);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shineuser_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.shineuser_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mShineUsers = new ArrayList<>();
        school_id = (int)getArguments().getSerializable(ARGS_SCHOOL_ID);
        height = ((ImageView)v.findViewById(R.id.ivShineUserPhoto)).getHeight();
        width = ((ImageView)v.findViewById(R.id.ivShineUserPhoto)).getWidth();
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
        mShineUsers.clear();
        getShineUserData();
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
            ((HomeActivity)getActivity()).onProfilePictureClicked(mShineUser.getUser());
        }

        public void bindShineUser(ShineUser shineUser)
        {
            mShineUser = shineUser;
            mTextViewName.setText(mShineUser.getUser().get(ShineUser.MAP_USER_NAME));
            mTextViewRate.setText(mShineUser.getUser().get(ShineUser.MAP_USER_RATE));
            double rate = Double.parseDouble(mShineUser.getUser().get(ShineUser.MAP_USER_RATE));
            if (rate > 8.55)
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.ten));
            else if(rate > 7.5)
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.six));
            else if(rate > 6.5)
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.four));
            else
                mTextViewRate.setBackgroundColor(getResources().getColor(R.color.one));

            byte[] bytes = Base64.decode(mShineUser.getUser().get(ShineUser.MAP_USER_BITMAP), Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            options.inSampleSize = ImageProcessingHelper.calculateInSampleSize(options,
                    width,
                    height);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            if (bitmap == null) {
                mImageViewPhoto.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            } else
                mImageViewPhoto.setImageBitmap(bitmap);
            mTextViewBio.setText("\""+mShineUser.getUser().get(ShineUser.MAP_USER_BIO)+"\"");
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

