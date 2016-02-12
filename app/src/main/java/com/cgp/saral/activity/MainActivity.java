package com.cgp.saral.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgp.saral.R;
import com.cgp.saral.SaralApplication;
import com.cgp.saral.adapter.RightSideAdapter;
import com.cgp.saral.customviews.OnItemClickListener;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.fragments.About_Us;
import com.cgp.saral.fragments.BaseFragment;
import com.cgp.saral.fragments.CallbackTabFragment;
import com.cgp.saral.fragments.Contact_Us;
import com.cgp.saral.fragments.FeedBack;
import com.cgp.saral.fragments.FloorPUpload;
import com.cgp.saral.fragments.HomeTabFragment;
import com.cgp.saral.fragments.MessageNotiFragment;
import com.cgp.saral.fragments.TabsFragment;
import com.cgp.saral.interfc.StackedFragment;
import com.cgp.saral.model.DrawerItem;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.social.helper.FbConnectHelper;
import com.cgp.saral.social.helper.GooglePlusSignInHelper;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CameraActivity implements OnItemClickListener, View.OnClickListener, FbConnectHelper.OnFbSignInListener, GooglePlusSignInHelper.OnGoogleSignInListener,BaseFragment.OnFragmentInteractionListener {
    //
    ArrayList<DrawerItem> dataArray_right = new ArrayList<>();//implements NiceTabStrip.OnIndicatorColorChangedListener
    DrawerLayout mDrawerlayout;
    ListView list;
    RelativeLayout drawerPane;
    public static ActionBarDrawerToggle mDrawerToggle;
    RelativeLayout iv_setting;
    RightSideAdapter Right_Adapter;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    Typeface typeface;
    TextView username;
    ImageView iv_userpro;
    DataController dataController;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    Button callBack;
    public static MainActivity instanceMain;


    TextView tv_title;

    List<Userdata_Bean> dbUserData = new ArrayList<>();
    Userdata_Bean bean = null;

    OnItemClickListener listener;

    GooglePlusSignInHelper googlePlusSignInHelper;

    int cameraPagerId =0;

    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    // Required for camera operations in order to save the image file on resume.
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

      //  savedInstanceState.putInt("viewpagerid" , 4 );
        if (mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
        if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI_KEY)) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
        //cameraPagerId= savedInstanceState.getInt("viewpagerid");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Getters and setters.
     */

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // typeface = Typeface.createFromAsset(getAssets(), "fonts/roboto/Roboto-Medium.ttf");
        fragmentManager = getSupportFragmentManager();
        final Bundle b = getIntent().getExtras();

        instanceMain=MainActivity.this;

        preferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        editor = preferences.edit();



        FloorPUpload fragment;
        if (savedInstanceState != null) {
            fragment = (FloorPUpload) getSupportFragmentManager().findFragmentByTag("customtag");
        } else {
          //  fragment = new FloorPUpload();
          //  getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, "customtag").commit();
        }



        Constants.GLOBAL_USER_ID = preferences.getString(Constants.PREFS_USER_ID,"");

        Log.e("Main Act", "--->  "+Constants.GLOBAL_USER_ID);

        dataController = DataController.getsInstance(getApplicationContext());
        if (null != b) {

            bean = (Userdata_Bean) b.getSerializable("user");
        } else {
            Log.e("Bundle from Login/Reg", "--->  null else condition");
            dbUserData = dataController.getAllData();
            bean = dbUserData.get(0);
        }


        listener = this;
        //  checkUserStatus();

        // startTransaction(new TabsFragment(), "tabs");
        inIt();
        Constants.GLOBAL_USER_NAME = "Guest";
        if (null != bean) {

            setData();

        }


        ///  Picasso.with(this).load(userdata_bean.getImgurl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(iv_userpro);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("mDrawerlayout", "onDrawerClosed: " + getTitle());


            }
        };
        mDrawerlayout.setDrawerListener(mDrawerToggle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tv_title = (TextView)findViewById(R.id.tv_title_page);


        if(toolbar != null) {
            setSupportActionBar(toolbar);
           // getSupportActionBar().setTitle("SaralApp");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
           // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        iv_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDrawerlayout.openDrawer(drawerPane);
            }
        });

        Fill_RightList();


        Log.d("object array", "" + dataArray_right.size());
        Right_Adapter = new RightSideAdapter(dataArray_right, 1, MainActivity.this, listener);

        list.setAdapter(Right_Adapter);

        Log.e("MainActivity", "Call adapter");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MainActivity", "Call AdapterView" + position);
            }
        });


    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        Fragment f = fm.findFragmentByTag("viewPager");

        tv_title.setText("Home");
        Log.i("MainActivity", "popping backstack");
        if (f != null) {

            if (f instanceof TabsFragment) {
                ((TabsFragment) f).onBackPressed();
                Log.i("TabsFragment", "on Back Pressed");
            }
        } else {
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                fm.popBackStack();
                tv_title.setText("Home");
            } else {
                Log.i("MainActivity", "nothing on backstack, finish");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent startMain = new Intent(Intent.ACTION_MAIN);
                                startMain.addCategory(Intent.CATEGORY_HOME);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(startMain);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Are you sure to Exit now?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .setCancelable(false)
                        .setTitle("Exit Saral Vaastu");
                builder.show();


            }
        }


    }

    // initilize variable
    public void inIt() {
        username = (TextView) findViewById(R.id.drawer_userName);
        iv_userpro = (ImageView) findViewById(R.id.iv_userpro);
        //username.setTypeface(typeface);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        list = (ListView) findViewById(R.id.drawer_list_right);
        iv_setting = (RelativeLayout) findViewById(R.id.settingLayout);
        iv_setting.setVisibility(View.VISIBLE);
        iv_userpro.setOnClickListener(this);
        username.setOnClickListener(this);
        callBack = (Button) findViewById(R.id.callback);
        callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CallbackTabFragment();
                startTransaction(fragment, "callback");
                tv_title.setText("Call Back");

            }
        });
         ((SaralApplication) getApplication()).trackEvent(MainActivity.this, "MainActivity", "App Flow", "fragment Switching ");
    }

    // Filling the ArrayLists
    public void selectItemFromDrawer(int position) {

        tv_title.setText("Home");

        Fragment frag=new TabsFragment();

        Log.e("selectItemFromDrawer", " ---->" + position);
        if (position == 0) {
            tv_title.setText("Home");
            Log.e("selectItemFromDrawer", " ---->" + position);
            FragmentManager fm = getSupportFragmentManager();

            Fragment f = fm.findFragmentById(R.id.tab);


              //  Fragment ff= new HomeTabFragment();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.show(f);

                transaction.commit();


        }

        if (position == 1) {
            Log.e("selectItemFromDrawer", " ---->" + position);
            Fragment fragment = new MessageNotiFragment();
            startTransaction(fragment, "message");
            tv_title.setText("Saral Message");


        }
        if (position == 2) {

            Fragment fragment = new FloorPUpload();
            startTransaction(fragment, "plan_upload");
            tv_title.setText("Floor Plan");
            //getSupportActionBar().setTitle("Floor Plan Upload");
        }

        if (position == 3) {

            Utils.rateApp(0, MainActivity.this, true);
           // getSupportActionBar().setTitle("Feeds");
            tv_title.setText("");

        }
        if (position == 4) {

            Fragment fragment= new Contact_Us();
            startTransaction(fragment, "contact");
            getSupportActionBar().setTitle("Contact");
            tv_title.setText("Contact Us");
            // Toast.makeText(MainActivity.this, "Contact Us ..In Development", Toast.LENGTH_LONG).show();
        }
        if (position == 5) {
            Fragment fragment = new FeedBack();

            startTransaction(fragment, "feedback");
            tv_title.setText("Feedback");
            //getSupportActionBar().setTitle("Feedback");
        }
        if (position == 6) {
            Fragment fragment = new About_Us();
            startTransaction(fragment, "about_us");
            tv_title.setText("About Us");
            //getSupportActionBar().setTitle("About Us");

        }

        if (position == 7) {

            showProgressDialog(MainActivity.this, "Please Wait, Signing out !!!");

            logout_validate();
        }
        list.setItemChecked(position, true);
        setTitle(dataArray_right.get(position).getTitle());

        // Close the drawer
        mDrawerlayout.closeDrawer(drawerPane);
    }

    public void Fill_RightList() {
        dataArray_right.clear();
        // add menu item
        dataArray_right.add(new DrawerItem("HOME", R.drawable.ic_home));
        dataArray_right.add(new DrawerItem("SARAL MESSAGE", R.drawable.ic_news_letter_white));
        dataArray_right.add(new DrawerItem("FLOOR PLAN", R.drawable.ic_upload_white));
        dataArray_right.add(new DrawerItem("RATE APP", R.drawable.ic_apprate));
        dataArray_right.add(new DrawerItem("CONTACT US", R.drawable.ic_contact_us));
        dataArray_right.add(new DrawerItem("FEEDBACK", R.drawable.ic_feedback));
        dataArray_right.add(new DrawerItem("ABOUT US", R.drawable.ic_aboutus));
        dataArray_right.add(new DrawerItem("SIGN OUT", R.drawable.ic_signout_white));

    }

    //fragment transctions
    public void startTransaction(Fragment fragment, String str) {

         ((SaralApplication) getApplication()).trackEvent(MainActivity.this, "MainActivity", "App Flow", "fragment Switching " + str);
        Fragment f= fragmentManager.findFragmentById(R.id.tab);
        if(f.isVisible())
        {
            Log.e("HomeTabFragment","1");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(f);
            transaction.add(R.id.content_frame, fragment, str);
            transaction.addToBackStack(null);
            transaction.commit();
        }else if(fragment instanceof HomeTabFragment)
        {
           Log.e("HomeTabFragment","2");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //transaction.hide(f);
            transaction.replace(R.id.content_frame, fragment, str);
            transaction.addToBackStack(null);
            transaction.commit();

        }else
        {
            Log.e("HomeTabFragment","3");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //transaction.hide(f);
            transaction.replace(R.id.content_frame, fragment, str);
            transaction.addToBackStack(null);
            transaction.commit();
        }



    }

    public void removeFragmentbyTag(Fragment fragment, String str) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment, str);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onItemClicked(View v, int position) {
        mDrawerlayout.closeDrawer(drawerPane);
        Log.e("MainActivity", "Call onItemClicked" + position);
        selectItemFromDrawer(position);
    }

    @Override
    public void onClick(View v) {

        if (null != bean) {
            Log.e("Main Activity", "UserData Not Null");
            Intent i = new Intent(MainActivity.this, UserProfile_Activity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle b = new Bundle();
            b.putSerializable("dbuserdata", bean);
            i.putExtras(b);
            startActivity(i);


            //startActivity(new Intent(MainActivity.this, UserProfile_Activity.class).putExtra("dbuserdata", (Serializable) dbUserData));
            mDrawerlayout.closeDrawer(drawerPane);
        } else {
            Log.e("Main Activity", "UserData Null");
        }
        //getSupportFragmentManager().beginTransaction().replace(R.id.mailactivity_container,new HomeTabFragment()).commit();

    }


    public synchronized void showProgressDialog(Activity ctx, String msg) {


        if(progressDialog ==null)
        {
            progressDialog = new ProgressDialog(ctx);
        }

        progressDialog.getLayoutInflater();
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public  synchronized void dismissDialog() {
        progressDialog.dismiss();
    }

    public void setData() {
        username.setText(bean.getUserFName());
        Constants.GLOBAL_USER_NAME = bean.getUserFName();
        if (bean.getImgurl() != null) {
            Picasso.with(this).load(bean.getImgurl())
                    .placeholder(R.color.colorAccent).error(R.drawable.ic_dp_grey).into(iv_userpro);
        } else {
            iv_userpro.setImageResource(R.drawable.ic_dp_grey);
        }
    }

    public void checkUserStatus() {
        boolean status;
        status = dataController.isUserExist();

        if (status == true) {
            editor.putBoolean(Constants.PREFS_KEY, true);
            editor.commit();
        } else if (status == false) {
            editor.putBoolean(Constants.PREFS_KEY, false);
            editor.commit();
        }
    }


    private void updateActionBar(Fragment fragment) {
        ActionBar actionBar = getSupportActionBar();
        if (fragment instanceof StackedFragment) {
            StackedFragment tF = (StackedFragment) fragment;
            actionBar.setDisplayHomeAsUpEnabled(tF.getDisplayHomeAsUpEnabled());
            actionBar.setTitle("Message");
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(getString(R.string.app_name));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Constants.isGoogleServices) {
            ((SaralApplication) getApplication()).reportActivityStart(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (progressDialog != null) {
            dismissDialog();

        }
        if (dataController != null) {
            dataController.closeDB();
        }
        if(Constants.isGoogleServices) {
            ((SaralApplication) getApplication()).reportActivityStop(this);
        }
    }

    public void logout_validate() {


        Intent intent = new Intent(MainActivity.this, UserAuthoriseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = bean.getSocialtype();
        Log.e("Logout", " type " + type);
        String id = bean.getUserId();

        Log.e("Logout", " Id " + id);
        if (type != null) {
            if (type.equalsIgnoreCase("fb")) {
                Log.e("Main Activity", "This is fb");
                showProgressDialog(MainActivity.this, "Please Wait, Signing out !!!");
                long ides = dataController.updateStatus(0, id);
                Log.e("Main Activity", "Update row " + ides);
                dataController.deleteUsers();
                editor.putBoolean(Constants.PREFS_KEY, false);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoginManager.getInstance().logOut();
                    }
                }).start();



            } else if (type.equalsIgnoreCase("gplus")) {


                showProgressDialog(MainActivity.this, "Please Wait, Signing out !!!");

                long ides = dataController.updateStatus(0, id);
                Log.e("Google Plus Logout", "Update row " + ides);
                dataController.deleteUsers();
                editor.putBoolean(Constants.PREFS_KEY, false);
                editor.commit();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        googlePlusSignInHelper = new GooglePlusSignInHelper(MainActivity.this, MainActivity.this);
                        googlePlusSignInHelper.signOut();
                    }
                }).start();




            } else if (type.equalsIgnoreCase("normal")) {
                showProgressDialog(MainActivity.this, "Please Wait, Signing out !!!");
                long ides = dataController.updateStatus(0, id);
                dataController.deleteUsers();
                Log.e(" Normal Logout", "Update row " + ides);
                editor.putBoolean(Constants.PREFS_KEY, false);
                editor.commit();


            }
        } else {
            long ides = dataController.updateStatus(0, id);
            dataController.deleteUsers();
            Log.e("userprofile_activity", "Update row " + ides);
            editor.putBoolean(Constants.PREFS_KEY, false);
            editor.commit();


        }
        dismissDialog();
        startActivity(intent);
        finish();

    }



    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {

    }

    @Override
    public void OnFbError(String errorMessage) {

    }

    @Override
    public void OnGSignSuccess(Person mPerson, String emailAddress) {
        googlePlusSignInHelper = new GooglePlusSignInHelper(this, this);
        googlePlusSignInHelper.signOut();
      //  long ides = dataController.updateStatus(0, id);
        Log.e("Google Plus Logout", "Update row on GSign");
        editor.putBoolean(Constants.PREFS_KEY, false);
        editor.commit();
    }

    @Override
    public void OnGSignError(String errorMessage) {

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(int actionId) {

    }
}
