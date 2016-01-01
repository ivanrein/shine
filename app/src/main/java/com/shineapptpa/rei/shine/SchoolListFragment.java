package com.shineapptpa.rei.shine;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marchelino on 01/01/2016.
 */

public class SchoolListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SchoolAdapter mSchoolAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void getSchoolData()
    {
        //Tar fetch data pake volley di sini lu

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_school_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.school_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    public static SchoolListFragment createFragment(Context context)
    {
        SchoolListFragment fragment = new SchoolListFragment();
        return fragment;
    }

    public void updateUI()
    {
        //dummy data
        List<School> mSchools = new ArrayList<School>();
        for (int i = 0; i < 25; i++)
        {
            mSchools.add(new School("Sekolah "+i, .23, .23, 90));
        }

        mSchoolAdapter = new SchoolAdapter(mSchools);
        mRecyclerView.setAdapter(mSchoolAdapter);
    }

    private class SchoolHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mTextViewName, mTextViewTotalStudent, mTextViewRate;
        private School mSchool;

        public SchoolHolder(View itemView) {
            super(itemView);
            mTextViewName = (TextView) itemView.findViewById(R.id.tvSchoolName);
//            mTextViewTotalStudent = (TextView) itemView.findViewById(R.id.tvSchoolTotalStudent);
            mTextViewRate = (TextView) itemView.findViewById(R.id.tvSchoolRate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            SchoolStudentListFragment fragment = SchoolStudentListFragment
                    .createFragment(getActivity(), 0);
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment,"STUDENT LIST")
                    .addToBackStack(null)
                    .commit();
        }

        public void bindSchool(School school)
        {
            mSchool = school;
            mTextViewName.setText(mSchool.getName());
            mTextViewRate.setText(""+mSchool.getRate());
//            mTextViewTotalStudent.setText(""+mSchool.getRate());
        }
    }

    private class SchoolAdapter extends RecyclerView.Adapter<SchoolHolder>
    {
        private List<School> mSchools;

        public SchoolAdapter(List<School> schoolList) {
            mSchools = schoolList;
        }

        @Override
        public SchoolHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.layout_list_school, parent, false);
            return new SchoolHolder(view);
        }

        @Override
        public void onBindViewHolder(SchoolHolder holder, int position) {
            holder.bindSchool(mSchools.get(position));
        }

        @Override
        public int getItemCount() {
            return mSchools.size();
        }
    }
}

