package com.cgp.saral.interfc;

import android.content.Context;

/**
 * Created by WeeSync on 04/10/15.
 */
public interface StackedFragment {
    //Return true if fragment needs to show back arrow on ActionBar
    public boolean getDisplayHomeAsUpEnabled();
    //Return title to be shown on ActionBar when fragment added
    public String getTitle(Context context);

    public abstract void showDrawerToggle(boolean showDrawerToggle);
}
