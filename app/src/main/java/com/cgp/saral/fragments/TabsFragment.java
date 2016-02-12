package com.cgp.saral.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgp.saral.R;
import com.cgp.saral.adapter.Tabadapter;

import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabsFragment extends BaseFragment {
    private static final String ARG_PAGE_NUMBER = "page_number";
    View view;
    Tabadapter myadapter;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;


    Stack<Integer> pageHistory;
    int currentPage;
    boolean saveToHistory;
    private int[] tabIcons = {
            R.drawable.home,
            R.drawable.you,
            R.drawable.ic_action_hand61,
            R.drawable.ic_invite,
            R.drawable.chat
            //,
            //R.drawable.ic_house_plan
    };
    private int[] tabselIcons = {
            R.drawable.home_green,
            R.drawable.you_green,
            R.drawable.ic_action_hand61_1,
            R.drawable.ic_invite_g,
            R.drawable.chat_green
            //,
            //  R.drawable.ic_house_plan_green

    };

    //, " House Plan"
    String title[] = {" Discover", " You", "Book Now" , " Invite"," Chat"};

    public TabsFragment() {

    }

    public static TabsFragment newInstance(int page) {
        TabsFragment fragment = new TabsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //   setUserVisibleHint(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      /*  Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;*/

        view = inflater.inflate(R.layout.fragment_tabs, container, false);
        ButterKnife.bind(this, view);

        myadapter = new Tabadapter(getChildFragmentManager(), title);


        viewPager.setAdapter(myadapter);

        setRetainInstance(true);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
        // selectPage(0);
        viewPager.setCurrentItem(0);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setIcon(tabselIcons[0]);

        pageHistory = new Stack<Integer>();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tabLayout.getTabAt(0).setIcon(tabIcons[0]);

                tabLayout.setTabTextColors(getResources().getColor(R.color.Textcolor_dark), getResources().getColor(R.color.green));
                int i = tab.getPosition();
                tab.setIcon(tabselIcons[i]);
                viewPager.setCurrentItem(tab.getPosition());
                if (saveToHistory)
                    pageHistory.push(Integer.valueOf(i));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                tab.setIcon(tabIcons[i]);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       /* viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener() {

            @Override
            public void onPageSelected(int i) {
                if(saveToHistory)
                    pageHistory.push(Integer.valueOf(currentPage));
                tabLayout.setTabTextColors(getResources().getColor(R.color.Textcolor_dark), getResources().getColor(R.color.green));
                int i = tab.getPosition();
                setIcon(tabselIcons[i]);
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });*/
        saveToHistory = true;
        return view;
    }

    void selectPage(int pageIndex) {
        viewPager.setCurrentItem(pageIndex);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    public void onBackPressed() {
        Log.e("View Pager ", "onBackPressed");
        if (pageHistory.empty()) {
            // super.getActivity().onBackPressed();
            Log.e("View Pager ", "onBackPressed --> pageHistory Empty");
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            getActivity().finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure to Exit now?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .setCancelable(false)
                    .setTitle("Exit Saral Vastu");
            builder.show();
        } else {
            saveToHistory = false;
            viewPager.setCurrentItem(pageHistory.pop().intValue());
            saveToHistory = true;
            Log.e("View Pager ", "onBackPressed --> saveToHistory" + saveToHistory + " " + pageHistory.size());
        }
    }

    ;


}
