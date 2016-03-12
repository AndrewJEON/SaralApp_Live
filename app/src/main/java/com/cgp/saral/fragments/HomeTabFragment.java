package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cgp.saral.R;
import com.cgp.saral.adapter.HomeTab_Adapter;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.fab.FloatingActionButton;
import com.cgp.saral.fab.FloatingActionMenu;
import com.cgp.saral.model.Datum;
import com.cgp.saral.model.FeedData;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.ObjectSerializerHelper;
import com.cgp.saral.myutils.SharedPreferenceManager;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTabFragment extends BaseFragment implements View.OnClickListener {
    View view;
    @Bind(R.id.tv_category)
    TextView category;
    @Bind(R.id.homeRecyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.home_menu)
    FloatingActionMenu menu1;

    LinearLayoutManager layoutManager;

    @Bind(R.id.fab_marriage)
    FloatingActionButton marriage;

    @Bind(R.id.fab_career)
    FloatingActionButton career;
    @Bind(R.id.fab_edu)
    FloatingActionButton edu;
    @Bind(R.id.fab_health)
    FloatingActionButton health;
    @Bind(R.id.fab_wealth)
    FloatingActionButton wealth;


    private static final String ARG_PAGE_NUMBER = "page_number";

    ArrayList<Datum> feedList = new ArrayList<>();
    ArrayList<Datum> listData = new ArrayList<Datum>();
    private List<Datum> filteredFeeds = new ArrayList<>();
    ArrayList<Datum> tempList = new ArrayList<>();

    //variable to calculate end of scrolling on items of recycler view
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    //handle to the progressbar
    private ProgressBar progBar;

    private boolean isLoadedDefault = false;

    //variable to call more feed on user scroll action
    private int feedPage = 0;

    private final String VOLLEY_TAG = "getFeeds";

    private SwipeRefreshLayout swipeRefreshLayout;

    boolean isPullRefresh = false;

    boolean isMessage = false;

    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    HomeTab_Adapter adapter;

    DataController dbController;
    private final String DISCOVER_FEED = "DISCOVER_FEED";
    private static Long lastFeedTime;
    public HomeTabFragment() {

    }

    public static HomeTabFragment newInstance(int page) {
        HomeTabFragment fragment = new HomeTabFragment();
        try{

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);

        }catch (Throwable t){
            Log.e("HomeTabFragment",t.getMessage(),t);
        }
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{

            super.onSaveInstanceState(outState);

        }catch (Throwable t){
            Log.e("HomeTabFragment",t.getMessage(),t);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try{
        view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        Bundle b = getArguments();
        if (b != null) {
            isMessage = b.getBoolean("isMessage");
        }


        dbController = DataController.getsInstance(getActivity());
        List<Userdata_Bean> data = dbController.getAllData();
        if (data != null) {
            Constants.GLOBAL_USER_ID = data.get(0).getUserId();
            Constants.GLOBAL_USER_CONT_NO = data.get(0).getContact1();
            Constants.GLOBAL_U_LUCK_CHART = data.get(0).getLucky_Chart();
            Constants.GLOBAL_USER_EMAIL = data.get(0).getMail();
            dbController.closeDB();
            view.findViewById(R.id.llWelcome).setVisibility(View.VISIBLE);
            TextView tvUser = (TextView)view.findViewById(R.id.tvUser);
            tvUser.setText("Hi " + data.get(0).getUserFName().trim() + "!");
        }
        ButterKnife.bind(this, view);




        initView();

        }catch (Throwable t){
            Log.e("HomeTabFragment",t.getMessage(),t);
        }

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try{
        super.onActivityCreated(savedInstanceState);
        }catch (Throwable t){
            Log.e("HomeTabFragment",t.getMessage(),t);
        }

        //  initView();

    }

    @Override
    public void onResume() {
        try{
        super.onResume();

        }catch (Throwable t){
            Log.e("HomeTabFragment",t.getMessage(),t);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void initView() {
        try{
        recyclerView.setHasFixedSize(true);
        //recyclerView.hasStableIds(false);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        //reference to the progressbar
        progBar = (ProgressBar) view.findViewById(R.id.progressBar);

       /* TextView tv_title = (TextView)view.findViewById(R.id.tv_title_page);
        tv_title.setText("Home");*/


        //
        // feedPage++;

        //fetch initial Content feed
        //if(!isLoadedDefault) {

        //  }

        getFeed(feedPage);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeed(feedPage);
                //   isLoadedDefault=true;
            }
        });


        //This are some optional methods for customizing
        // the colors and size of the loader.
        swipeRefreshLayout.setColorSchemeResources(
                R.color.blue_s,       //This method will rotate
                R.color.red_s,        //colors given to it when
                R.color.yellow,     //loader continues to
                R.color.green_s);     //refresh.

        //setSize() Method Sets The Size Of Loader
        // ************* For Bug Testing  ************
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //Below Method Will set background color of Loader
        //*************      ********************
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);

        adapter = new HomeTab_Adapter(getActivity(), feedList, dbController);
        recyclerView.setAdapter(adapter);

        if (isMessage) {

            Log.e("Comming Through Messa", "In App :-" + isMessage);
            adapter.getFilter().filter("");
            adapter.notifyDataSetChanged();
        }


        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Fab Menu On Click", "");
                if (menu1.isOpened()) {
                    Toast.makeText(getActivity(), "Menu Clicked", Toast.LENGTH_SHORT).show();
                }

                menu1.toggle(true);
            }
        });

        menu1.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                Log.e("Fab Menu", "" + opened);

                if (!opened) {
                    category.setText("");
                    category.setVisibility(View.GONE);
                    adapter.resetData();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        menus.add(menu1);

        menu1.hideMenuButton(false);


        int delay = 400;
        for (final FloatingActionMenu menu : menus) {

            Log.e("Menu ", "" + menu.getId());
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        menu1.setClosedOnTouchOutside(true);


        career.setOnClickListener(this);
        health.setOnClickListener(this);
        marriage.setOnClickListener(this);
        wealth.setOnClickListener(this);
        edu.setOnClickListener(this);
        }catch (Throwable t){
            Log.e("HomeTabFragment",t.getMessage(),t);
        }

    }


    public void getFeed(int page) {
        try {

            //start Progress bar before network call start
            if (swipeRefreshLayout.isRefreshing()) {
                progBar.setVisibility(View.GONE);
                isPullRefresh = true;
            } else {
                progBar.setVisibility(View.VISIBLE);
            }
            String strDiscoverFeedPref = SharedPreferenceManager.getSharedInstance().getStringFromPreferances(DISCOVER_FEED);
            if (strDiscoverFeedPref != null && !strDiscoverFeedPref.isEmpty()) {
                feedList = (ArrayList<Datum>) ObjectSerializerHelper.stringToObject(strDiscoverFeedPref);

            }
            //check Internet connection
            if (!Utils.isConnectedToInternet(getActivity())) {
                Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
                progBar.setVisibility(View.GONE);
                if (isPullRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                return;

            }
            if ((lastFeedTime != null && (System.currentTimeMillis()-lastFeedTime)< Constants.REFRESH_TIME_IN_MILLISECONDS)) {
                progBar.setVisibility(View.GONE);
                if (isPullRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } else {

                JsonObject data = new JsonObject();

                Log.e("Page Counter", " -->" + page);
                data.addProperty("userId", "" + Constants.GLOBAL_USER_ID);
                data.addProperty("startindex", "" + 0);
                data.addProperty("count", "" + 200);
                data.add("source", Constants.getDeviceInfo());
                Log.e(" JSON for Feed", "" + data.toString());


                GsonRequestPost<FeedData> myReq = new GsonRequestPost<>(
                        Request.Method.POST, Constants.userFeeds, FeedData.class, null,
                        successListener(), errorListener(), data);

                VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, VOLLEY_TAG);
            }

        } catch (Throwable t) {
            Log.e("HomeTabFragment", t.getMessage(), t);
        }

    }

    @Override
    public void onDestroy() {
        try{
        super.onDestroy();


        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(VOLLEY_TAG);
        if (dbController != null) {
            dbController.closeDB();
        }
        }catch (Throwable t){
            Log.e("HomeTabFragment",t.getMessage(),t);
        }

    }


    private Response.Listener<FeedData> successListener() {
        return new Response.Listener<FeedData>() {
            @Override
            public void onResponse(FeedData response) {


                Log.e("Response Packet", "Data Size : " + response.getGetAllContentsResult());

                progBar.setVisibility(View.INVISIBLE);
                if (listData.size() > 0) {
                    listData.clear();
                }


                if (response != null) {

                    if (response.getGetAllContentsResult().getData() != null) {
                        listData = (ArrayList<Datum>) response.getGetAllContentsResult().getData();

                        if(listData !=null)
                        {
                            if(listData.size()>0)
                            {
                                feedPage += listData.size();
                               // feedPage = Integer.valueOf(listData.get(size).getId());
                                Log.e("FeedPage", " " + feedPage);
                            }else
                            {
                                //Toast.makeText(getActivity(), "No Data available", Toast.LENGTH_LONG).show();
                                if (isPullRefresh) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                Log.e("Empty Result", "---0000");
                                return;
                            }
                        }


                    } else {
                        if (isPullRefresh) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Toast.makeText(getActivity(), response.getGetAllContentsResult().getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }


                } else {
                    Log.e("Response Packet", "Data empty");
                }

                if (listData == null) {
                    return;
                }

                if(listData.size()==feedList.size() || listData.size()>feedList.size()||listData.size()<feedList.size())
                {
                    feedList.clear();
                    for (Datum d : listData) {
                        feedList.add(d);
                    }

                }else
                {
                    for (Datum d : listData) {
                        feedList.add(d);
                    }
                }
               SharedPreferenceManager.getSharedInstance().setStringInPreferences(DISCOVER_FEED, ObjectSerializerHelper.objectToString(feedList));
                adapter.notifyDataSetChanged();
                if (isPullRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                lastFeedTime = System.currentTimeMillis();
            }


        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progBar.setVisibility(View.INVISIBLE);
                Log.i("Debug: MainActivity", error.toString());
            }
        };
    }


    class DateSortComp implements Comparator<Datum> {

        @Override
        public int compare(Datum e1, Datum e2) {
            try{
            long refTime1=new Date(e1.getApprovalDate()).getTime();
            long refTime2=new Date(e2.getApprovalDate()).getTime();

            if(refTime1 < refTime2){
                return 1;
            } else {
                return -1;
            }
            }catch (Throwable t){
                Log.e("DateSortComp",t.getMessage(),t);
            }
        return -1;
        }
    }



    /*
  * Listener for user scroll on recycler view
  * helps to fetch the next set of movie feed on user scroll action
  *
  * */
    public void addRecyclerViewScrollListener() {

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try{
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    //nearing the end of the total items
                    //need to fetch the next page

                    //increment the page count
                    //  feedPage++;

                    //fetch the next page
                    getFeed(feedPage);

                    loading = true;
                }
                }catch (Throwable t){
                    Log.e("DateSortComp",t.getMessage(),t);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        try{
        tempList = feedList;
        adapter.resetData();
        adapter.notifyDataSetChanged();
        if (v == health) {


            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Health");
            adapter.getFilter().filter("" + 600004);
            adapter.notifyDataSetChanged();

            // }
        } else if (v == wealth) {

            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Wealth");
            adapter.getFilter().filter("" + 600007);
            adapter.notifyDataSetChanged();

        } else if (v == edu) {

            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Education");
            adapter.getFilter().filter("" + 600003);
            adapter.notifyDataSetChanged();

        } else if (v == career) {

            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Job/Business");
            adapter.getFilter().filter("" + 600002);
            adapter.notifyDataSetChanged();

        } else if (v == marriage) {
            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Marriage & Relationship");
            adapter.getFilter().filter("" + 600006);
            adapter.notifyDataSetChanged();

        }

        }catch (Throwable t){
            Log.e("DateSortComp",t.getMessage(),t);
        }
    }


    public void showProgressDialog(Activity ctx, String msg) {
try{

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ctx);
        }

        progressDialog.getLayoutInflater();
        progressDialog.setMessage(msg);
        progressDialog.show();
}catch (Throwable t){
    Log.e("DateSortComp",t.getMessage(),t);
}
    }

    public void dismissDialog() {
        try{
        progressDialog.dismiss();
        }catch (Throwable t){
            Log.e("DateSortComp",t.getMessage(),t);
        }
    }

    ProgressDialog progressDialog;
}
