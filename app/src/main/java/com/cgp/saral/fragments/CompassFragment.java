package com.cgp.saral.fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.cgp.saral.R;
import com.cgp.saral.customviews.Compass;
import com.cgp.saral.customviews.CompassView;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.fab.FloatingActionButton;
import com.cgp.saral.fab.FloatingActionMenu;
import com.cgp.saral.model.LuckyChartResponse;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.SharedPreferenceManager;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;
import com.google.gson.internal.Excluder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class CompassFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    View view;
    //TextView usr_name;
    SwitchCompat favor, unfavor;
    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String TAG = "CompassFragment";
    /*for fab*/
    private List<FloatingActionMenu> menus = new ArrayList<FloatingActionMenu>();
    private Handler mUiHandler = new Handler();
    static CompassView compassView;
    // MyCompassView compassView;

    private String luckyChart;
    private boolean notHavingSensor = true;

    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;
    @Bind(R.id.text_Note)
    TextView text_Note;
    @Bind(R.id.camp_cp)
    RelativeLayout camp_cp;

    @Bind(R.id.home_menu)
    FloatingActionMenu menu1;
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

    @Bind(R.id.compass_layout)
    RelativeLayout compass_layout;
    @Bind(R.id.ll)
    LinearLayout favDir_lay;

    @Bind(R.id.lay_no)
    LinearLayout favDirno_lay;

    @Bind(R.id.unfav_lay)
    LinearLayout unfavDir_lay;

    @Bind(R.id.unlay_no)
    LinearLayout unfavDirno_lay;


    @Bind(R.id.FavDir_parent)
    RelativeLayout FavDir_parent;

    @Bind(R.id.UnfavDir_parent)
    RelativeLayout UnfavDir_parent;

    @Bind(R.id.fabLayout)
    RelativeLayout fabLayout;
    @Bind(R.id.msg2)
    TextView msg;
    @Bind(R.id.msg3)
    TextView compassMessage;
    @Bind(R.id.wealthdir)
    TextView wealthdir;
    @Bind(R.id.healthdir)
    TextView healthdir;
    @Bind(R.id.edudir)
    TextView edudir;
    @Bind(R.id.jobdir)
    TextView jobdir;
    @Bind(R.id.marriagedir)
    TextView marriagedir;



    @Bind(R.id.tv_color1)
    TextView color1;
    @Bind(R.id.tv_color2)
    TextView color2;
    @Bind(R.id.tv_color3)
    TextView color3;
    @Bind(R.id.tv_color4)
    TextView color4;
    @Bind(R.id.tv_color5)
    TextView color5;


    @Bind(R.id.tv_uncolor1)
    TextView ucolor1;
    @Bind(R.id.tv_uncolor2)
    TextView ucolor2;
    @Bind(R.id.tv_uncolor3)
    TextView ucolor3;
    @Bind(R.id.tv_uncolor4)
    TextView ucolor4;
    @Bind(R.id.tv_uncolor5)
    TextView ucolor5;


    @Bind(R.id.tv_ncolor1)
    TextView ncolor1;
    @Bind(R.id.tv_ncolor2)
    TextView ncolor2;
    @Bind(R.id.tv_ncolor3)
    TextView ncolor3;
    @Bind(R.id.tv_ncolor4)
    TextView ncolor4;
    @Bind(R.id.tv_ncolor5)
    TextView ncolor5;
    @Bind(R.id.audio_help)
    CheckBox audioHelp;
    @Bind(R.id.audio_help_hindi)
    CheckBox audioHindiHelp;

    ArrayList<TextView> arrColors = new ArrayList<>();
    ArrayList<TextView> arrUColors = new ArrayList<>();
    ArrayList<TextView> arrNColors = new ArrayList<>();

    ArrayList<TextView> DirectionTextview = new ArrayList<>();
    ArrayList<String> directionList = new ArrayList<>();

    boolean isCompassVisible =false;


    String strFavColor, strUnFavColor, strNeutralColor;
    Compass compass;
    Context ctx;
    String fab;
    String unFab;

    String mr = "";
    String cr = "";
    String hl = "";
    String w = "";
    String e = "";
    SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private Sensor mGyroSensor;
    boolean favDirectionState = false;

    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayerHindi;
    public CompassFragment() {
        // Required empty public constructor
    }


    public static CompassFragment newInstance(int page) {
        CompassFragment fragment = new CompassFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
try{
        view = inflater.inflate(R.layout.fragment_campass, container, false);
        ButterKnife.bind(this, view);


        ctx = getActivity();
        try {

            arrColors = new ArrayList<>();
            arrUColors = new ArrayList<>();
            arrNColors = new ArrayList<>();

             DirectionTextview = new ArrayList<>();
            directionList = new ArrayList<>();
            DirectionTextview.add(wealthdir);
            DirectionTextview.add(healthdir);
            DirectionTextview.add(marriagedir);
            DirectionTextview.add(edudir);
            DirectionTextview.add(jobdir);

            arrColors.add(color1);
            arrColors.add(color2);
            arrColors.add(color3);
            arrColors.add(color4);
            arrColors.add(color5);

            arrUColors.add(ucolor1);
            arrUColors.add(ucolor2);
            arrUColors.add(ucolor3);
            arrUColors.add(ucolor4);
            arrUColors.add(ucolor5);


            arrNColors.add(ncolor1);
            arrNColors.add(ncolor2);
            arrNColors.add(ncolor3);
            arrNColors.add(ncolor4);
            arrNColors.add(ncolor5);

            Constants.initColorMap();
            Constants.initDirectionMap();

            loadLuckyChart();

            mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mGyroSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            PackageManager PM = getActivity().getPackageManager();
            boolean accel = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
            boolean comp = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
            boolean gyro = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

           /* TextView tvUser = (TextView)view.findViewById(R.id.tvUser);
            tvUser.setText("Hi " + Constants.GLOBAL_USER_NAME.trim() + "!");*/

            Log.e("Compass ",""+" Accel "+accel+"  Compass "+comp+" Gyro "+gyro);
            //Toast.makeText(getActivity()," Accel "+accel+"  Compass "+comp+" Gyro "+gyro,Toast.LENGTH_LONG).show();
            compass = new Compass(getActivity());
            compassMessage.setText(Html.fromHtml(getResources().getString(R.string.compass_message)));
            if (comp) {
                isCompassVisible =true;
                favDirectionState = false;
                text_Note.setVisibility(View.VISIBLE);
                camp_cp.setVisibility(View.VISIBLE);
                compass_layout.setVisibility(View.VISIBLE);
                FavDir_parent.setVisibility(View.GONE);
                UnfavDir_parent.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
                msg.setText(getResources().getString(R.string.note2));
                fabLayout.setVisibility(View.GONE);
                menu1.setVisibility(View.VISIBLE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        compass.arrowView =  view.findViewById(R.id.compass_layout);



                    }
                });
            } else {
                msg.setVisibility(View.VISIBLE);
                msg.setText(getResources().getString(R.string.note1));
                menu1.setVisibility(View.GONE);
                fabLayout.setVisibility(View.VISIBLE);
                text_Note.setVisibility(View.GONE);
                camp_cp.setVisibility(View.GONE);
                isCompassVisible=false;
                favDirectionState = true;
                compass_layout.setVisibility(View.GONE);
                FavDir_parent.setVisibility(View.VISIBLE);
                UnfavDir_parent.setVisibility(View.VISIBLE);
                favUnfav_Direction();
                setfabFixDirection();
            }

        }catch(Exception ex)
        {
            Log.e("Exception",""+ex.toString());
            Toast.makeText(ctx,"Opps",Toast.LENGTH_SHORT).show();
        }

        if (!fragmentResume && fragmentVisible) {   //only when first time fragment is created

            isNameValid();

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
                    if (favor.isChecked()==true)
                    {
                        favor.setChecked(false);
                    }
                    if (unfavor.isChecked()==true)
                    {
                        unfavor.setChecked(false);
                    }
                    compassView.doUnToggle(Constants.RESET);

                    if(!isCompassVisible)
                    {
                        if(favDirectionState)
                        {
                            FavDir_parent.setVisibility(View.VISIBLE);
                            UnfavDir_parent.setVisibility(View.VISIBLE);
                            favDir_lay.invalidate();
                            favDir_lay.removeAllViews();
                            favDirno_lay.invalidate();
                            favDirno_lay.removeAllViews();
                            unfavDir_lay.invalidate();
                            unfavDir_lay.removeAllViews();
                            unfavDirno_lay.invalidate();
                            unfavDirno_lay.removeAllViews();
                            favUnfav_Direction();
                        }

                    }

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


        //preparePlayers();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayerHindi = new MediaPlayer();
        mediaPlayerHindi.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayerHindi.setDataSource(Utils.getAudioHelpHindiUrl());
            mediaPlayerHindi.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayerHindi.start();
                }
            });

        }catch (Exception e){
            Log.e("Audio play",e.getMessage());
        }

        try {
            mediaPlayer.setDataSource(Utils.getAudioHelpEnglishUrl());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

        }catch (Exception e){
            Log.e("Audio play",e.getMessage());
        }

        audioHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Is the switch is on?
                boolean on = ((CheckBox) v).isChecked();
                if(on)
                {
                    try {
                        if(mediaPlayerHindi != null && mediaPlayerHindi.isPlaying()) {
                            mediaPlayerHindi.pause();
                            audioHindiHelp.setChecked(false);
                        }
                        try {
                            if (mediaPlayer != null) {
                                mediaPlayer.prepare();
                            }
                        }catch (Exception e){
                            mediaPlayer.start();
                        }
                    }catch (Exception e){
                        Log.e("Audio play",e.getMessage());
                    }
                }
                else
                {
                 if(mediaPlayer!= null && mediaPlayer.isPlaying()) {
                     mediaPlayer.pause();
                 }

                }
            }
        });

        audioHindiHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Is the switch is on?
                boolean on = ((CheckBox) v).isChecked();
                if(on)
                {
                    try {
                        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            audioHelp.setChecked(false);
                        }
                        try {
                            if (mediaPlayerHindi != null) {
                                mediaPlayerHindi.prepare();
                            }
                        }catch (Exception e){
                            mediaPlayerHindi.start();
                        }
                    }catch (Exception e){
                        Log.e("Audio play",e.getMessage());
                    }
                }
                else
                {
                    if(mediaPlayerHindi!= null && mediaPlayerHindi.isPlaying()) {
                        mediaPlayerHindi.pause();
                    }

                }
            }
        });

}catch (Throwable t){
    Log.e("CompassView",t.getMessage(),t);
}
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initilization(view);
        //preparePlayers();

    }



    private void setfabFixDirection()
    {
        try{
        //String strF = luckyChart;
        if (luckyChart != null && !luckyChart.equals("null")) {
            String[] str = Utils.chartAnalysis(luckyChart);
            fab = str[0].trim();
            unFab = str[1].trim();


            char crW = fab.charAt(0);
            char crC = fab.charAt(0);
            char crM = fab.charAt(2);
            char crE = fab.charAt(3);
            char crH = fab.charAt(1);

            mr = String.valueOf(crM);
            w = String.valueOf(crW);
            hl = String.valueOf(crH);
            e = String.valueOf(crE);
            cr = String.valueOf(crC);

            directionList.add(w);
            directionList.add(hl);
            directionList.add(mr);
            directionList.add(e);
            directionList.add(cr);


            for (int i = 0; i < directionList.size(); i++) {

                String s=Constants.containKeyDirection(directionList.get(i), Constants.directionMap);

                    DirectionTextview.get(i).setText(s);
                Log.e("Direction"," Dir "+s);
                    //arrColors.get(i).setBackgroundColor(getResources().getColor(R.color.green));

            }
        }
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        try{
        if (visible && isResumed()) {   // only at fragment screen is resumed
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;
            // initilization(view);
            isNameValid();


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
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    private void favUnfav_Direction() {
try{
        //String strF = Constants.GLOBAL_U_LUCK_CHART;
        Log.e("initCompass", luckyChart);
        if (luckyChart != null && !luckyChart.equals("null")) {
            String[] str = Utils.chartAnalysis(luckyChart);
            fab = str[0].trim();
            unFab = str[1].trim();


            char crW = fab.charAt(0);
            char crC = fab.charAt(0);
            char crM = fab.charAt(2);
            char crE = fab.charAt(3);
            char crH = fab.charAt(1);

            mr = String.valueOf(crM);
            w = String.valueOf(crW);
            hl = String.valueOf(crH);
            e = String.valueOf(crE);
            cr = String.valueOf(crC);
            Log.e("MReeeeeeeeeee", "" + crM);
            Log.e("MR", mr);
            Log.e("HL", hl);
            Log.e("W", w);
            Log.e("E", e);
            Log.e("CR", cr);
//1534
            for (int i = 0; i < 4; i++) {
                TextView textView = new TextView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                textView.setTextSize(20);
                textView.setPadding(5, 5, 5, 5);
                textView.setLayoutParams(params);

                TextView textView1 = new TextView(getActivity());
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                textView1.setTextSize(20);
                textView1.setPadding(5, 5, 5, 5);
                textView1.setLayoutParams(params1);


                int j=0;
                if (String.valueOf(fab.charAt(i)).equals("1")) {
                    textView.setText("N");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (String.valueOf(fab.charAt(i)).equals("2")) {
                    textView.setText("NE");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (String.valueOf(fab.charAt(i)).equals("3")) {
                    textView.setText("E");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (String.valueOf(fab.charAt(i)).equals("4")) {
                    textView.setText("SE");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (String.valueOf(fab.charAt(i)).equals("5")) {
                    textView.setText("S");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (String.valueOf(fab.charAt(i)).equals("6")) {
                    textView.setText("SW");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (String.valueOf(fab.charAt(i)).equals("7")) {
                    textView.setText("W");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (String.valueOf(fab.charAt(i)).equals("8")) {
                    textView.setText("NW");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.green));
                }

                favDir_lay.addView(textView);
                favDirno_lay.addView(textView1);


            }


            for (int i = 0; i < 4; i++) {
                TextView textView = new TextView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                textView.setTextSize(20);
                textView.setPadding(5, 5, 5, 5);
                textView.setLayoutParams(params);

                TextView textView1 = new TextView(getActivity());
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                textView1.setTextSize(20);
                textView1.setPadding(5, 5, 5, 5);
                textView1.setLayoutParams(params1);
int j=0;

                if (String.valueOf(unFab.charAt(i)).equals("1")) {
                    textView.setText("N");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (String.valueOf(unFab.charAt(i)).equals("2")) {
                    textView.setText("NE");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (String.valueOf(unFab.charAt(i)).equals("3")) {
                    textView.setText("E");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (String.valueOf(unFab.charAt(i)).equals("4")) {
                    textView.setText("SE");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (String.valueOf(unFab.charAt(i)).equals("5")) {
                    textView.setText("S");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (String.valueOf(unFab.charAt(i)).equals("6")) {
                    textView.setText("SW");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (String.valueOf(unFab.charAt(i)).equals("7")) {
                    textView.setText("W");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (String.valueOf(unFab.charAt(i)).equals("8")) {
                    textView.setText("NW");
                    j=i+1;
                    textView1.setText("" + j);
                    textView.setBackgroundColor(getResources().getColor(R.color.red));
                }

                unfavDir_lay.addView(textView);
                unfavDirno_lay.addView(textView1);


            }


        }
}catch (Throwable t){
    Log.e("CompassView",t.getMessage(),t);
}
    }

    public void fabFilter(String tag) {
        try{
        FavDir_parent.setVisibility(View.VISIBLE);
        UnfavDir_parent.setVisibility(View.GONE);
        favDir_lay.invalidate();
        favDir_lay.removeAllViews();

        Log.e("fabFilter  ", tag);
        Log.e("fabFilter  ", tag);

        for (int i = 0; i < 1; i++) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    80, 50, 1);
            textView.setTextSize(20);
            textView.setPadding(5, 5, 5, 5);
            textView.setLayoutParams(params);


            if (String.valueOf(tag.charAt(i)).equals("1")) {
                textView.setText("N");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (String.valueOf(tag.charAt(i)).equals("2")) {
                textView.setText("NE");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (String.valueOf(tag.charAt(i)).equals("3")) {
                textView.setText("E");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (String.valueOf(tag.charAt(i)).equals("4")) {
                textView.setText("SE");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (String.valueOf(tag.charAt(i)).equals("5")) {
                textView.setText("S");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (String.valueOf(tag.charAt(i)).equals("6")) {
                textView.setText("SW");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (String.valueOf(tag.charAt(i)).equals("7")) {
                textView.setText("W");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (String.valueOf(tag.charAt(i)).equals("8")) {
                textView.setText("NW");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            }

            favDir_lay.addView(textView);


        }
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }


    public void initilization(final View view) {
try{
        /*usr_name = (TextView) view.findViewById(R.id.tv_usrname);*/
        favor = (SwitchCompat) view.findViewById(R.id.switch_fav);
        unfavor = (SwitchCompat) view.findViewById(R.id.switch_unfav);
        favor.setOnCheckedChangeListener(this);
        unfavor.setOnCheckedChangeListener(this);

        compassView = (CompassView) view.findViewById(R.id.cmp);

        if(luckyChart != null) {
            String[] str = Utils.chartAnalysis(luckyChart);

            for (int i = 2; i < str.length; i++) {
                Log.e("Lucky No ", " " + i + "     " + str);


            }

            if (str != null) {
                strFavColor = str[2];
                strUnFavColor = str[3];
                strNeutralColor = str[4];
            }



        for (int i = 0; i < 5; i++) {
            if (String.valueOf(strFavColor.charAt(i)).equals("X")) {
                arrColors.get(i).setVisibility(View.INVISIBLE);
            } else {
                arrColors.get(i).setVisibility(View.VISIBLE);
                arrColors.get(i).setText(Constants.containKeyColor(String.valueOf(strFavColor.charAt(i)), Constants.colorMap));
                arrColors.get(i).setBackgroundColor(getResources().getColor(R.color.green));
            }
        }

        for (int i = 0; i < 5; i++) {
            if (String.valueOf(strUnFavColor.charAt(i)).equals("X")) {
                arrUColors.get(i).setVisibility(View.INVISIBLE);
            } else {
                arrUColors.get(i).setVisibility(View.VISIBLE);
                arrUColors.get(i).setText(Constants.containKeyColor(String.valueOf(strUnFavColor.charAt(i)), Constants.colorMap));
                arrUColors.get(i).setBackgroundColor(getResources().getColor(R.color.red));
            }
        }

        for (int i = 0; i < 5; i++) {
            if (String.valueOf(strNeutralColor.charAt(i)).equals("X")) {
                arrNColors.get(i).setVisibility(View.INVISIBLE);
            } else {
                arrNColors.get(i).setVisibility(View.VISIBLE);
                arrNColors.get(i).setText(Constants.containKeyColor(String.valueOf(strNeutralColor.charAt(i)), Constants.colorMap));
                arrNColors.get(i).setBackgroundColor(getResources().getColor(R.color.brown));
            }
        }
        }
}catch (Throwable t){
    Log.e("CompassView",t.getMessage(),t);
}
    }

    public boolean isNameValid() {

        boolean status = true;
        /*try{

                if (!Constants.GLOBAL_USER_NAME.equals("Guest")) {
                    usr_name.setText("Welcome, " + Constants.GLOBAL_USER_NAME);
                    usr_name.setTextColor(getResources().getColor(R.color.green));
                    status = true;
                } else {
                    usr_name.setText("Guest");
                    status = false;
                }
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }*/
        return status;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        try{
        compass.start();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop compass");
        try{
        compass.stop();
            stopPlayers();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause compass");
        try{
        compass.stop();
            //stopPlayers();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    void stopPlayers(){
        try{
            if(mediaPlayerHindi != null){
                mediaPlayerHindi.pause();
            }
            if(mediaPlayer != null){
                mediaPlayer.pause();
            }
        }catch(Exception e){

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume compass");
        try{
        compass.start();
        //initilization(view);
        //fabInit(view);
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try{
        int id = buttonView.getId();
        if (id == R.id.switch_fav && isChecked) {

            compassView.doToggle(Constants.FAV);
            if (favDirectionState) {
                FavDir_parent.setVisibility(View.VISIBLE);
            }


            Log.e("CompassFrag", " fav...." + isChecked);
        } else if (id == R.id.switch_fav && !isChecked) {
            compassView.doUnToggle(Constants.FAV);
            Log.e("CompassFrag", " Unfav...." + isChecked);

            FavDir_parent.setVisibility(View.GONE);

        }
        if (id == R.id.switch_unfav && isChecked) {
            compassView.doToggle(Constants.UNFAV);
            if (favDirectionState) {
                UnfavDir_parent.setVisibility(View.VISIBLE);
            }


            Log.e("CompassFrag", " Unfav...." + isChecked);

        } else if (id == R.id.switch_unfav && !isChecked) {
            compassView.doUnToggle(Constants.UNFAV);
            Log.e("CompassFrag", " Unfav...." + isChecked);
            UnfavDir_parent.setVisibility(View.GONE);

        }

        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    @Override
    public void onClick(View v) {
try{
        if (v == health) {

            menu1.close(true);
            compassView.doToggle(Constants.HEALTH);
            if (favDirectionState) {
                fabFilter(hl);
            }


        } else if (v == wealth) {

            menu1.close(true);
            compassView.doToggle(Constants.WEALTH);
            if (favDirectionState) {
                fabFilter(w);
            }


        } else if (v == edu) {

            menu1.close(true);
            compassView.doToggle(Constants.EDUCATION);
            if (favDirectionState) {
                fabFilter(e);
            }

            // }
        } else if (v == career) {

            menu1.close(true);
            compassView.doToggle(Constants.CAREER);
            if (favDirectionState) {
                fabFilter(cr);
            }

            //  }
        } else if (v == marriage) {

            menu1.close(true);
            compassView.doToggle(Constants.MR);
            if (favDirectionState) {
                fabFilter(mr);
            }


        }
}catch (Throwable t){
    Log.e("CompassView",t.getMessage(),t);
}

    }


    private void loadLuckyChart(){
        try{
        String luckyChat =  SharedPreferenceManager.getSharedInstance().getStringFromPreferances("LUCKY_CHAT");
        if(luckyChat == null || luckyChat.isEmpty()){

            DataController  dbController = DataController.getsInstance(getActivity());
            List<Userdata_Bean> userData = dbController.getAllData();
            if (userData != null && !userData.isEmpty()) {



                String parameters = "apikey="+Constants.LUCKY_CHAT_APIKEY;

                if (userData.get(0).getGender().equalsIgnoreCase("200001")) {
                    parameters = parameters + "&gender="+"male" ;
                } else {
                    parameters = parameters + "&gender="+"female" ;
                }
                parameters = parameters + "&dob=" + userData.get(0).getDob().substring(0,10);
                GsonRequestPost<LuckyChartResponse> myReq = new GsonRequestPost<>(
                        Request.Method.POST, Constants.LUCKY_CHAT_URL+"?"+parameters, LuckyChartResponse.class, null,
                        successListener(), errorListener(), new JsonObject());
                //
                Log.d(TAG,"Lucky Chart request data " + parameters);
                int socketTimeout = 50000;//50 seconds
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                myReq.setRetryPolicy(policy);
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "compassReq");
            }
        }else{
            this.luckyChart = luckyChat;
            initilization(view);
        }
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    private Response.Listener<LuckyChartResponse>  successListener(){
        return new Response.Listener<LuckyChartResponse>() {
            @Override
            public void onResponse(LuckyChartResponse response) {
                Log.e(TAG, "Lucky Response");
                try{
               String luckyNumber =  response.getLuckyNumber();
                luckyChart =  response.getLuckyChart();
                if(luckyNumber==null || luckyNumber.isEmpty() || luckyChart == null || luckyChart.isEmpty()) {
                    return;
                }
                String luckyChat =  SharedPreferenceManager.getSharedInstance().getStringFromPreferances("LUCKY_CHAT");
                    if(luckyChat==null || luckyChat.isEmpty()) {
                        SharedPreferenceManager.getSharedInstance().setStringInPreferences("LUCKY_CHAT", luckyChart);
                        Constants.GLOBAL_U_LUCK_CHART = luckyChart;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                initilization(view);

                            }
                        });
                    }
            }catch (Throwable t){
                Log.e("CompassView",t.getMessage(),t);
            }
            }


        };
    }

    private Response.ErrorListener errorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

Log.e(TAG, error.getMessage());
            }
        };
    }
}
