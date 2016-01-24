package com.cgp.saral.network;


import android.content.Context;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Akshay on 8/27/15.
 *
 * Picasso library to load images from the network
 * Picasso has L1 (memory) and L2 (disk) caching
 * ensuring minimal use to network connection resource
 */
public class PicassoSingleton {

        /**
         * Static Picasso Instance
         */
        private static Picasso picassoInstance = null;

        /**
         * PicassoCache Constructor
         *
         * @param context application Context
         */
        private PicassoSingleton (Context context) {

            //Providing OKHTTP as the http client. as this has the disk cache (L2)
            //Picasso does not have L2 of its own, it depends on the client
            Downloader downloader   = new OkHttpDownloader(context, 10*1024*1024);
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(downloader);

            picassoInstance = builder.build();
        }

        /**
         * Get Singleton Picasso Instance
         *
         * @param context application Context
         * @return Picasso instance
         */
        public static Picasso getPicassoInstance (Context context) {

            if (picassoInstance == null) {

                new PicassoSingleton(context);
                return picassoInstance;
            }

            return picassoInstance;
        }


}
