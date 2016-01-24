package com.cgp.saral.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgp.saral.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassword_Options extends BaseFragment implements View.OnClickListener{
View view;
    @Bind(R.id.iv_bysms)
    ImageView bysms;
    @Bind(R.id.iv_bymail)
    ImageView bymail;

    FragmentManager fragmentManager;
    public ForgotPassword_Options() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     view=inflater.inflate(R.layout.fragment_forgate_password__options, container, false);
        ButterKnife.bind(this,view);



        bysms.setOnClickListener(this);
        bymail.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
    Fragment fragment=new ForgotPassword_Verify();
        if (v==bymail)
        {
            Log.e("NewBean", "Validation " + 0);
        startTransaction(fragment, 0);

        }
        else if (v==bysms)
        {
            Log.e("NewBean", "Validation " +1);
            startTransaction(fragment,1);
        }

    }
    public void startTransaction(Fragment fragment,int id)
    {   Bundle b=new Bundle();
        fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        b.putInt("flag", id);
        fragment.setArguments(b);
        transaction.replace(R.id.homeContainer, fragment,"pass");
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
