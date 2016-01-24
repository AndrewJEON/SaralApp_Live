package com.cgp.saral.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.cgp.saral.R;

/**
 * Created by karamjeetsingh on 03/09/15.
 */
public class test extends ActionBarActivity {
    private static final String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.fragment_profile_edit);


    }

}
