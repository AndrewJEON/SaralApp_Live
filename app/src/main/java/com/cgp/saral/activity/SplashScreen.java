package com.cgp.saral.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cgp.saral.R;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.GetAllLocationsResult;
import com.cgp.saral.model.LocationData;
import com.cgp.saral.model.LocationItems;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.SharedPreferenceManager;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.PicassoSingleton;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity implements SensorEventListener{
    private static int SPLASH_TIME_OUT = 5000;
    //private static long SPLASH_TIME_OUT =16000;
    boolean status = false;
   // private GifImageView gifImageView;
    SharedPreferences preferences;
    FragmentManager fragmentManager;
   // byte[] imagedata = null;
    boolean condtion = false;
    DataController dataController;
    Context ctx;
    ProgressBar bar;

    private SensorManager mSensorManager;
    private Sensor mGyroSensor;
    boolean userExist;

    private ViewFlipper sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
         // check user exist or not
        preferences = getApplicationContext().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        condtion = preferences.getBoolean(Constants.PREFS_KEY, false);
        preferences = getApplicationContext().getSharedPreferences(Constants.PREF_TOURGUIDE_NAME, MODE_PRIVATE);
        status = preferences.getBoolean(Constants.PREFS_TOURGUIDE_KEY, false);

        bar = (ProgressBar) findViewById(R.id.progress_bar_splash);

        ctx = getApplicationContext();

        String strId = preferences.getString(Constants.PREFS_USER_ID, "");

        if (!strId.equals("")) {
            Constants.GLOBAL_USER_ID = strId;
        }

        if (Constants.ctx == null) {
            Constants.ctx = ctx;
        }
       // splashScreen();

        /* Slider images*/
        sliderView = (ViewFlipper) findViewById(R.id.view_flipper);
        Animation animationFlipIn  = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation animationFlipOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        sliderView.setInAnimation(animationFlipIn);
        sliderView.setOutAnimation(animationFlipOut);
        Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                //animation started event
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                //TODO animation stopped event
                if (sliderView.getDisplayedChild()==sliderView.getChildCount()){
                    sliderView.stopFlipping();
                }
            }
        };

        //Get a reference to one of the animations set on the ViewFlipper
        //In this example, I used the "In Animation"
        sliderView.getInAnimation().setAnimationListener(mAnimationListener);


        for(int i = 1; i <= 4; i++){
            final ImageView imageView = new ImageView(ctx);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Log.i("SplashScreen:", Utils.getSliderImagePath(i));
            PicassoSingleton.getPicassoInstance(ctx).load(Utils.getSliderImagePath(i)).into(imageView,new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    sliderView.addView(imageView);
                }

                @Override
                public void onError() {

                }
            });


        }
        sliderView.startFlipping();


        /* End Slider images*/
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        PackageManager PM= this.getPackageManager();
        boolean gyro = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        // Check sensor avalable or not
        if(gyro){

              //  Toast.makeText(getApplicationContext(),"Gyroscope sensor is present", Toast.LENGTH_LONG).show();
        }else
        {
           // Toast.makeText(getApplicationContext(),"Gyroscope sensor is not present", Toast.LENGTH_LONG).show();
        }


        Log.e("User Auth Splash Screen", " -->" + condtion + "" + strId + " ShowCase " + status);
        if (condtion != false) {

        }

        dataController = DataController.getsInstance(this);
        userExist = dataController.isUserExist();

        Log.e("UserAuthoriseActivity", "condtion " + userExist);


        processData();
      //  doProcessForward();

       // DataController.getsInstance(this).closeDB();


      //  Log.e("Splash Screen",""+gifImageView.getFramesDisplayDuration()*1000);
       // Log.e("Splash Screen Frame, "" +gifImageView.getFramesDisplayDuration())

        SharedPreferenceManager.getSharedInstance().setStringInPreferences("contacts", Utils.CONTACTS);

    }

    public void doProcessForward()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.e("Handler", "CAll Handler");


                if (userExist == true) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, UserAuthoriseActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }


   /* public void splashScreen() {
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        new GifDataDownloader(this) {
            @Override
            protected void onPostExecute(final byte[] bytes) {
                imagedata = bytes;
                gifImageView.setBytes(bytes);
                gifImageView.startAnimation();
                Log.d("SplashScreen", "GIF width is " + gifImageView.getGifWidth());
                Log.d("SplashScreen", "GIF height is " + gifImageView.getGifHeight());
              //  SPLASH_TIME_OUT=gifImageView.getFramesDisplayDuration()*1000;
            }
        }.execute("htt");
    }*/

    @Override
    protected void onStop() {
        super.onStop();
       // gifImageView.stopAnimation();
       // gifImageView.clear();
        // gifImageView=null;
        // imagedata =null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //gifImageView.stopAnimation();
       // gifImageView.clear();
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll("dataSync");
        // imagedata=null;
    }

    private void processData() {


        LocationItems location= dataController.lastRecordLocation();

        String lastRecordId=location.getId();
        Log.e("Location ", " Last Record Id " + lastRecordId);

        JsonObject data = new JsonObject();
        bar.setVisibility(View.VISIBLE);


        data.addProperty("lastIndexId",lastRecordId);
        data.add("sourceInfo", Constants.getDeviceInfo());

        GsonRequestPost<LocationData> myReq = new GsonRequestPost<>(
                Request.Method.POST, Constants.locationMaster, LocationData.class, null,
                successListener(), errorListener(), data);

        int socketTimeout = 50000;//50 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        VolleySingleton.getInstance(ctx).addToRequestQueue(myReq, "dataSync");

    }


    private Response.Listener<LocationData> successListener() {
        return new Response.Listener<LocationData>() {
            @Override
            public void onResponse(LocationData response) {

                Log.e("Master Data Sync", "" + response);


                if (response != null) {
                    GetAllLocationsResult result = response.getGetAllLocationsResult();


                    ArrayList<LocationItems> data = result.getData();

                    if (data != null) {

                        Log.e("--->",""+data.size());

                        bar.setVisibility(View.GONE);

                        boolean status=dataController.bulkInsertRecords(data);
                        if(status) {
                            doProcessForward();
                        }


                    } else {

                      //  Toast.makeText(SplashScreen.this, "Database is Up todate ", Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.GONE);
                        doProcessForward();

                    }


                }else
                {
                    bar.setVisibility(View.GONE);
                    doProcessForward();
                }


            }


        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.i("Debug: Splash Screen", error.toString());
                handleVolleyError(error);
                bar.setVisibility(View.GONE);
                doProcessForward();
            }
        };
    }

    String str;

    private void handleVolleyError(VolleyError error) {
        //if any error occurs in the network operations, show the TextView that contains the error message

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            str = "Oops! Your Connection Timed Out, Seems there is no Network !!!";
            //getResources(R.string.error_timeout).toString();

        } else if (error instanceof AuthFailureError) {
            str = "Oops! Saral Vaastu Says It Doesn\'t Recognize You";
            //mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof ServerError) {
            str = "Oops! Saral Vaastu Server Just Messed Up";
            // mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof NetworkError) {
            str = "Oops! Your Connection Timed Out May be Network Messed Up";
            //TODO
        } else if (error instanceof ParseError) {
            str = "Oops! Data Received Not Recieved Properly ";
            //TODO
        }
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        float angularXSpeed = event.values[0];
        //Log.e("Angular X speed level is: " , "" +angularXSpeed);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // important to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
