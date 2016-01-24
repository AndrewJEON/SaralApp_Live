package com.cgp.saral.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgp.saral.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WeeSync on 17/10/15.
 */
public class Contact_Us extends Fragment {

    View view;

    private static final String ARG_PAGE_NUMBER = "page_number";

    public Contact_Us() {

    }

    public static Contact_Us newInstance(int page) {
        Contact_Us fragment = new Contact_Us();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_us, container, false);
        ButterKnife.bind(this,view);



        return view;
    }
}
