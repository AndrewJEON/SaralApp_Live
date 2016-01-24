package com.cgp.saral.fragments;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgp.saral.R;
import com.cgp.saral.myutils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginHomeFragment extends BaseFragment implements View.OnClickListener {
View view;
    @Bind(R.id.btn_homelogin)
    AppCompatButton login;
    @Bind(R.id.btn_homeregist)
    AppCompatButton registration;
    Typeface typeface;
    Fragment fragment;
    FragmentManager fragmentManager;
    public LoginHomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_login_home, container, false);
        ButterKnife.bind(this, view);
        login.setOnClickListener(this);
        registration.setOnClickListener(this);

                return view;
    }


    @Override
    public void onClick(View v) {
        String id="";
        if(v==login)
        {

                    startTransaction(new LoginFragment(), "2");

        }
        else if(v==registration)
        {            startTransaction(new LoginFragment(), "1");

        }
    }
    public void startTransaction(Fragment fragment,String id)
    {   Bundle bundle=new Bundle();
        fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        bundle.putString(Constants.FRAGMENT_ID,id);
        fragment.setArguments(bundle);
        transaction.replace(R.id.homeContainer,fragment,"Login_Home");
        transaction.addToBackStack(null);
        transaction.commit();


    }


}
