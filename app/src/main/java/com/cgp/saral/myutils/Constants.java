package com.cgp.saral.myutils;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * Created by karamjeetsingh on 03/09/15.
 */
public class Constants {
   // public static ProgressDialog progressDialog;
    private static int SPLASH_TIME_OUT = 2000;


    public enum ServiceLevel
    {
        SESSION_INITIATED, SESSION_ACQUIRED, SEND_CHAT
    }


    public static SparseArray<String> strContents=new SparseArray<>();


    public static boolean isGoogleServices=false;

    public static String IMAGE_DIRECTORY_NAME ="Saral App";

//"Interests",

    public static String intrestData[] = { "Marriage & Relationship", "Education", "Job/Business", "Health", "Wealth"};

    public static final int HEALTH = 0;
    public static final int WEALTH = 1;
    public static final int CAREER = 2;
    public static final int EDUCATION = 3;
    public static final int MR = 4;
    public static final int FAV = 5;
    public static final int UNFAV = 6;
    public static final int RESET = 9;

    public static Context ctx = null;
    public static String GLOBAL_USER_CONT = "''";
    public static String GLOBAL_USER_CONT_NO = "name";
    public static String GLOBAL_USER_NAME = "name";
    public static String GLOBAL_USER_ID = "''";
    public static String GLOBAL_U_LUCK_NO = "";
   // public static String GLOBAL_U_LUCK_CHART = "1234-5678-RWBGX-DPOYX-SKNXX";
     public static String GLOBAL_U_LUCK_CHART ="";
   // public static String GLOBAL_USER_FIRST_NAME = "";

    public static String USER_PROFILE_PIC = "p_picture";


    public static final String LOCATION_STATE = "300002";
    public static final String LOCATION_DIST = "300003";
    public static final String LOCATION_CITY = "300004";


    public static final String PREFS_NAME = "MY_PREF";
    public static final String PREFS_KEY = "login";
    public static final String PREFS_USER_ID = "u_id";
    public static final String PREFS_KEY_GOOGLE_LOGOUT = "logout";
    public static final String PREFS_NAME_EDIT = "PREF";
    public static final String PREFS_LANG_POSITION = "lng";
    public static final String PREFS_GENDER_POSITION = "gen";
    public static final String PREFS_STATE_POSITION = "st";
    public static final String PREFS_DIST_POSITION = "dist";
    public static final String PREF_TOURGUIDE_NAME = "tuar";
    public static final String PREFS_TOURGUIDE_KEY = "key";
    public static final String FRAGMENT_ID = "id";

    /*For RegistrationFragment data */
    public static final String MOBILE_NO = "mobile";
    public static final String USER_PASS = "password";

    /* User Table Column name*/
    public static final String USER_ID = "id";
    public static final String USER_STATUS_C = "status";
    public static final String USER_NAME_C = "f_name";
    public static final String USER_PASS_C = "password";
    public static final String USER_DOB_C = "dob";
    public static final String USER_EMAIL_C = "email";
    public static final String USER_CONTACT_C = "contact1";
    public static final String USER_GENDER_C = "gender";
    public static final String USER_IMAGEURL_C = "p_picture";
    public static final String USER_LANGUAGE_C = "language";
    public static final String USER_STATE_C = "state";
    public static final String USER_INTREST_C = "favorites";
    public static final String USER_SOCIAL_TYPE_C = "social_type";
    public static final String USER_SOCIAL_ID_C = "social_id";

    public static final String USER_COUNTRY_C = "country";
    public static final String USER_CITY_C = "city";
    //  public static final String USER_MODI_DATE_C = "modified_date";
    public static final String USER_DISTRICT_C = "district";

    public static final String USER_ADDRESS_C = "address";
    public static final String USER_LUCKY_NO_C = "lucky_no";
    public static final String USER_LUCKY_CHART_C = "lucky_chart";


    public static boolean STATUS = false;


    public static String strFormat = "yyyy-MM-dd'T'HH:mm:ss'M'SSS";

    public static String strFormatChat = "yyyy-MM-dd HH:mm:ss SSS";

    public static String GLOBAL_SETTINGS = "GLOBAL_SETTINGS";

    //for testing server
    //public static final String IMAGE_FILE_UPLOAD_URL = "http://appuitest.cgparivar.com/admin/images/fileUpload.php";

    // for production server
    public static final String IMAGE_FILE_UPLOAD_URL = "http://appui.cgparivar.com/admin/images/fileUpload.php";


    //for production server
    public static String baseURL = "http://appapi.saralvaastu.com/";

    //for testing server
    //public static String baseURL = "http://appapitest.saralvaastu.com/";


    public static String userRegURL = baseURL + "User.svc/CreateUser";
    public static String userUpdateURL = baseURL + "User.svc/UpdateUser";

    public static String userLogin = baseURL + "Auth.svc/Login";

    public static String userForgotPURL = baseURL + "Auth.svc/ForgotPassword";
    public static String userChangePassword = baseURL + "Auth.svc/ChangePassword";
    public static String userIsRegisteredUserURL = baseURL + "Auth.svc/IsRegisteredUser";
    public static String isUserRegisteredBySocial = baseURL + "Auth.svc/IsRegisteredBySocial";
    public static String userResetPassword = baseURL + "Auth.svc/ResetPassword";
    public static String userFeeds = baseURL + "Content.svc/GetAllContents";

    public static String messageBoardFeeds = baseURL + "Content.svc/SearchContent";



    public static String postFeedAction = baseURL + "Content.svc/PostAction";


    public static String locationMaster = baseURL + "User.svc/GetAllLocations";

    public static String searchFeedByCategory = baseURL + "Content.svc/GetAllContents";

    public static String getChatSessionId = baseURL + "Chat.svc/GetChatSessionId";

    public static String chatStartSession = baseURL + "Chat.svc/StartSession";

    public static String sendChatText = baseURL + "Chat.svc/SendChatText";

    public static String getChatTextHistory = baseURL + "Chat.svc/GetChatHistoryBySessionId";

    public static String postFeedback = baseURL + "User.svc/AddUserRating";

    public static String location = "12.4214425,9.3464666";

    public static String SLIDER_IMG_PATH = "http://appapi.saralvaastu.com/sliderimages/";
    public static String LUCKY_CHAT_URL = "http://luckychartapi.cgparivar.com/api.php";
    public static String BOOKNOW_URL = "http://api.cgparivar.com/service_request.php";

    public static String LUCKY_CHAT_APIKEY = "saralvaastu";
    public static JsonObject getDeviceInfo() {
        JsonObject data = new JsonObject();
        JsonObject d = new JsonObject();
        d.addProperty("DeviceName", getDeviceName());
        if (ctx != null) {

            d.addProperty("GPSLocation", location);
            // d.addProperty("GPSLocation", getLocation(ctx));

            //  Log.e("Location update", ""+getLocation(ctx));
        } else {
            d.addProperty("GPSLocation", location);
        }

        d.addProperty("GPSLocation", location);
        d.addProperty("SourceType", "Mobile");

        d.addProperty("IPAddress", "");
        d.addProperty("MACAddress", "");


        return d;
    }


    public static HashMap<String, String> colorMap = new HashMap<>();
    public static HashMap<String, String> directionMap = new HashMap<>();




    public static void initColorMap()
    {

        //White,Blue,Red,Orange,Yellow,Green
       // ,Gold,Silver,Black,,,,Pink,,Brown,
        Constants.colorMap.put("R","Red");
        Constants.colorMap.put("D","Gold");
        Constants.colorMap.put("S", "Silver");
        Constants.colorMap.put("B","Blue");
        Constants.colorMap.put("G","Green");
        Constants.colorMap.put("Y","Yellow");
        Constants.colorMap.put("O","Orange");
        Constants.colorMap.put("W","White");
        Constants.colorMap.put("P","Pink");
        Constants.colorMap.put("K", "Black");
        Constants.colorMap.put("N", "Brown");
        Constants.colorMap.put("X", "");

        // D- Gold
        // P- Pink
        // S- Siver
        // K- Black
        // N- Brown

        //X - No Colors


        // DPSXY
    }
    public static void initDirectionMap()
    {
        Constants.directionMap.put("1","N");
        Constants.directionMap.put("2","NE");
        Constants.directionMap.put("3", "E");
        Constants.directionMap.put("4","ES");
        Constants.directionMap.put("5","S");
        Constants.directionMap.put("6","SW");
        Constants.directionMap.put("7","W");
        Constants.directionMap.put("8","NW");

    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static String getLocation(Context ctx) {
        String strLocation = "";


        FallbackLocationTracker tracker = new FallbackLocationTracker(ctx);
        if (tracker != null) {
            tracker.start();

            Location location = tracker.getLocation();
            Log.e("GetLocation", " -->" + location);
            if (location != null) {
                strLocation = location.getLatitude() + "" + location.getLongitude();
            }
        }

        Log.e("GetLocation", " -->" + strLocation);
        return strLocation;
    }


    public static String getFormattedDate(String strDate) {
        String strDateF = "";
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat, Locale.getDefault());
       // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        strDateF = sdf.format(new Date(strDate));

        Log.e("Formatted Date for DOB", "" + strDateF);
        return strDateF;
    }

    public static String getFormattedDateChat(String strDate) {
        String strDateF = "";
        SimpleDateFormat sdf = new SimpleDateFormat(strFormatChat, Locale.getDefault());
        // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        strDateF = sdf.format(new Date(strDate));

        Log.e("Chat Time", "" + strDateF);
        return strDateF;
    }

    // get value from hashmap through hash key
    public static String containKey(String key, HashMap<Integer, String> map) {
        String value = "";
        int k = Integer.parseInt(key);
        Log.e("Key", " key is " + key);

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getKey().equals(k)) {
                System.out.println(entry.getValue());
                Log.e("Key", " key is ff" + entry.getValue());
                value = entry.getValue();
            }
        }
        return value;
    }
    public static String containKeyDirection(String key, HashMap<String, String> map) {
        String value = "";

        Log.e("Key", " key is " + key);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                System.out.println(entry.getValue());
                Log.e("Key", " key is ff" + entry.getValue());
                value = entry.getValue();
            }
        }
        return value;
    }


    // get value from hashmap through hash key
    public static String containKeyColor(String key, HashMap<String, String> map) {
        String value = "";

        Log.e("Key", " key is " + key);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                System.out.println(entry.getValue());
                Log.e("Key", " key is ff" + entry.getValue());
                value = entry.getValue();
            }
        }
        return value;
    }

    public static String getFormatDate(Date d) {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        myFormat = sdf.format(d);
        return myFormat;

    }

    /* for RegistrationSecondpage*/
    public static String getdata(String val, HashMap<Integer, String> map) {
        String s = "";
        Set set = map.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            if (m.getValue().equals(val)) {
                s = m.getKey().toString();
                System.out.println(m.getKey() + ":" + m.getValue());

            }
        }
        return s;
    }


  /*  public static synchronized void showProgressDialog(Context ctx, String msg) {


        if(progressDialog ==null)
        {
            progressDialog = new ProgressDialog(ctx);
        }

        progressDialog.getLayoutInflater();
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public static synchronized void dismissDialog() {
        progressDialog.dismiss();
    }*/



    public enum SharedPreferenceKeys {
        USER_NAME("userName"),
        USER_EMAIL("userEmail"),
        USER_IMAGE_URL("userImageUrl");


        private String value;

        SharedPreferenceKeys(String value) {
            this.value = value;
        }

        public String getKey() {
            return value;
        }
    }

}
