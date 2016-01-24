package com.cgp.saral.model;

/**
 * Created by karamjeetsingh on 02/09/15.
 */
public class TabBean
{
    public  CharSequence mTitle;
    public  int mIosIconResId;
    public  int mAndroidIconResId;
    public  int mIndicatorColor;
    public  int mDividerColor;

    public TabBean(CharSequence title, int iosIconResId, int androidIconResId, int indicatorColor, int dividerColor) {
        this.mDividerColor = dividerColor;
        this.mIndicatorColor = indicatorColor;
        this.mAndroidIconResId = androidIconResId;
        this.mIosIconResId = iosIconResId;
        this.mTitle = title;
    }
    public TabBean()
    {

    }



    public int getmDividerColor() {
        return mDividerColor;
    }

    public void setmDividerColor(int mDividerColor) {
        this.mDividerColor = mDividerColor;
    }

    public int getmIndicatorColor() {
        return mIndicatorColor;
    }

    public void setmIndicatorColor(int mIndicatorColor) {
        this.mIndicatorColor = mIndicatorColor;
    }

    public int getmAndroidIconResId() {
        return mAndroidIconResId;
    }

    public void setmAndroidIconResId(int mAndroidIconResId) {
        this.mAndroidIconResId = mAndroidIconResId;
    }

    public int getmIosIconResId() {
        return mIosIconResId;
    }

    public void setmIosIconResId(int mIosIconResId) {
        this.mIosIconResId = mIosIconResId;
    }

    public CharSequence getmTitle() {
        return mTitle;
    }

    public void setmTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }



}
