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
import com.cgp.saral.activity.MainActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
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
                        Intent i = new Intent(act.getActivity(), MainActivity.class);
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


    public static String GLOBAL_SETTINGS = "{ urls: { audioHelpEnglish : \"http://appapi.saralvaastu.com/mp3/help_english.mp3\", audioHelpHindi: \"http://appapi.saralvaastu.com/mp3/help_hindi.mp3\", about: \"https://www.youtube.com/embed/fe3WPhsH4Gg?autoplay=1&vq=small&showinfo=0\", floorPlan: \"https://www.youtube.com/embed/ftxqKYpEgIg?autoplay=1&vq=small&showinfo=0\" }, slider: { count : \"5\", url: \"http://appapi.saralvaastu.com/sliderimages/\" }, corporate: { phone: \"+9122 61092732\", email: \"support@saralvaastu.com\" }, branches: [ { name: \"Maharashtra\", phone: \"+91 9321333022\" }, { name: \"Karnataka\", phone: \"+91 9448286758\" }, { name: \"Gujarat\", phone: \"+91 9662015070\" }, { name: \"Goa\", phone: \"+91 9448286758\" }, { name: \"Delhi\", phone: \"+91 9321333022\" }, { name: \"Rajasthan\", phone: \"+91 9321333022\" }, { name: \"Madhya Pradesh\", phone: \"+91 9321333022\" }, { name: \"Chhattisgarh\", phone: \"+91 9321333022\" }, { name: \"Uttar Pradesh\", phone: \"+91 9321333022\" }, { name: \"Tamil Nadu\", phone: \"+91 9360101001\" }, { name: \"Haryana\", phone: \"+91 9321333022\" } ] }";

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

    public static String getAudioHelpEnglishUrl(){
        String url = "";
        try {
            JSONObject jsonObject = Utils.getJSONUrls();
            if(jsonObject != null) {
                JSONObject urls =jsonObject.getJSONObject("urls");
                if(urls!= null) {
                    url = urls.getString("audioHelpEnglish");
                }
            }
        }catch (Exception e){

        }
        return url;
    }

    public static String getAudioHelpHindiUrl(){
        String url = "";
        try {
            JSONObject jsonObject = Utils.getJSONUrls();
            if(jsonObject != null) {
                JSONObject urls =jsonObject.getJSONObject("urls");
                if(urls!= null) {
                    url = urls.getString("audioHelpHindi");
                }
            }
        }catch (Exception e){

        }
        return url;
    }
    public static JSONObject getJSONUrls(){
        String jsonString = SharedPreferenceManager.getSharedInstance().getStringFromPreferances(Constants.GLOBAL_SETTINGS);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        }catch (Exception e){

        }
        return jsonObject;
    }

    public static String getFloorPlanVideoUrl(){
        String floorPlan="";
        try {
            JSONObject jsonObject = Utils.getJSONUrls();
            if(jsonObject != null) {
                JSONObject urls =jsonObject.getJSONObject("urls");
                if(urls!= null) {
                    floorPlan = urls.getString("floorPlan");
                }
            }
        }catch (Exception e){

        }
        return floorPlan;
    }

    public static String getAboutVideoUrl(){
        String about="";
        try {
            JSONObject jsonObject = Utils.getJSONUrls();
            if(jsonObject != null) {
                JSONObject urls =jsonObject.getJSONObject("urls");
                if(urls!= null) {
                    about = urls.getString("about");
                }
            }
        }catch (Exception e){

        }
        return about;
    }

    public static <T> T getObjectFromJSON(String serializedData, Type tClass) {
        // Use GSON to instantiate this class using the JSON representation of the state

        Gson gson = new Gson();
       return (T) gson.fromJson(serializedData, tClass);
    }

    public static  String toJSONString(Object srcObj, Type tClass){
        Gson gson = new Gson();
        return gson.toJson(srcObj,tClass);
    }
}
