package com.cgp.saral.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cgp.saral.R;
import com.cgp.saral.activity.DetailedMedia;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.Content_Action;
import com.cgp.saral.model.Datum;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.PicassoSingleton;
import com.cgp.saral.network.VolleySingleton;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by karamjeetsingh on 04/09/15.
 */
public class HomeTab_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    //private List<ContactInfo> contactList;

   // private static final int VIEW_TYPE_DEFAULT = 1;
   // private static final int VIEW_TYPE_IMAGE = 2;
   // private static final int VIEW_TYPE_VIDEO = 3;

    ArrayList<Datum> list;
    FeedFilters mFeedFilter;

    ArrayList<Datum> originalData;
    ProgressBar progressBar;
    // private final ArrayList<Integer> likedPositions = new ArrayList<>();

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private final Map<Integer, Integer> disLikesCount = new HashMap<>();
    Activity context;
    String strURL;
    //int likeCount, disLikeCount;
    DataController dbController;
    //String strContentId = "";


    String strLiked = "false";
    String strUnLiked = "false";
    boolean youLiked = false;
    boolean youDisliked = false;

    boolean isClicked = false;

    private int mSelectedPosition = 0;

    int likeCount;
    int disLikeCount;

    private final ArrayList<Integer> likedPositions = new ArrayList<>();

    private final ArrayList<Integer> unLikedPositions = new ArrayList<>();

    ArrayList<Content_Action> contents = new ArrayList<>();

    ArrayList<String> itemSepColors = new ArrayList<String>();

    public HomeTab_Adapter(Activity ctx, ArrayList<Datum> data, DataController dbController) {
        this.list = data;
        this.context = ctx;
        this.originalData = data;
        this.dbController = dbController;
        itemSepColors.add("#ff2189df");
        itemSepColors.add("#ff009682");
        itemSepColors.add("#ffc52c18");
        itemSepColors.add("#ffdb8d11");
    }

    @Override
    public HomeHolderView onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.fragment_hometab_adapter, viewGroup, false);
        final HomeHolderView holderView = new HomeHolderView(view);


        contents = dbController.contentActionList();

        return holderView;

    }

   /* @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_IMAGE | VIEW_TYPE_VIDEO;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hView, int i) {

        final HomeHolderView holdview = (HomeHolderView) hView;

       /* if (getItemViewType(i) == VIEW_TYPE_DEFAULT) {
            doProcessDataBinding(i, holdview);
        } else if (getItemViewType(i) == VIEW_TYPE_IMAGE) {
            doProcessDataBinding(i, holdview);
        }else if(getItemViewType(i) == VIEW_TYPE_VIDEO)
        {

        }*/
        doProcessDataBinding(i, holdview);



    }


    private void doProcessDataBinding(int i, final HomeHolderView holdview) {
        final Datum d = list.get(i);

        holdview.listItemSeperator.setBackgroundColor(Color.parseColor(itemSepColors.get(i%4)));
        holdview.timestamp.setTextColor(Color.parseColor(itemSepColors.get(i%4)));

        holdview.title.setText(d.getSummary());







        // holdview.description.setText(d.getDescription());


        final String strImageThumbNail = "http://" + d.getContentDetails().get(0).getThumbnailPath();

        // Log.e("Thumbnail Image Path ", "--->" + strImageThumbNail);
        String strMediaType = d.getContentDetails().get(0).getMediaType();
        Log.e("Post Data",""+strMediaType+" "+d.getDescription());

        holdview.description.setMovementMethod(LinkMovementMethod.getInstance());
        if (strMediaType.equals("700004")) {
            //String strData=removeChar(d.getDescription(),"'");
            String [] str=d.getDescription().split("'");
            StringBuilder out = new StringBuilder();
            if(str !=null) {
                for (int ii = 0; ii < str.length; ii++) {
                    out.append(str[ii]);
                }
                Log.e(" Link",""+out.toString());
                holdview.description.setText(Html.fromHtml(out.toString()));
            }else
            {
                holdview.description.setText(Html.fromHtml(d.getDescription()));
            }

        }else
        {
            holdview.description.setText(Html.fromHtml(d.getDescription()));
        }

        likeCount = Integer.valueOf(d.getContentStats().getLikes());
        disLikeCount = Integer.valueOf(d.getContentStats().getDisLikes());




        if (!likesCount.containsKey(holdview.getAdapterPosition())) {

            likesCount.put(holdview.getAdapterPosition(), likeCount);

            // fillLikesValues(holdview, d);
        }
        if(likesCount.containsKey(holdview.getAdapterPosition()))
        {

            holdview.likeTv.getTag();
            holdview.likeTv.setText("" + likesCount.get(holdview.getAdapterPosition()));
        }

        if (!disLikesCount.containsKey(holdview.getAdapterPosition())) {

            disLikesCount.put(holdview.getAdapterPosition(), disLikeCount);

            // fillLikesValues(holdview, d);
        }
        if(disLikesCount.containsKey(holdview.getAdapterPosition()))
        {

            holdview.dislikeTv.getTag();
            holdview.dislikeTv.setText("" + disLikesCount.get(holdview.getAdapterPosition()));
        }


        try {

            long refTime = new Date(d.getApprovalDate()).getTime();

            holdview.timestamp.setReferenceTime(refTime);
        } catch (Exception ex) {
            // Log.e("Exception in Date Parse", "" + d.getApprovalDate());
        }

        if (strMediaType.equals("700003")) {

           // HomeHolderView holder = (HomeHolderView) holdview.getTag();
            holdview.mediaLayout.setVisibility(View.VISIBLE);

            if (!strImageThumbNail.isEmpty()) {

                final String strImagePath="http://" + d.getContentDetails().get(0).getMediaPath();
                holdview.list_icon.setVisibility(View.VISIBLE);


                holdview.list_icon.setImageBitmap(null);
                PicassoSingleton
                        .getPicassoInstance(context).cancelRequest(holdview.list_icon);

                PicassoSingleton
                        .getPicassoInstance(context)
                        .load(strImagePath).placeholder(R.drawable.list_item).error(R.drawable.list_item)
                        .into(holdview.list_icon);

                holdview.list_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, DetailedMedia.class);
                        Bundle b = new Bundle();
                        b.putString("ImageUrl", strImagePath);
                        intent.putExtras(b);
                        context.startActivity(intent);


                        Log.e("Image Click", "" + strImagePath);

                    }
                });
            } else {
                holdview.list_icon.setVisibility(View.GONE);

            }
        } else if (strMediaType.equals("700002")) {
            holdview.mediaLayout.setVisibility(View.VISIBLE);
            if (!strImageThumbNail.isEmpty()) {
                //String strVideoThumbnail = d.getContentDetails().get(0).getThumbnailPath();
                holdview.list_icon.setVisibility(View.VISIBLE);
                holdview.list_icon.setImageBitmap(null);
                PicassoSingleton
                        .getPicassoInstance(context).cancelRequest(holdview.list_icon);

               // if (!strVideoThumbnail.isEmpty()) {
                    PicassoSingleton
                            .getPicassoInstance(context)
                            .load(R.drawable.list_item).placeholder(R.drawable.list_item).error(R.drawable.list_item)
                            .into(holdview.list_icon);
               // }

                strURL = d.getContentDetails().get(0).getMediaPath();

                holdview.videoView.loadUrl("http://www.youtube.com/embed/" + strURL + "?autoplay=1&vq=small&showinfo=0");
                holdview.videoView.getSettings().setJavaScriptEnabled(true);
                holdview.videoView.setVisibility(View.VISIBLE);
                holdview.videoView.getSettings().setPluginState(WebSettings.PluginState.ON);; //sets MediaController in the video view

                holdview.videoView.requestFocus();//give focus to a specific view
                holdview.videoView.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        holdview.list_icon.setVisibility(View.GONE);
                    }
                });


                /*String strVideoThumbnail = d.getContentDetails().get(0).getThumbnailPath();
                //  holdview.youtube_player.setVisibility(View.VISIBLE);
                strURL = d.getContentDetails().get(0).getMediaPath();
                //Log.e("Video Data URL", "" + strURL);

                holdview.list_icon.setVisibility(View.VISIBLE);
                holdview.list_icon.setImageBitmap(null);
                PicassoSingleton
                        .getPicassoInstance(context).cancelRequest(holdview.list_icon);

                if (!strVideoThumbnail.isEmpty()) {
                    PicassoSingleton
                            .getPicassoInstance(context)
                            .load(strVideoThumbnail).placeholder(R.drawable.list_item).error(R.drawable.list_item)
                            .into(holdview.list_icon);
                }

                holdview.list_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holdview.list_icon.setVisibility(View.GONE);
                        holdview.progressBar.setVisibility(View.VISIBLE);

                        String video_path = "http://www.youtube.com/watch?v=" + strURL;
                        Uri uri = Uri.parse(video_path);

                        // With this line the Youtube application, if installed, will launch immediately.
                        // Without it you will be prompted with a list of the application to choose.
                        uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));

*//*
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
*//*
                        holdview.videoView.loadUrl("http://www.youtube.com/embed/" + strURL + "?autoplay=1&vq=small");
                        holdview.videoView.getSettings().setJavaScriptEnabled(true);
                        holdview.videoView.setVisibility(View.VISIBLE);
                        holdview.videoView.getSettings().setPluginState(WebSettings.PluginState.ON);; //sets MediaController in the video view

                        holdview.videoView.requestFocus();//give focus to a specific view
                        holdview.videoView.setWebChromeClient(new WebChromeClient());
                        holdview.progressBar.setVisibility(View.GONE);


                    }
                });*/


            } else {
                // holdview.youtube_player.setVisibility(View.GONE);
                //  Log.e("No Image Data ", "--->" + strImageThumbNail);
            }
        } else {
            holdview.mediaLayout.setVisibility(View.GONE);
        }

       // holdview.likeTv.setTag(i);
       // holdview.dislikeTv.setTag(i);
        holdview.like.setTag(holdview);
        holdview.unlike.setTag(holdview);
        holdview.list_icon.setTag(holdview);
       // doProcessDataBinding(i, holdview);
        doProcessDataBindingClick(d, holdview);

    }

    private void doProcessDataBindingClick(final Datum d, final HomeHolderView holdview) {

        String strLiked = d.getYouLiked();
        String strUnLiked = d.getYouDisliked();
        Log.e("StrLiked", "" + strLiked + "  unliked -->" + strUnLiked);

        if (strLiked.equals("false") && strUnLiked.equals("false")) {

            isClicked = false;


            holdview.like.setEnabled(true);
            holdview.like.setBackgroundResource(R.drawable.ic_like_grey);
            holdview.unlike.setEnabled(true);
            holdview.unlike.setBackgroundResource(R.drawable.ic_dislike_grey);
           // notifyItemChanged(holdview.getAdapterPosition());

            Log.e("Content Action"," No Action");

        }
        if (strLiked.equals("true")&&strUnLiked.equals("false")) {


          //  HomeHolderView holderView=
            int contentId = Integer.parseInt(d.getId());

            if (!likedPositions.contains(contentId)) {

                likedPositions.add(contentId);

               // fillLikesValues(holdview, d);
            }
            if(likedPositions.contains(contentId)&& likesCount.containsKey(holdview.getAdapterPosition()))
            {

                fillLikesValues(holdview, d);
            }
          //  Log.e("Content Action", " Liked");
            // holdview.like.setEnabled(false);
            // holdview.unlike.setEnabled(false);
        }else
        {
            holdview.like.setBackgroundResource(R.drawable.ic_like_grey);

        }
        if (strLiked.equals("false")&&strUnLiked.equals("true")) {
            int contentId = Integer.parseInt(d.getId());

            if (!unLikedPositions.contains(contentId)) {

                unLikedPositions.add(contentId);


            }
            if(unLikedPositions.contains(contentId)&& disLikesCount.containsKey(holdview.getAdapterPosition()))
            {
                fillDisLikesValues(holdview, d);
            }
           // Log.e("Content Action", " Un Liked");


        } else {
              holdview.unlike.setBackgroundResource(R.drawable.ic_dislike_grey);
        }

       // holdview.like.getTag();

        holdview.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                long mLastClickTime = 0;


                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }


                // holdview.like.getTag();
                mLastClickTime = SystemClock.elapsedRealtime();
                int contentId = Integer.parseInt(d.getId());

                if (!likedPositions.contains(contentId)) {

                    likedPositions.add(contentId);
                    Log.e("Content Action", " Like The Content");
                    updateLikesCounter(holdview,d);


                } else {

                    holdview.like.setBackgroundResource(R.drawable.ic_like_b);
                    Toast.makeText(context, "Already Liked", Toast.LENGTH_SHORT).show();

                }
            }
        });

       // holdview.unlike.getTag();
        holdview.unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long mLastClickTime = 0;

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //  holdview.unlike.getTag();

                int contentId = Integer.parseInt(d.getId());

                if (!unLikedPositions.contains(contentId)) {
                    unLikedPositions.add(contentId);
                    updateDisLikesCounter(holdview, d);
                    Log.e("Content Action", " Unlike The Content");


                } else {

                    holdview.unlike.setBackgroundResource(R.drawable.ic_dislike_b);
                    Toast.makeText(context, "Already Un Liked", Toast.LENGTH_SHORT).show();
                }

              /*  if (!isClicked && d.getYouLiked().equals("false")) {
                    holdview.unlike.setEnabled(false);
                    holdview.like.setEnabled(false);

                    String strU = String.valueOf(disLikeCount + 1);
                    holdview.unlike.setBackgroundResource(R.drawable.ic_dislike_b);

                    doPostAction(strContentId, "2");
                    holdview.dislikeTv.getTag();
                    holdview.dislikeTv.setText(strU);
                    Content_Action action = new Content_Action();
                    action.setStrContentId(strContentId);
                    action.setStrLiked("0");
                    action.setStrDisliked("1");

                    dbController.contentAction(action);
                    isClicked = true;
                } else {
                    Toast.makeText(context, "Already Un Liked", Toast.LENGTH_SHORT).show();
                }*/
            }

        });

    }


    private void fillLikesValues(HomeHolderView holdview, Datum d) {

       // notifyItemChanged(holdview.getAdapterPosition());

       // HomeHolderView view= holdview.
        Log.e(" Liked Data Item","-->"+d.getYouLiked() +"  -->"+d.getSummary());
        holdview.like.setBackgroundResource(R.drawable.ic_like_b);
        holdview.like.setEnabled(false);
        holdview.unlike.setEnabled(false);

    }

    private void fillDisLikesValues(HomeHolderView holdview, Datum d) {

        //notifyItemChanged(holdview.getAdapterPosition());
        Log.e(" Un Liked Data Item","-->"+d.getYouDisliked() +"  -->"+d.getSummary());
        holdview.unlike.setBackgroundResource(R.drawable.ic_dislike_b);
        holdview.like.setEnabled(false);
        holdview.unlike.setEnabled(false);

    }

    private void updateLikesCounter(HomeHolderView holder, Datum d) {
       // int currentLikesCount = Integer.parseInt(d.getContentStats().getLikes());

        int currentLikesCount = likesCount.get(holder.getAdapterPosition());
        String strContentId = d.getContentDetails().get(0).getContentId();

        holder.like.setEnabled(false);
        holder.unlike.setEnabled(false);

        doPostAction(strContentId, "1");
        currentLikesCount=currentLikesCount+1;
        String strL = String.valueOf(currentLikesCount);

       // holder.likeTv.getTag();
        holder.likeTv.setText(strL);
        holder.like.setBackgroundResource(R.drawable.ic_like_b);
        Content_Action action = new Content_Action();
        action.setStrContentId(strContentId);
        action.setStrLiked("1");
        action.setStrDisliked("0");
        int contentId = Integer.parseInt(d.getId());
      //  notifyItemChanged(holder.getAdapterPosition());

        // dbController.contentAction(action);
        //isClicked = true;

        //  holder.likeTv.setText(likesCountText);


        likesCount.put(holder.getAdapterPosition(), currentLikesCount);
    }

    private void updateDisLikesCounter(HomeHolderView holder, Datum d) {
        int currentDisLikesCount = disLikesCount.get(holder.getAdapterPosition());
     //  int currentDisLikesCount = disLikesCount.get(holder.getAdapterPosition()) + 1;
       // int currentDisLikesCount = Integer.parseInt(d.getContentStats().getDisLikes());

        String strContentId = d.getContentDetails().get(0).getContentId();
        holder.like.setEnabled(false);
        holder.unlike.setEnabled(false);

        doPostAction(strContentId, "2");
        currentDisLikesCount=currentDisLikesCount+1;
        String strL = String.valueOf(currentDisLikesCount);

        //holder.unlike.getTag();
        holder.dislikeTv.setText(strL);
        holder.unlike.setBackgroundResource(R.drawable.ic_dislike_b);
        Content_Action action = new Content_Action();
        action.setStrContentId(strContentId);
        action.setStrLiked("0");
        action.setStrDisliked("1");

        int contentId = Integer.parseInt(d.getId());

      //  dbController.contentAction(action);


        //  holder.likeTv.setText(likesCountText);

      //  notifyItemChanged(holder.getAdapterPosition());
        disLikesCount.put(holder.getAdapterPosition(), currentDisLikesCount);
    }

   /* @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.iv_like) {
            HomeHolderView holder = (HomeHolderView) view.getTag();
            if (!likedPositions.contains(holder.getPosition())) {
                likedPositions.add(holder.getPosition());
                updateLikesCounter(holder);

            }else
            {
                Toast.makeText(context, "Already Liked", Toast.LENGTH_SHORT).show();
            }
        }else if(viewId == R.id.iv_dislike)
        {
            HomeHolderView holder = (HomeHolderView) view.getTag();
            if (!unLikedPositions.contains(holder.getPosition())) {
                unLikedPositions.add(holder.getPosition());
                updateDisLikesCounter(holder);


            } else {
                Toast.makeText(context, "Already Un Liked", Toast.LENGTH_SHORT).show();

            }

        }

    }*/

    public void doPostAction(String strContentId, String strAction) {

        JsonObject data = new JsonObject();
        JsonObject dataFromBean = new JsonObject();


        data.addProperty("contentId", strContentId);
        data.addProperty("action", strAction);

        data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);


        // data.add("loginData", dataFromBean);
        data.add("source", Constants.getDeviceInfo());
        Log.e("Data Packet in JSON", "" + data.toString()+"   ---->"+strAction);


        GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                Request.Method.POST, Constants.postFeedAction, JsonObject.class, null,
                successListener(), errorListener(), data);

        VolleySingleton.getInstance(context).addToRequestQueue(myReq, "postAction");

        if (strAction.equals("1")) {
            showProgressDialog(context, "Posting Like");
        } else if (strAction.equals("2")) {
            showProgressDialog(context, "Posting Un Like");
        }

        Log.e(" Url", " link" + myReq.toString());
    }


    private Response.Listener<JsonObject> successListener() {
        return new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                //  JsonObject jsonObject,jsonObjectLogin;
                //   Log.e("Like Action", "" + response);
                if (response != null) {


                    Log.e("Action ", "" + response);

                    dismissDialog();
                }

            }
        };
    }


    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.i("Debug: MainActivity", error.toString());
                dismissDialog();
                handleVolleyError(error);

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
            str = "Oops! Data Received Was An Unreadable Mess";
            //TODO
        }
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class HomeHolderView extends RecyclerView.ViewHolder {

        RelativeTimeTextView timestamp;
        TextView title;
        TextView description;
        TextView likeTv;
        TextView dislikeTv;
        ImageView list_icon;
        ImageButton like, unlike;
        WebView videoView;
        View listItemSeperator;
        LinearLayout mediaLayout;
        ProgressBar progressBar;
        // YouTubePlayerView youtube_player;

        public HomeHolderView(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_postheader);
            list_icon = (ImageView) itemView.findViewById(R.id.img_postimage);
            description = (TextView) itemView.findViewById(R.id.tv_postmassege);
            likeTv = (TextView) itemView.findViewById(R.id.tv_like);
            //likeTv.setTag(this);
            dislikeTv = (TextView) itemView.findViewById(R.id.tv_dislike);
            //dislikeTv.setTag(this);
            timestamp = (RelativeTimeTextView) itemView.findViewById(R.id.tv_posttime);

            like = (ImageButton) itemView.findViewById(R.id.iv_like);
            //like.setTag(this);
            unlike = (ImageButton) itemView.findViewById(R.id.iv_dislike);
            //unlike.setTag(this);
            mediaLayout = (LinearLayout) itemView.findViewById(R.id.mediaLayout);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            videoView = (WebView) itemView.findViewById(R.id.vid_postvideo);
            listItemSeperator = itemView.findViewById(R.id.list_item_seperator);
        }


    }

    private static String removeChar(String s, char c) {
        StringBuffer buf = new StringBuffer(s.length());
        buf.setLength(s.length());
        int current = 0;
        for (int i=0; i<s.length(); i++){
            char cur = s.charAt(i);
            if(cur != c) buf.setCharAt(current++, cur);
        }
        return buf.toString();
    }

    @Override
    public Filter getFilter() {
        if (mFeedFilter == null)
            mFeedFilter = new FeedFilters();
        return mFeedFilter;
    }

    private class FeedFilters extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // Create a FilterResults object
            FilterResults results = new FilterResults();

            // If the constraint (search string/pattern) is null
            // or its length is 0, i.e., its empty then
            // we just set the `values` property to the
            // original contacts list which contains all of them
            if (constraint == null || constraint.length() == 0) {
                results.values = list;
                results.count = list.size();
            } else {
                // Some search copnstraint has been passed
                // so let's filter accordingly
                ArrayList<Datum> filteredContacts = new ArrayList<Datum>();

                // We'll go through all the contacts and see
                // if they contain the supplied string
                for (Datum c : list) {
                    if (c.getContentDetails().get(0).getContentType().contains(constraint.toString().toUpperCase())) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredContacts.add(c);
                    }
                }

                // Finally set the filtered values and size/count
                results.values = filteredContacts;
                results.count = filteredContacts.size();
            }

            // Return our FilterResults object
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (ArrayList<Datum>) results.values;
            notifyDataSetChanged();
        }
    }


    public void resetData() {
        list = originalData;
    }


    ProgressDialog progressDialog;

    public void showProgressDialog(Activity ctx, String msg) {


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ctx);
        }

        progressDialog.getLayoutInflater();
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissDialog() {
        progressDialog.dismiss();
    }
}
