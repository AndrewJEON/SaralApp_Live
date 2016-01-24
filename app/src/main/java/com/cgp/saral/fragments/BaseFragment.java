package com.cgp.saral.fragments;

import android.net.Uri;
import android.support.v4.app.Fragment;

//import com.cgp.saral.SaralApplication;
//import com.squareup.leakcanary.RefWatcher;

/**
 * Created by WeeSync on 14/10/15.
 */
public abstract class BaseFragment extends Fragment {

   /* @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = SaralApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }*/



    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void onFragmentInteraction(String id);
        public void onFragmentInteraction(int actionId);
    }
}