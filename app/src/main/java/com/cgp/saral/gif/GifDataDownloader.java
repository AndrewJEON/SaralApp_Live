package com.cgp.saral.gif;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;

public class GifDataDownloader extends AsyncTask<String, Void, byte[]> {

    private static final String TAG = "GifDataDownloader";

    Context ctx;
    public GifDataDownloader(Context ctx) {
        this.ctx= ctx;
    }

    @Override
    protected byte[] doInBackground(final String... params) {
        final String gifUrl = params[0];

        if (gifUrl == null)
            return null;

        byte[] gif = null;
        try {


            AssetManager assetManager = ctx.getAssets();
            InputStream inputStream = null;

            //gif = ByteArrayHttpClient.get(gifUrl);
            //src/main/assets/

            if(assetManager !=null) {

               /* try {
                    *//*InputStream is = assetManager.open("compass.gif");
                    gif = new byte[is.available()];
                    is.read(gif);
                    is.close();*//*
                } catch (IOException ex) {
                    Log.e("file IO Exception", "" + ex.toString());
                }*/
            }else
            {
                Toast.makeText(ctx, "No Assets", Toast.LENGTH_LONG).show();
            }
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "GifDecode OOM: " + gifUrl, e);
        }

        return gif;
    }
}
