package com.cgp.saral.myutils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.cgp.saral.R;
import com.cgp.saral.tuarguide.Showcase_guide;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by WeeSync on 29/09/15.
 */
public class Utils {


 //  public static GoogleApiClient mGoogleApiClient;

    public static int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public static HashMap sortByValues(HashMap map) {

        List list = new LinkedList(map.entrySet());

        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {

                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order

        HashMap sortedHashMap = new LinkedHashMap();

        for (Iterator it = list.iterator(); it.hasNext();) {

            Map.Entry entry = (Map.Entry) it.next();

            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    public static boolean isConnectedToInternet(Context context) {

        boolean isConnected = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    public static String getSelectedItemsAsString(String  _item, String [] str ) {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;
        Log.e(" utils "," String size "+str.length);
        for (int i = 0; i < str.length; ++i) {
            if (_item.contains(""+i)) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(str[i]);
            }
        }
        return sb.toString();
    }


    public static String [] chartAnalysis(String str)
    {

        String [] strArray;


        strArray= str.split("-");

       return strArray;
    }


    public static String intrestValue(List<Integer> list) {
        String data = "";
        StringBuilder sb = new StringBuilder();
        if (list.size() > 0 && !(list.size() == 0)) {
            for (Integer in : list) {
                if (in != -1) {
                    sb.append(in);
                }

            }
            data = sb.toString();
        }
        return data;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


   /* public static void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        }
    }
*/
    public static void rateApp(int count, final AppCompatActivity act, boolean flash)
    {
        try {


            if(flash)
            {
                rateAlert(act);

            }else {


                // Get the app's shared preferences
                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(act);

                // Get the value for the run counter
                int counter = app_preferences.getInt("counter", 0);

                // Do every x times
                int RunEvery = count;

                if (counter != 0 && counter % RunEvery == 0) {
                    //Toast.makeText(this, "This app has been started " + counter + " times.", Toast.LENGTH_SHORT).show();

                    rateAlert(act);
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putInt("counter", ++counter);
                    editor.commit(); // Very important

                }

            }
            // Increment the counter

        } catch (Exception e) {
            //Do nothing, don't run but don't break
        }

    }


    public static void showAlert(final Fragment act, String strName, String strMessage, final Bundle b)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                act.getActivity());
        alert.setTitle("Welcome, "+strName);
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_launcher); //app icon here
        alert.setMessage(strMessage);

        alert.setPositiveButton("Done !",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {


                      //  startActivity(new Intent(RegistrationFragment.this.getActivity(), Showcase_guide.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(b));
                        Intent i = new Intent(act.getActivity(), Showcase_guide.class);
                        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);


                        i.putExtras(b);
                        act.getActivity().startActivity(i);


                        act.getActivity().finish();
                    }
                });

        alert.show();
    }

    private static void rateAlert(final AppCompatActivity act)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                act);
        alert.setTitle("Please Rate");
        alert.setIcon(R.drawable.ic_launcher); //app icon here
        alert.setMessage("Thanks for using this Saral App. Please take a moment to rate it.");

        alert.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //Do nothing
                    }
                });

        alert.setNegativeButton("Rate it",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // final String appName = getApplicationContext().getPackageName();
                        final String appName = "com.cgp.saral";
                        try {
                            act.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id="
                                            + appName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            act.startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id="
                                            + appName)));
                        }

                    }
                });
        alert.show();

    }


    public static String CONTACTS = "{\n" +
            "\t\tcorporate: {\n" +
            "\t\t\tphone: \"+9122 61092732\",\n" +
            "\t\t\temail: \"app@saralvaastu.com\"\n" +
            "\t\t},\n" +
            "\t\tbranches: [\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Maharashtra\",\n" +
            "\t\t\tphone: \"+9192233 03355\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Karnataka\",\n" +
            "\t\t\tphone: \"+9194482 86755\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Gujarat\",\n" +
            "\t\t\tphone: \"+9196620 15070\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Goa\",\n" +
            "\t\t\tphone: \"+9192253 65755\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Delhi\",\n" +
            "\t\t\tphone: \"+9185889 20616\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Rajasthan\",\n" +
            "\t\t\tphone: \"+9193147 20809\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Madhya Pradesh\",\n" +
            "\t\t\tphone: \"+9191110 04406\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Chhattigarh\",\n" +
            "\t\t\tphone: \"+9191110 18288\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Uttar Pradesh\",\n" +
            "\t\t\tphone: \"+9193354 56002\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Tamil Nadu\",\n" +
            "\t\t\tphone: \"+9193601 01001\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\tname:\t\"Haryana\",\n" +
            "\t\t\tphone: \"+9193715 55542\"\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}";

    public static String getSliderImagePath(int i){
        String folder = "";
        if(deviceWidth <= 200){
            folder = "200";
        }else if(deviceWidth <= 320){
            folder = "320";
        }else if(deviceWidth <= 480){
            folder = "480";
        }else if(deviceWidth <= 720){
            folder = "720";
        }else if(deviceWidth <= 960){
            folder = "960";
        }else{
            folder = "1280";
        }
        return Constants.SLIDER_IMG_PATH + "/" + folder +"/" +i + ".jpg" ;
    }
}
