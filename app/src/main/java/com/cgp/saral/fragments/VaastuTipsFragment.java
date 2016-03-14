package com.cgp.saral.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cgp.saral.R;
import com.cgp.saral.adapter.HomeTab_Adapter;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.fab.FloatingActionMenu;
import com.cgp.saral.model.Datum;
import com.cgp.saral.model.FilteredFeedData;
import com.cgp.saral.model.Language;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.ObjectSerializerHelper;
import com.cgp.saral.myutils.SharedPreferenceManager;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VaastuTipsFragment extends BaseFragment {
    View view;
    @Bind(R.id.tv_category)
    TextView category;
    @Bind(R.id.homeRecyclerView)
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Bind(R.id.home_menu)
    FloatingActionMenu menu;

    private static final String ARG_PAGE_NUMBER = "page_number";

    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;


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

    //variable to call more feed on user scroll action
    private int feedPage = 0;

    private final String VOLLEY_TAG = "getFeeds";

    private SwipeRefreshLayout swipeRefreshLayout;

    boolean isPullRefresh = false;

    boolean isMessage = false;


    HomeTab_Adapter adapter;

    DataController dbController;
    private final String NEWS_FEED = "VAASTU_TIPS_FEED";
    private static Long lastFeedTime;
    HashMap<String,Integer> langKeyMap;
    Spinner  languageSpinner;
    Integer langKey;

    String langFilter = "", catFilter = "";
    public VaastuTipsFragment() {
        // Required empty public constructor
    }

    public static VaastuTipsFragment newInstance(int page) {
        VaastuTipsFragment fragment = new VaastuTipsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //  setUserVisibleHint(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        ButterKnife.bind(this, view);
        dbController = DataController.getsInstance(getActivity());
        initView();
        //  setRetainInstance(true);


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initView();
    }

    public void initView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        //reference to the progressbar
        progBar = (ProgressBar) view.findViewById(R.id.progressBar);
        menu.setVisibility(View.GONE);
        languageSpinner = (Spinner) view.findViewById(R.id.spinnerlanguage);

        //prepareLanguageSpinner();


        if (!fragmentResume && fragmentVisible) {   //only when first time fragment is created
            //initilization(view);


        }


        //fetch initial Content feed
        getFeed(feedPage);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeed(feedPage);
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
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //Below Method Will set background color of Loader
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);

        adapter = new HomeTab_Adapter(getActivity(), feedList, dbController);
        recyclerView.setAdapter(adapter);

    }

    private void prepareLanguageSpinner(){
        ArrayList<Language> langs = dbController.languageList();
        langKeyMap = new LinkedHashMap<String,Integer>();
        langKeyMap.put("Language",-1);
        for (Language l : langs) {
            langKeyMap.put(l.getName(),Integer.valueOf(l.getId()));
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1, new ArrayList<String>(langKeyMap.keySet()));

        // Specify the layout to use when the list of choices appears
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        languageSpinner.setAdapter(langAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                langKey = langKeyMap.get(((TextView)view).getText());
                langFilter = "";
                if(langKey >0) {
                    langFilter = langKey+"";
                    performFeedFilter();
                }else{
                    tempList = feedList;
                    adapter.resetData();
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void  performFeedFilter(){
        tempList = feedList;
        adapter.resetData();
       // adapter.notifyDataSetChanged();
        adapter.getFilter().filter(langFilter+","+catFilter);
        adapter.notifyDataSetChanged();
    }
    public void getFeed(int page) {


        //start Progress bar before network call start
        if (swipeRefreshLayout.isRefreshing()) {
            progBar.setVisibility(View.GONE);
            isPullRefresh = true;
        } else {
            progBar.setVisibility(View.VISIBLE);
        }

        String strDiscoverFeedPref = SharedPreferenceManager.getSharedInstance().getStringFromPreferances(NEWS_FEED);

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
            data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);
            data.addProperty("startindex", "" + 0);
            data.addProperty("count", "200");
            data.addProperty("searchString", "");
            data.addProperty("contentType", "600010");
            data.addProperty("mediaType", "-1");

            data.add("source", Constants.getDeviceInfo());
            Log.e(" JSON for Feed", "" + data.toString());


            GsonRequestPost<FilteredFeedData> myReq = new GsonRequestPost<>(
                    Request.Method.POST, Constants.messageBoardFeeds, FilteredFeedData.class, null,
                    successListener(), errorListener(), data);

            VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, VOLLEY_TAG);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (dbController != null) {
            dbController.closeDB();
        }

        //cancel any pending requests to avoid using network resources after activity is destroyed
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(VOLLEY_TAG);
    }


    private Response.Listener<FilteredFeedData> successListener() {
        return new Response.Listener<FilteredFeedData>() {
            @Override
            public void onResponse(FilteredFeedData response) {


                Log.e("Response Packet", "Data Size : " + response.getSearchContentResult());

                progBar.setVisibility(View.INVISIBLE);
                if (listData.size() > 0) {
                    listData.clear();
                }


                if (response != null) {

                    if (response.getSearchContentResult().getData() != null) {
                        listData = (ArrayList<Datum>) response.getSearchContentResult().getData();
                        if(listData !=null)
                        {
                            if(listData.size()>0)
                            {
                                feedPage+= listData.size();
                               // feedPage = Integer.valueOf(listData.get(size).getId());
                                Log.e("FeedPage", " " + feedPage);
                            }else
                            {
                                if (isPullRefresh) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                //Toast.makeText(getActivity(), "No Data available", Toast.LENGTH_LONG).show();
                                Log.e("Empty Result", "---0000");
                                return;
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), "No Data available", Toast.LENGTH_LONG).show();
                        return;
                    }


                } else {
                    Log.e("Response Packet", "Data empty");
                }

                if (listData == null) {
                    return;
                }


                if(listData.size()==feedList.size())
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

                Collections.sort(feedList, new DateSortComp());

                SharedPreferenceManager.getSharedInstance().setStringInPreferences(NEWS_FEED, ObjectSerializerHelper.objectToString(feedList));
                adapter.notifyDataSetChanged();
                // feedPage++;
                //notify the adapter that the data set has changed

                if (isPullRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                lastFeedTime = System.currentTimeMillis();
            }


        };
    }


    class DateSortComp implements Comparator<Datum> {

        @Override
        public int compare(Datum e1, Datum e2) {
            if(!e1.getApprovalDate().equals("") &&  !e2.getApprovalDate().equals(""))
            {
                long refTime1=new Date(e1.getApprovalDate()).getTime();
                long refTime2=new Date(e2.getApprovalDate()).getTime();

                if(refTime1 < refTime2){
                    return 1;
                } else {
                    return -1;
                }
            }else
            {
                return 1;
            }

        }
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


    /*
  * Listener for user scroll on recycler view
  * helps to fetch the next set of movie feed on user scroll action
  *
  * */
    public void addRecyclerViewScrollListener() {

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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

                }
            }
        });
    }


}
