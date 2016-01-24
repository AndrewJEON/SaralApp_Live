package com.cgp.saral.adapter;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgp.saral.R;
import com.cgp.saral.customviews.OnItemClickListener;
import com.cgp.saral.model.DrawerItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by karamjeetsingh on 31/08/15.
 */
public class RightSideAdapter extends BaseAdapter {

    ViewHolder holder1;
    Context context;
    List<DrawerItem> mylist;


    OnItemClickListener listener;

    public RightSideAdapter(List<DrawerItem> items, int x, Context ctx, OnItemClickListener list) {

        Log.d("mDrawerlayout", "RightSideAdapter: constructor ");
        this.context = ctx;
        this.mylist = items;
        this.listener = list;

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mylist.get(position);
    }

    public int getItemInteger(int pos) {
        return pos;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mylist.size();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Log.d("mDrawerlayout", "getView: method ");
        LayoutInflater inflator = LayoutInflater.from(context);
        //Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto/Roboto-Medium.ttf");
        convertView = inflator.inflate(R.layout.drawer_item, parent, false);
        holder1 = new ViewHolder();
        holder1.text = (TextView) convertView.findViewById(R.id.txtData);
        holder1.text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        holder1.iv = (ImageView) convertView.findViewById(R.id.imgView);

        convertView.setTag(holder1);
        final String text = mylist.get(position).getTitle();
        holder1.text.setText(text);
        holder1.iv.setImageResource(mylist.get(position).getIcon());
        // holder1.text.setTypeface(typeface);

        holder1.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (listener != null) {
                   listener.onItemClicked(v, position);
                }
               /* if (text.equalsIgnoreCase("item 1")) {
                    Log.d("mDrawerlayout", "setOnClickListener: " + text);
                } else if (text.equalsIgnoreCase("item 2")) {
                    Log.d("mDrawerlayout", "setOnClickListener: " + text);
                } else if (text.equalsIgnoreCase("item 3")) {
                    Log.d("mDrawerlayout", "setOnClickListener: " + text);
                }*/


            }
        });
        return convertView;
    }


}


class ViewHolder {
    TextView text, textcounter;
    ImageView iv;



}
