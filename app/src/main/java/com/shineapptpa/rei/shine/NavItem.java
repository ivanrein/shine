package com.shineapptpa.rei.shine;

/**
 * Created by Marchelino on 21/12/2015.
 */
public class NavItem {

    private String mTitle, mDesc;
    private int mIcon;

    public NavItem(String title, String desc, int icon) {
        mTitle = title;
        mDesc = desc;
        mIcon = icon;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }
}
