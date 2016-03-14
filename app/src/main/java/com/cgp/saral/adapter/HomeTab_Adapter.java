package com.cgp.saral.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.youtube.player.YouTubeIntents;
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

    String TAG = "HomeTab_Adapter";

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
        HomeHolderView holderView = null;
        try{
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.fragment_hometab_adapter, viewGroup, false);
         holderView = new HomeHolderView(view.getContext(),view);
        contents = dbController.contentActionList();
        }catch (Throwable t){
            Log.e("HomeTab_Adapter",t.getMessage(),t);
        }
        return holderView;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hView, int i) {

        try{
        final HomeHolderView holdview = (HomeHolderView) hView;


        doProcessDataBinding(i, holdview);
        }catch (Throwable t){
            Log.e("HomeTab_Adapter",t.getMessage(),t);
        }


    }


    private void doProcessDataBinding(int i, final HomeHolderView holdview) {
        try{
        final Datum d = list.get(i);
        int listSepColor = Color.parseColor(itemSepColors.get(i%4));
        holdview.listItemSeperator.setBackgroundColor(listSepColor);
       // holdview.timestamp.setTextColor(listSepColor);
       // holdview.shareBtnRl.setBackgroundColor(listSepColor);
        holdview.title.setText(d.getSummary());

        holdview.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,d.getSummary());
                String url = d.getUrl();
                if(url == null || url.isEmpty()){
                    url = Constants.SARALVAASTU_WEB_URL;
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT, d.getDescription() +" -> for more details follow us at "+ url);
                sendIntent.setType("text/plain");
                v.getContext().startActivity(Intent.createChooser(sendIntent, "Share post via"));

            }
        });
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

        }
        if(likesCount.containsKey(holdview.getAdapterPosition()))
        {

            holdview.likeTv.getTag();
            holdview.likeTv.setText("" + likesCount.get(holdview.getAdapterPosition()) +" likes");
        }

        if (!disLikesCount.containsKey(holdview.getAdapterPosition())) {

            disLikesCount.put(holdview.getAdapterPosition(), disLikeCount);
        }
        if(disLikesCount.containsKey(holdview.getAdapterPosition()))
        {

            holdview.dislikeTv.getTag();
            holdview.dislikeTv.setText("" + disLikesCount.get(holdview.getAdapterPosition()) +" dislikes");
        }


        try {

            long refTime = new Date(d.getApprovalDate()).getTime();

            holdview.timestamp.setReferenceTime(refTime);
            holdview.timestamp.setPrefix("Posted on ");
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
                String strVideoThumbnail = d.getContentDetails().get(0).getThumbnailPath();
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

                strURL = d.getContentDetails().get(0).getMediaPath();


                holdview.buttonPreview.setVisibility(View.VISIBLE);
                holdview.buttonPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(holdview.activityContext, strURL, true, true);
                        holdview.activityContext.startActivity(intent);
                    }
                });


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
        }catch (Throwable t){
            Log.e("HomeTab_Adapter",t.getMessage(),t);
        }
    }

    private void doProcessDataBindingClick(final Datum d, final HomeHolderView holdview) {
try{
        String strLiked = d.getYouLiked();
        String strUnLiked = d.getYouDisliked();
        Log.e("StrLiked", "" + strLiked + "  unliked -->" + strUnLiked);


        if (strLiked.equals("true")&&strUnLiked.equals("false")) {
            int contentId = Integer.parseInt(d.getId());

            if (!likedPositions.contains(contentId)) {

                likedPositions.add(contentId);

            }
            if(likedPositions.contains(contentId)&& likesCount.containsKey(holdview.getAdapterPosition()))
            {

               holdview.like.setChecked(true);
            }

        }else if (strLiked.equals("false")&&strUnLiked.equals("true")) {
            int contentId = Integer.parseInt(d.getId());

            if (!unLikedPositions.contains(contentId)) {

                unLikedPositions.add(contentId);


            }
            if(unLikedPositions.contains(contentId)&& disLikesCount.containsKey(holdview.getAdapterPosition()))
            {
                holdview.unlike.setChecked(true);
            }
        }



        holdview.likeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                int contentId = Integer.parseInt(d.getId());
                switch (i){
                    case R.id.iv_like:

                            likedPositions.add(contentId);
                            Log.e("Content Action", " Like The Content");
                            updateLikesCounter(holdview,d);

                        break;
                    case R.id.iv_dislike:
                            unLikedPositions.add(contentId);
                            updateDisLikesCounter(holdview, d);
                            Log.e("Content Action", " Unlike The Content");
                        break;
                }
            }
        });
}catch (Throwable t){
    Log.e("HomeTab_Adapter",t.getMessage(),t);
}


    }




    private void updateLikesCounter(HomeHolderView holder, Datum d) {
try{
        int currentLikesCount = likesCount.get(holder.getAdapterPosition());
        String strContentId = d.getContentDetails().get(0).getContentId();

        doPostAction(strContentId, "1");
        currentLikesCount=currentLikesCount+1;
        String strL = String.valueOf(currentLikesCount);

        holder.likeTv.setText(strL +" likes");

        likesCount.put(holder.getAdapterPosition(), currentLikesCount);
        int contentId = Integer.parseInt(d.getId());
        if(unLikedPositions.contains(contentId)){
            int currentDisLikesCount = disLikesCount.get(holder.getAdapterPosition());
            currentDisLikesCount=currentDisLikesCount-1;
             strL = String.valueOf(currentDisLikesCount);

            holder.dislikeTv.setText(strL +" dislikes");

            disLikesCount.put(holder.getAdapterPosition(), currentDisLikesCount);
            unLikedPositions.remove(Integer.getInteger(contentId+""));
        }
}catch (Throwable t){
    Log.e("HomeTab_Adapter",t.getMessage(),t);
}
    }

    private void updateDisLikesCounter(HomeHolderView holder, Datum d) {
        try{
        int currentDisLikesCount = disLikesCount.get(holder.getAdapterPosition());

        String strContentId = d.getContentDetails().get(0).getContentId();

        doPostAction(strContentId, "2");
        currentDisLikesCount=currentDisLikesCount+1;
        String strL = String.valueOf(currentDisLikesCount);

        holder.dislikeTv.setText(strL +" dislikes");

        disLikesCount.put(holder.getAdapterPosition(), currentDisLikesCount);
        int contentId = Integer.parseInt(d.getId());
        if(likedPositions.contains(contentId)){
            int currentLikesCount = likesCount.get(holder.getAdapterPosition());
            currentLikesCount=currentLikesCount-1;
            strL = String.valueOf(currentLikesCount);

            holder.likeTv.setText(strL +" likes");

            likesCount.put(holder.getAdapterPosition(), currentLikesCount);
            likedPositions.remove(Integer.getInteger(contentId+""));
        }
        }catch (Throwable t){
            Log.e("HomeTab_Adapter",t.getMessage(),t);
        }
    }



    public void doPostAction(String strContentId, String strAction) {
try{
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
}catch (Throwable t){
    Log.e("HomeTab_Adapter",t.getMessage(),t);
}
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
        RadioButton like, unlike;
        //YouTubePlayerSupportFragment videoView;
        View listItemSeperator;
        FrameLayout mediaLayout;
        ProgressBar progressBar;
        ImageButton shareBtn;
        LinearLayout shareBtnRl;
        RadioGroup likeGroup;
        ImageView buttonPreview;
        Context activityContext;
        // YouTubePlayerView youtube_player;

        public HomeHolderView(Context activityContext, View itemView) {
            super(itemView);
            this.activityContext = activityContext;
            try{
            title = (TextView) itemView.findViewById(R.id.tv_postheader);
            list_icon = (ImageView) itemView.findViewById(R.id.img_postimage);
            description = (TextView) itemView.findViewById(R.id.tv_postmassege);
            likeTv = (TextView) itemView.findViewById(R.id.tv_like);
            //likeTv.setTag(this);
            dislikeTv = (TextView) itemView.findViewById(R.id.tv_dislike);
            //dislikeTv.setTag(this);
            timestamp = (RelativeTimeTextView) itemView.findViewById(R.id.tv_posttime);

            like = (RadioButton) itemView.findViewById(R.id.iv_like);
            //like.setTag(this);
            unlike = (RadioButton) itemView.findViewById(R.id.iv_dislike);
            //unlike.setTag(this);
            mediaLayout = (FrameLayout) itemView.findViewById(R.id.mediaLayout);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            //videoView = (YouTubePlayerSupportFragment) ((FragmentActivity)activityContext).getSupportFragmentManager().findFragmentById(R.id.youtube_holder);
            listItemSeperator = itemView.findViewById(R.id.list_item_seperator);
            shareBtn = (ImageButton) itemView.findViewById(R.id.shareBtn);
            shareBtnRl = (LinearLayout)itemView.findViewById(R.id.shareBtnRl);
            likeGroup = (RadioGroup)itemView.findViewById(R.id.like_container);
                buttonPreview = (ImageView) itemView.findViewById(R.id.button_preview);
            }catch (Throwable t){
                Log.e("HomeTab_Adapter",t.getMessage(),t);
            }
        }


    }

    private static String removeChar(String s, char c) {
        StringBuffer buf = new StringBuffer(s.length());
        try{
        buf.setLength(s.length());
        int current = 0;
        for (int i=0; i<s.length(); i++){
            char cur = s.charAt(i);
            if(cur != c) buf.setCharAt(current++, cur);
        }
        }catch (Throwable t){
            Log.e("HomeTab_Adapter",t.getMessage(),t);
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
            FilterResults results = new FilterResults();
            try{
            // Create a FilterResults object
                String language = null;
                String category = null;

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
                String[] constraints = constraint.toString().split(",");


                if(constraints != null && constraints.length>0) {
                    language = constraints[0];

                    if(constraints.length>1) {
                        category = constraints[1];
                    }


                // We'll go through all the contacts and see
                // if they contain the supplied string
                for (Datum c : list) {
                    if(category != null && language != null) {
                        if (c.getContentDetails().get(0).getContentType().contains(category.toString().toUpperCase())
                                && c.getLanguage().equalsIgnoreCase(language)) {
                            // if `contains` == true then add it
                            // to our filtered list
                            filteredContacts.add(c);
                        }
                    }else if(category != null){
                        if (c.getContentDetails().get(0).getContentType().contains(category.toString().toUpperCase())) {
                            // if `contains` == true then add it
                            // to our filtered list
                            filteredContacts.add(c);
                        }
                    }else if(language != null){
                        if (c.getLanguage().equalsIgnoreCase(language)) {
                            // if `contains` == true then add it
                            // to our filtered list
                            filteredContacts.add(c);
                        }
                    }
                }

                // Finally set the filtered values and size/count
                results.values = filteredContacts;
                results.count = filteredContacts.size();
                }else{
                    results.values = list;
                    results.count = list.size();
                }
            }
            }catch (Throwable t){
                Log.e("HomeTab_Adapter",t.getMessage(),t);
            }
            // Return our FilterResults object
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try{
            list = (ArrayList<Datum>) results.values;
            notifyDataSetChanged();
            }catch (Throwable t){
                Log.e("HomeTab_Adapter",t.getMessage(),t);
            }
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
