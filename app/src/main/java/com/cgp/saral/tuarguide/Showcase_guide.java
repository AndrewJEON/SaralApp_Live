package com.cgp.saral.tuarguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cgp.saral.R;
import com.cgp.saral.activity.MainActivity;

import com.cgp.saral.myutils.Constants;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class Showcase_guide extends AppCompatActivity {
    public TourGuide mTutorialHandler, mTutorialHandler2;
    public Activity mActivity;
    Button tab1,tab2,tab3,tab4,navigation;
    ImageView tabimage,naviagtionimage;

    ImageButton btnfloat;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase_guide);
        preferences=getSharedPreferences(Constants.PREF_TOURGUIDE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        editor.putBoolean(Constants.PREFS_TOURGUIDE_KEY, true);
        editor.commit();

        Intent in= getIntent();
        b= in.getExtras();
       // b.putSerializable("user", bean);

        tab1= (Button) findViewById(R.id.home);
        tab2= (Button) findViewById(R.id.you);
 //       tab2.setBackgroundResource(R.drawable.you);
        tab3= (Button) findViewById(R.id.chat);
        tab4= (Button) findViewById(R.id.invite);
 //       tab3.setBackgroundResource(R.drawable.chat);
        naviagtionimage=(ImageView)findViewById(R.id.ivv);
        tabimage=(ImageView)findViewById(R.id.ivvtab);
        navigation= (Button) findViewById(R.id.title2);
        btnfloat= (ImageButton) findViewById(R.id.btnfloat);


        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

   /* initialize TourGuide without playOn() */
        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                                .setTitle("Menu")
                                .setDescription("App menu")
                                .setGravity(Gravity.CENTER)
                )
                .setOverlay(new Overlay()
                                .setEnterAnimation(enterAnimation)
                                .setExitAnimation(exitAnimation)
                );

        /* setup 1st button, when clicked, cleanUp() and re-run TourGuide on button2 */
        navigation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.setToolTip(new ToolTip()
                        .setTitle(" Vaastu Feeds ")
                        .setDescription(" Vaastu feeds for all the categories ")
                        .setGravity(Gravity.BOTTOM|Gravity.RIGHT))
                        .playOn(tab1);

                naviagtionimage.setVisibility(View.INVISIBLE);
                btnfloat.setVisibility(View.INVISIBLE);
                tabimage.setVisibility(View.VISIBLE);
                tabimage.setImageResource(R.drawable.s11);
            }
        });
        tab1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.setToolTip(new ToolTip()
                        .setTitle("You")
                        .setDescription("Your personalized Favorable and Unfavorable directions/colors")
                        .setGravity(Gravity.BOTTOM|Gravity.LEFT))
                        .playOn(tab2);

                naviagtionimage.setVisibility(View.INVISIBLE);
                tabimage.setVisibility(View.VISIBLE);
                tabimage.setImageResource(R.drawable.s2);
            }
        });

        /* setup 2nd button, when clicked, cleanUp() and re-run TourGuide on button3 */
        tab2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.setToolTip(new ToolTip()
                        .setTitle("Chat")
                        .setDescription("Chat with Saral Vaastu")
                        .setGravity(Gravity.BOTTOM|Gravity.LEFT))
                        .playOn(tab3);

                naviagtionimage.setVisibility(View.INVISIBLE);
                btnfloat.setVisibility(View.INVISIBLE);
                tabimage.setVisibility(View.VISIBLE);
                tabimage.setImageResource(R.drawable.s1);

            }
        });
        tab3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.setToolTip(new ToolTip()
                        .setTitle("Invite")
                        .setDescription("Invite your friends")
                        .setGravity(Gravity.BOTTOM|Gravity.LEFT))
                        .playOn(tab4);

                naviagtionimage.setVisibility(View.INVISIBLE);
                tabimage.setVisibility(View.VISIBLE);
                tabimage.setImageResource(R.drawable.s3);
                btnfloat.setVisibility(View.INVISIBLE);
            }
        });
        tab4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.setToolTip(new ToolTip()
                        .setTitle("Filter your choice")
                        .setDescription("Filter as per your category of choice")
                        .setGravity(Gravity.TOP|Gravity.LEFT))
                        .playOn(btnfloat);

                naviagtionimage.setVisibility(View.INVISIBLE);
                btnfloat.setVisibility(View.VISIBLE);
                tabimage.setVisibility(View.VISIBLE);
                tabimage.setImageResource(R.drawable.s4);

            }
        });

        /* setup 3rd button, when clicked, run cleanUp() */
        btnfloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();

                naviagtionimage.setVisibility(View.VISIBLE);
                tabimage.setVisibility(View.INVISIBLE);
                btnfloat.setVisibility(View.INVISIBLE);
                startActivity(new Intent(Showcase_guide.this, MainActivity.class).putExtras(b));
                finish();
            }
        });

        mTutorialHandler.playOn(navigation);
    }



}
