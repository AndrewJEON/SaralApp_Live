package com.cgp.saral.fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
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

import com.cgp.saral.R;
import com.cgp.saral.customviews.Compass;
import com.cgp.saral.customviews.CompassView;
import com.cgp.saral.fab.FloatingActionButton;
import com.cgp.saral.fab.FloatingActionMenu;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompassFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    View view;
    TextView usr_name;
    SwitchCompat favor, unfavor;
    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String TAG = "CompassFragment";
    /*for fab*/
    private List<FloatingActionMenu> menus = new ArrayList<FloatingActionMenu>();
    private Handler mUiHandler = new Handler();
    static CompassView compassView;
    // MyCompassView compassView;


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


    ArrayList<TextView> arrColors = new ArrayList<>();
    ArrayList<TextView> arrUColors = new ArrayList<>();
    ArrayList<TextView> arrNColors = new ArrayList<>();

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

        view = inflater.inflate(R.layout.fragment_campass, container, false);
        ButterKnife.bind(this, view);


        ctx = getActivity();
        try {


            mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mGyroSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            PackageManager PM = getActivity().getPackageManager();
            boolean accel = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
            boolean comp = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
            boolean gyro = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

            Log.e("Compass ",""+" Accel "+accel+"  Compass "+comp+" Gyro "+gyro);
            //Toast.makeText(getActivity()," Accel "+accel+"  Compass "+comp+" Gyro "+gyro,Toast.LENGTH_LONG).show();
            compass = new Compass(getActivity());
            Constants.initColorMap();
            if (comp) {
                isCompassVisible =true;
                favDirectionState = false;
                text_Note.setVisibility(View.VISIBLE);
                camp_cp.setVisibility(View.VISIBLE);
                compass_layout.setVisibility(View.VISIBLE);
                FavDir_parent.setVisibility(View.GONE);
                UnfavDir_parent.setVisibility(View.GONE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        compass.arrowView = (ImageView) view.findViewById(R.id.cmp_image);



                    }
                });
            } else {
                text_Note.setVisibility(View.GONE);
                camp_cp.setVisibility(View.GONE);
                isCompassVisible=false;
                favDirectionState = true;
                compass_layout.setVisibility(View.GONE);
                FavDir_parent.setVisibility(View.VISIBLE);
                UnfavDir_parent.setVisibility(View.VISIBLE);
                favUnfav_Direction();
            }

        }catch(Exception ex)
        {
            Log.e("Exception",""+ex.toString());
            Toast.makeText(ctx,"Opps",Toast.LENGTH_SHORT).show();
        }

        String[] str = Utils.chartAnalysis(Constants.GLOBAL_U_LUCK_CHART);

        for (int i = 2; i < str.length; i++) {
            Log.e("Lucky No ", " " + i + "     " + str);


        }

        if (str != null) {
            strFavColor = str[2];
            strUnFavColor = str[3];
            strNeutralColor = str[4];
        }
        if (!fragmentResume && fragmentVisible) {   //only when first time fragment is created

            isNameValid();

        }

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


        final String url = "http://appapi.saralvaastu.com/guideforyoupage.mp3"; // your URL here
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();// might take long! (for buffering, etc)

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
                        mediaPlayer.start();
                    }catch (Exception e){
                        Log.e("Audio play",e.getMessage());
                    }
                }
                else
                {
                 if(mediaPlayer.isPlaying()) {
                     mediaPlayer.pause();
                 }

                }
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initilization(view);


    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
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
    }

    private void favUnfav_Direction() {

        String strF = Constants.GLOBAL_U_LUCK_CHART;
        Log.e("initCompass", strF);
        if (!strF.equals("null")) {
            String[] str = Utils.chartAnalysis(strF);
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
                    textView1.setText("" + i);
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
    }

    public void fabFilter(String tag) {
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
                textView.setText("ES");

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
                textView.setText("WN");

                textView.setBackgroundColor(getResources().getColor(R.color.green));
            }

            favDir_lay.addView(textView);


        }

    }


    public void initilization(final View view) {
        usr_name = (TextView) view.findViewById(R.id.tv_usrname);
        favor = (SwitchCompat) view.findViewById(R.id.switch_fav);
        unfavor = (SwitchCompat) view.findViewById(R.id.switch_unfav);
        favor.setOnCheckedChangeListener(this);
        unfavor.setOnCheckedChangeListener(this);

        compassView = (CompassView) view.findViewById(R.id.cmp);


        for (int i = 0; i < 5; i++) {
            if (String.valueOf(strFavColor.charAt(i)).equals("X")) {
                arrColors.get(i).setVisibility(View.INVISIBLE);
            } else {
                arrColors.get(i).setText(Constants.containKeyColor(String.valueOf(strFavColor.charAt(i)), Constants.colorMap));
                arrColors.get(i).setBackgroundColor(getResources().getColor(R.color.green));
            }
        }

        for (int i = 0; i < 5; i++) {
            if (String.valueOf(strUnFavColor.charAt(i)).equals("X")) {
                arrUColors.get(i).setVisibility(View.INVISIBLE);
            } else {
                arrUColors.get(i).setText(Constants.containKeyColor(String.valueOf(strUnFavColor.charAt(i)), Constants.colorMap));
                arrUColors.get(i).setBackgroundColor(getResources().getColor(R.color.red));
            }
        }

        for (int i = 0; i < 5; i++) {
            if (String.valueOf(strNeutralColor.charAt(i)).equals("X")) {
                arrNColors.get(i).setVisibility(View.INVISIBLE);
            } else {
                arrNColors.get(i).setText(Constants.containKeyColor(String.valueOf(strNeutralColor.charAt(i)), Constants.colorMap));
                arrNColors.get(i).setBackgroundColor(getResources().getColor(R.color.brown));
            }
        }


    }

    public boolean isNameValid() {
        boolean status = false;


        if (!Constants.GLOBAL_USER_NAME.equals("Guest")) {
            usr_name.setText("Welcome, " + Constants.GLOBAL_USER_NAME);
            usr_name.setTextColor(getResources().getColor(R.color.green));
            status = true;
        } else {
            usr_name.setText("Guest");
            status = false;
        }

        return status;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop compass");
        compass.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause compass");
        compass.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume compass");
        compass.start();
        //initilization(view);
        //fabInit(view);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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


    }

    @Override
    public void onClick(View v) {

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


    }


}
