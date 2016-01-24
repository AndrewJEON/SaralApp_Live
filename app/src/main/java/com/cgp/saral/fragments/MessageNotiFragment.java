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
import android.widget.ProgressBar;
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
public class MessageNotiFragment extends BaseFragment {
    View view;
    @Bind(R.id.tv_category)
    TextView category;
    @Bind(R.id.homeRecyclerView)
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Bind(R.id.home_menu)
    FloatingActionMenu menu;

    private static final String ARG_PAGE_NUMBER = "page_number";


    //  private static final String TRANSLATION_Y = "translationY";
   /* private ImageButton fab;
    private boolean expanded = false;
    private View fab_marriage_h;
    private View fab_career_h;
    private View fab_education_h;
    private View fab_wealth_h;
    private View fab_health_h;*/

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

    public MessageNotiFragment() {
        // Required empty public constructor
    }

    public static MessageNotiFragment newInstance(int page) {
        MessageNotiFragment fragment = new MessageNotiFragment();
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

    /* @Override
     public void setUserVisibleHint(boolean visible) {
         super.setUserVisibleHint(visible);
         if (visible && isResumed()) {   // only at fragment screen is resumed
             fragmentResume = true;
             fragmentVisible = false;
             fragmentOnCreated = true;
             // initView();

            getActivity().runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     fabInit(view);
                     collapseFab();
                 }
             });


         } else if (visible) {        // only at fragment onCreated
             fragmentResume = false;
             fragmentVisible = true;
             fragmentOnCreated = true;
             //fabInit(view);
             //collapseFab();
         } else if (!visible && fragmentOnCreated) {// only when you go out of fragment screen
             fragmentVisible = false;
             fragmentResume = false;
             //fabInit(view);
             //collapseFab();
         }
     }

 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        ButterKnife.bind(this, view);
        initView();
        //  setRetainInstance(true);

        dbController = DataController.getsInstance(getActivity());
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
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

        //check Internet connection
        if (!Utils.isConnectedToInternet(getActivity())) {
            Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
            return;

        }

        if (!fragmentResume && fragmentVisible) {   //only when first time fragment is created
            //initilization(view);


        }

      /*  getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fabInit(view);

            }
        });*/

        //
      //  feedPage++;

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


        //adding a scroll listener to fetch further feed on user scroll
        // addRecyclerViewScrollListener();

    }

    public void getFeed(int page) {


        //start Progress bar before network call start
        if (swipeRefreshLayout.isRefreshing()) {
            progBar.setVisibility(View.GONE);
            isPullRefresh = true;
        } else {
            progBar.setVisibility(View.VISIBLE);
        }


        JsonObject data = new JsonObject();
        data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);
        data.addProperty("startindex", "" + 0);
        data.addProperty("count", "200");
        data.addProperty("searchString", "");
        data.addProperty("contentType", "600001");
        data.addProperty("mediaType", "-1");

        data.add("source", Constants.getDeviceInfo());
        Log.e(" JSON for Feed", "" + data.toString());


        GsonRequestPost<FilteredFeedData> myReq = new GsonRequestPost<>(
                Request.Method.POST, Constants.messageBoardFeeds, FilteredFeedData.class, null,
                successListener(), errorListener(), data);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, VOLLEY_TAG);


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
                //add fetched feed to the FeedList bound to the adapter
               /* for (int i = 0; i < listData.size(); i++) {
                    feedList.add(listData.get(i));

                    Log.e("Size of AList", "" + feedList.size() + "  Counter " + i);
                }*/
                adapter.notifyDataSetChanged();
                // feedPage++;
                //notify the adapter that the data set has changed

                if (isPullRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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
                    //nearing the end of the total items
                    //need to fetch the next page

                    //increment the page count
                    // feedPage++;

                    //fetch the next page
                    //  getFeed(feedPage);

                    //   loading = true;
                }
            }
        });
    }

   /* public void fabInit(View view1) {

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
    }

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
/*

    @Override
    public void onClick(View v) {
        tempList = feedList;
        adapter.resetData();
        adapter.notifyDataSetChanged();
        if (v == fab_health_h) {

            expanded = !expanded;
            if (expanded) {

                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();
                category.setVisibility(View.VISIBLE);
                category.setText("Health");
                adapter.getFilter().filter("" + 600004);
                adapter.notifyDataSetChanged();

            }
        } else if (v == fab_wealth_h) {
            expanded = !expanded;
            if (expanded) {
                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();
                category.setVisibility(View.VISIBLE);
                category.setText("Wealth");
                adapter.getFilter().filter("" + 600007);
                adapter.notifyDataSetChanged();

            }
        } else if (v == fab_education_h) {
            expanded = !expanded;
            if (expanded) {
                expandFab();

                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();
                category.setVisibility(View.VISIBLE);
                category.setText("Education");
                adapter.getFilter().filter("" + 600003);
                adapter.notifyDataSetChanged();

            }
        } else if (v == fab_career_h) {
            expanded = !expanded;
            if (expanded) {
                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();
                category.setVisibility(View.VISIBLE);
                category.setText("Career");
                adapter.getFilter().filter("" + 600002);
                adapter.notifyDataSetChanged();

            }
        } else if (v == fab_marriage_h) {
            expanded = !expanded;
            if (expanded) {
                expandFab();
                adapter.resetData();
                adapter.notifyDataSetChanged();

            } else {
                collapseFab();
                category.setVisibility(View.VISIBLE);
                category.setText("Marriage & Relationship");
                adapter.getFilter().filter("" + 600006);
                adapter.notifyDataSetChanged();

            }
        }



    }*/
}
