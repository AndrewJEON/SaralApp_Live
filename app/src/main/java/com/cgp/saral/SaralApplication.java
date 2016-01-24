package com.cgp.saral;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.cgp.saral.customviews.TypefaceUtil;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.SharedPreferenceManager;
import com.cgp.saral.network.PicassoSingleton;
import com.cgp.saral.network.VolleySingleton;
import com.cgp.saral.observer.FragmentObserver;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;

/**
 * Created by WeeSync on 01/09/15.
 */
public class SaralApplication extends Application {
    private static final String TRACKER_ID = "UA-67132606-2";

    /**
     * The Analytics singleton. The field is set in onCreate method override when the application
     * class is initially created.
     */
    private static GoogleAnalytics analytics;

    /**
     * The default app tracker. The field is from onCreate callback when the application is
     * initially created.
     */
    private static Tracker tracker;

    /**
     * Access to the global Analytics singleton. If this method returns null you forgot to either
     * set android:name="&lt;this.class.name&gt;" attribute on your application element in
     * AndroidManifest.xml or you are not setting this.analytics field in onCreate method override.
     */
    public static GoogleAnalytics analytics() {
        return analytics;
    }

    /**
     * The default app tracker. If this method returns null you forgot to either set
     * android:name="&lt;this.class.name&gt;" attribute on your application element in
     * AndroidManifest.xml or you are not setting this.tracker field in onCreate method override.
     */
    public static Tracker tracker() {
        return tracker;
    }

/*
    private  RefWatcher  refWatcher ;

    public  static com.squareup.leakcanary.RefWatcher getRefWatcher ( Context  context )  {
        SaralApplication  Application  =  ( SaralApplication )  context . getApplicationContext ();
        return  Application . refWatcher ;
    }*/



    public FragmentObserver getObserver() {
        return observer;
    }

    FragmentObserver observer;
    @Override
    public void onCreate() {
        super.onCreate();

        observer = new FragmentObserver();



       // refWatcher = LeakCanary.install(this);

        VolleySingleton.getInstance(this);
        PicassoSingleton.getPicassoInstance(this);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/roboto/Roboto-Medium.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf




       Stetho.initialize(
               Stetho.newInitializerBuilder(this)
                       .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                       .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                       .build());

//        MultiDex.install(this);

        instantiateManagers();



        // TODO: Replace the tracker-id with your app one from https://www.google.com/analytics/web/



        int result= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        String strResult="";

        switch(result){
            case ConnectionResult.SUCCESS:
                strResult="SUCCESS";
                break;

            case ConnectionResult.SERVICE_MISSING:
                strResult="SERVICE_MISSING";
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                strResult="SERVICE_VERSION_UPDATE_REQUIRED";
                break;

            case ConnectionResult.SERVICE_DISABLED:
                strResult="SERVICE_DISABLED";
                break;
            case ConnectionResult.SERVICE_INVALID:
                strResult="SERVICE_INVALID";
                break;
            case ConnectionResult.SERVICE_UPDATING:
                strResult="SERVICE_UPDATING";
                break;


        }

        Log.e("Google Play Service", " --> "+strResult);


        if(strResult.equals("SUCCESS"))
        {
            Constants.isGoogleServices=true;
            analytics = GoogleAnalytics.getInstance(this);
            getTracker(TrackerName.APP_TRACKER); //setup app tracker
        }


        //7840097696

    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Method to instantiate all the managers in this app
     */
    private void instantiateManagers() {
        FacebookSdk.sdkInitialize(this);
        //Fresco.initialize(this);
        SharedPreferenceManager.getSharedInstance().initiateSharedPreferences(getApplicationContext());

    }

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER
    }

    protected HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            //GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            tracker = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)/*(TRACKER_ID)*/
                    // : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(TRACKER_ID)
                    // : analytics.newTracker(R.xml.ecommerce_tracker)
                    :analytics.newTracker(TRACKER_ID) ;
            tracker.enableAdvertisingIdCollection(true); //if Admob is used


          //  tracker = analytics.newTracker("UA-54478999-3");

            // Provide unhandled exceptions reports. Do that first after creating the tracker
            tracker.enableExceptionReporting(true);

            // Enable Remarketing, Demographics & Interests reports
            // https://developers.google.com/analytics/devguides/collection/android/display-features
            //tracker.enableAdvertisingIdCollection(true);

            // Enable automatic activity tracking for your app
            tracker.enableAutoActivityTracking(true);

            Thread.UncaughtExceptionHandler myHandler = new ExceptionReporter(
                    tracker,                                        // Currently used Tracker.
                    Thread.getDefaultUncaughtExceptionHandler(),      // Current default uncaught exception handler.
                    this);                                         // Context of the application.
            // Make myHandler the new default uncaught exception handler.
            Thread.setDefaultUncaughtExceptionHandler(myHandler);
            mTrackers.put(trackerId, tracker);
        }
        return mTrackers.get(trackerId);
    }
    public void trackEvent(Activity activity,String screenName, String category,String action)
    {

        //GoogleAnalytics analytics = GoogleAnalytics.getInstance(activity);
        Tracker tracker=getTracker(TrackerName.APP_TRACKER);
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.EventBuilder().setCategory(category) //"UX"
                .setAction(action) //"click"
                .setLabel(screenName + " event")
                        //.set(Fields.SCREEN_NAME, "help popup dialog")
                .build());

    }
    public void trackScreenView(Activity activity,String screenName)
    {


        analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        Tracker tracker=getTracker(TrackerName.APP_TRACKER);
        tracker.setScreenName(screenName);
        tracker.enableAutoActivityTracking(true);
        tracker.enableExceptionReporting(true);
        //tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    public void reportActivityStart(Activity activity)
    {
        analytics.reportActivityStart(activity);
    }
    public void reportActivityStop(Activity activity)
    {
        analytics.reportActivityStop(activity);
    }

}
