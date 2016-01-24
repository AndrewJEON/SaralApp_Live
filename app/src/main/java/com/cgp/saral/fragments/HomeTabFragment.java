package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
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


    /*private static final String TRANSLATION_Y = "translationY";
    private ImageButton fab;
    private boolean expanded = false;
    private View fab_marriage_h;
    private View fab_career_h;
    private View fab_education_h;
    private View fab_wealth_h;
    private View fab_health_h;*/

    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;


   /* private float offset1;
    private float offset2;
    private float offset3;
    private float offset4;
    private float offset5;*/


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

    public HomeTabFragment() {

    }

    public static HomeTabFragment newInstance(int page) {
        HomeTabFragment fragment = new HomeTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            dbController.closeDB();
        }
        ButterKnife.bind(this, view);



        initView();


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  initView();

    }

    @Override
    public void onResume() {
        super.onResume();

       // initView();


    }

    public void initView() {
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

        //check Internet connection
        if (!Utils.isConnectedToInternet(getActivity())) {
            Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
            return;

        }


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

        //adding a scroll listener to fetch further feed on user scroll
        // addRecyclerViewScrollListener();

    }


  /*  adapter.setOnItemClickListener(new HomeTab_Adapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            String name = users.get(position).name;
            Toast.makeText(UserListActivity.this, name + " was clicked!", Toast.LENGTH_SHORT).show();
        }
    });
*/
    public void getFeed(int page) {


        //start Progress bar before network call start
        if (swipeRefreshLayout.isRefreshing()) {
            progBar.setVisibility(View.GONE);
            isPullRefresh = true;
        } else {
            progBar.setVisibility(View.VISIBLE);
        }


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

    @Override
    public void onDestroy() {
        super.onDestroy();


        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(VOLLEY_TAG);
        if (dbController != null) {
            dbController.closeDB();
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
                        Toast.makeText(getActivity(), "No Data available", Toast.LENGTH_LONG).show();
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
              //  Collections.sort(feedList, new DateSortComp());


               /* if (feedList.size() == 0 && listData.size() > 0) {
                    for (Datum d : listData) {
                        feedList.add(d);
                    }
                } else {


                    for (Datum d : listData) {

                        String strListId = d.getId();
                        for (Datum f : feedList) {
                            String strFeedId = f.getId();
                            if (!strListId.equals(strFeedId)) {
                                feedList.add(d);

                            }
                        }

                    }
                }*/
                Log.e("Size of AList-->", "" + feedList.size());

                //add fetched feed to the FeedList bound to the adapter
               /* for (int i = 0; i < listData.size(); i++) {
                    feedList.add(listData.get(i));

                    Log.e("Size of AList", "" + feedList.size() + "  Counter " + i);
                }*/
                // feedPage++;
                //notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
                if (isPullRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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
            long refTime1=new Date(e1.getApprovalDate()).getTime();
            long refTime2=new Date(e2.getApprovalDate()).getTime();

            if(refTime1 < refTime2){
                return 1;
            } else {
                return -1;
            }
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
            }
        });
    }

  /*  public void fabInit(View view1) {

        Log.e("Fab Init", ""+expanded);

        final ViewGroup fabContainer = (ViewGroup) view1.findViewById(R.id.fab_container);
        fab = (ImageButton) view1.findViewById(R.id.fabmain);
        fab_health_h = view1.findViewById(R.id.fab_action_1);
        fab_wealth_h = view1.findViewById(R.id.fab_action_2);
        fab_education_h = view1.findViewById(R.id.fab_action_3);
        fab_career_h = view1.findViewById(R.id.fab_action_4);
        fab_marriage_h = view1.findViewById(R.id.fab_action_5);
        fab_health_h.setOnClickListener(this);
        fab_wealth_h.setOnClickListener(this);
        fab_education_h.setOnClickListener(this);
        fab_career_h.setOnClickListener(this);
        fab_marriage_h.setOnClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                category.setVisibility(View.GONE);
                adapter.resetData();
                adapter.notifyDataSetChanged();
                if (expanded) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            expandFab();
                        }
                    });


                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            collapseFab();

                        }
                    });

                }
            }
        });

        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = fab.getY() - fab_health_h.getY();
                fab_health_h.setTranslationY(offset1);
                offset2 = fab.getY() - fab_wealth_h.getY();
                fab_wealth_h.setTranslationY(offset2);
                offset3 = fab.getY() - fab_education_h.getY();
                fab_education_h.setTranslationY(offset3);
                offset4 = fab.getY() - fab_career_h.getY();
                fab_career_h.setTranslationY(offset4);
                offset5 = fab.getY() - fab_marriage_h.getY();
                fab_marriage_h.setTranslationY(offset5);
                return true;
            }
        });
    }*//**/

    /*//****************
    private void collapseFab() {
        fab.setImageResource(R.drawable.fab_add);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fab_health_h, offset1),
                createCollapseAnimator(fab_wealth_h, offset2),
                createCollapseAnimator(fab_education_h, offset3),
                createCollapseAnimator(fab_career_h, offset4),
                createCollapseAnimator(fab_marriage_h, offset5));
        animatorSet.start();
        animateFab();
    }

    private void expandFab() {
        fab.setImageResource(R.drawable.ic_close);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(fab_health_h, offset1),
                createExpandAnimator(fab_wealth_h, offset2),
                createExpandAnimator(fab_education_h, offset3),
                createExpandAnimator(fab_career_h, offset4),
                createExpandAnimator(fab_marriage_h, offset5)
        );
        animatorSet.start();
        animateFab();
    }

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private void animateFab() {
        Drawable drawable = fab.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }
*/

    @Override
    public void onClick(View v) {
        tempList = feedList;
        adapter.resetData();
        adapter.notifyDataSetChanged();
        if (v == health) {

           /* expanded = !expanded;
            if (expanded) {

                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {*/
            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Health");
            adapter.getFilter().filter("" + 600004);
            adapter.notifyDataSetChanged();

            // }
        } else if (v == wealth) {
           /* expanded = !expanded;
            if (expanded) {
                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();*/
            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Wealth");
            adapter.getFilter().filter("" + 600007);
            adapter.notifyDataSetChanged();

            // }
        } else if (v == edu) {
           /* expanded = !expanded;
            if (expanded) {
                expandFab();

                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();*/
            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Education");
            adapter.getFilter().filter("" + 600003);
            adapter.notifyDataSetChanged();

            // }
        } else if (v == career) {
           /* expanded = !expanded;
            if (expanded) {
                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();*/
            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Career");
            adapter.getFilter().filter("" + 600002);
            adapter.notifyDataSetChanged();

            //  }
        } else if (v == marriage) {
            /*expanded = !expanded;
            if (expanded) {
                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();*/
            menu1.close(true);
            category.setVisibility(View.VISIBLE);
            category.setText("Marriage & Relationship");
            adapter.getFilter().filter("" + 600006);
            adapter.notifyDataSetChanged();

            //}
        }


    }


    public void showProgressDialog(Activity ctx, String msg) {


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ctx);
        }

        progressDialog.getLayoutInflater();
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissDialog() {
        progressDialog.dismiss();
    }

    ProgressDialog progressDialog;
}
