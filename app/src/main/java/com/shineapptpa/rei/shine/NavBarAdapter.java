package com.shineapptpa.rei.shine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marchelino on 21/12/2015.
 */
public class NavBarAdapter extends BaseAdapter {

    ArrayList<NavItem> mSubMenuItems;
    Context mContext;

    public NavBarAdapter(Context context, ArrayList<NavItem> subMenuItems) {

        mContext = context;
        mSubMenuItems = subMenuItems;
    }

    @Override
    public int getCount() {
        return mSubMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.navbar_item_layout, null);
        }
        else
            view = convertView;

        TextView tvTitle = (TextView) view.findViewById(R.id.tvNavBarItemTitle);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvNavBarItemDesc);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivNavBarItemIcon);

        tvTitle.setText( mSubMenuItems.get(position).getTitle() );
        tvDesc.setText( mSubMenuItems.get(position).getDesc() );
        ivIcon.setImageResource(mSubMenuItems.get(position).getIcon());

        return view;
    }

}
