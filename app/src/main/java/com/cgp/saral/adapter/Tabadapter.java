package com.cgp.saral.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.cgp.saral.fragments.CompassFragment;
import com.cgp.saral.fragments.HomeTabFragment;
import com.cgp.saral.fragments.InviteTabFragment;
import com.cgp.saral.fragments.MessageNotiFragment;
import com.cgp.saral.fragments.TestimonialsFragment;
import com.cgp.saral.fragments.VaastuTipsFragment;

import java.util.ArrayList;


/**
 * Created by karamjeetsingh on 19/09/15.
 */
public class Tabadapter extends FragmentStatePagerAdapter {
String title[];

    static ArrayList<Fragment> mPlaceHolderFragmentArray = new ArrayList<Fragment>();


    public Tabadapter(FragmentManager fm, String tie[]) {
        super(fm);
        this.title=tie;
        try{
            if(mPlaceHolderFragmentArray.size()<=0){
                initFragments();
            }

        }catch (Throwable t){
            Log.e("Tabadapter",t.getMessage(),t);
        }
        // TODO Auto-generated constructor stub

    }

    private void initFragments()
    {
        int i= 0;
        mPlaceHolderFragmentArray.add(HomeTabFragment.newInstance(i++));
        mPlaceHolderFragmentArray.add(VaastuTipsFragment.newInstance(i++));
        mPlaceHolderFragmentArray.add(MessageNotiFragment.newInstance(i++));
        mPlaceHolderFragmentArray.add(TestimonialsFragment.newInstance(i++));
        mPlaceHolderFragmentArray.add(CompassFragment.newInstance(i++));
        mPlaceHolderFragmentArray.add(InviteTabFragment.newInstance(i++));
        //mPlaceHolderFragmentArray.add(YouFragment.newInstance(i++));
    }

    @Override
    public Fragment getItem(int i) {
        return mPlaceHolderFragmentArray.get(i);

    }

    @Override
    public CharSequence getPageTitle(int position) {

        return  title[position];
    }

    @Override
    public int getCount() {

        return mPlaceHolderFragmentArray.size(); //No of Tabs
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }



}
