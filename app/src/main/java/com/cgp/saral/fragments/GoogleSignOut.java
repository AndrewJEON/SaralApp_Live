package com.cgp.saral.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgp.saral.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

/**
 * Created by WeeSync on 11/10/15.
 */
public class GoogleSignOut extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult> {

    GoogleApiClient mGoogleApiClient;
    Context ctx;

    View view;

    public GoogleSignOut()
    {

    }

    OnDataPass dataPasser;
    public interface OnDataPass {
        public void onDataPass(String data);
    }

  //  UserProfileActivity appCompatActivity;


    @Override
    public void onAttach(Activity activity) {
        Log.e("Logout  onAttach", "gplus");
        super.onAttach(activity);

        if(activity instanceof OnDataPass)
        {
            this.dataPasser = (OnDataPass)activity;
        }else
        {

            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_social_logout, container, false);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        //copy this code on "Logout" Onclick

        Log.e("LOG OUT", "in process  " + mGoogleApiClient.isConnected());

        // TODO Auto-generated method stub
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
           // mGoogleApiClient.connect();
            dataPasser.onDataPass("true");
           // removeFragmentbyTag(this,"");
            // updateUI(false);
            Log.e("LOG OUT","Success");
            System.err.println("LOG OUT ^^^^^^^^^^^^^^^^^^^^ SUCESS");
        }else
        {
            Log.e("LOG OUT","else");
            //removeFragmentbyTag(this,"");
            dataPasser.onDataPass("false");
        }
        return view;
    }






    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        //mSignInClicked = false;

        // updateUI(true);
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
                this);
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mGoogleApiClient.connect();
        // updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult arg0) {
        // TODO Auto-generated method stub

    }


}
