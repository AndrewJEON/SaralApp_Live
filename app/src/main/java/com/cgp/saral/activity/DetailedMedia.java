package com.cgp.saral.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgp.saral.R;
import com.cgp.saral.network.PicassoSingleton;

/**
 * Created by WeeSync on 30/09/15.
 */
public class DetailedMedia extends Activity{


    protected void onCreate(Bundle savedInstanceState) {
       // requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed_media);

        //changing the status bar color
      /*  Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);*/
        //API available only for lollipop and above



       // toolbar= (Toolbar) findViewById(R.id.toolbar);
       //setSupportActionBar(toolbar);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        String imageUrl = null;


        Bundle imageInfo = this.getIntent().getExtras();

        if(imageInfo != null) {

            imageUrl = imageInfo.getString("ImageUrl");
            Log.e("Detailed Image",""+imageUrl);


        }

        ImageView imageView = (ImageView)findViewById(R.id.detailed_image);


        //Picasso library to display the image in detail
        //Why? As Picasso has L1 and L2 caching so avoids duplicate loading of the same resource
        //resource fetched from cache if hit
        PicassoSingleton.getPicassoInstance(this).load(imageUrl).into(imageView);

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_media, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
