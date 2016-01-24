package com.cgp.saral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgp.saral.R;
import com.cgp.saral.model.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by karamjeetsingh on 03/09/15.
 */
public class StateDistAdapter extends BaseAdapter{
   HashMap<Integer ,String>  hashMap;
   ArrayList<String> listData;
       Context context;

    public StateDistAdapter(Context ctx,  HashMap<Integer, String> map) {

        this.hashMap=map;
        this.context=ctx;
        listData=new ArrayList<>(map.values());

    }


    @Override
    public String getItem(int position) {
        if (listData != null) {
            return listData.get(position);
        } else {
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
    {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override public View getView(int pos, View cnvtView, ViewGroup prnt)
    {
        return getCustomView(pos, cnvtView, prnt);
    }

    @Override
    public int getCount() {
        if(listData.size()<0)
        {
           return 0;
        }else
        {

            return listData.size();
        }

    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        HashMap<Integer ,String> map;
        View mySpinner = inflater.inflate(R.layout.userprofile_adapter, parent, false);
        TextView main_text = (TextView) mySpinner.findViewById(R.id.tv_listdata);
        //map=list.get(position);


            main_text.setText(listData.get(position).toString());


            return mySpinner;
    }
    public String getdata()
    {
        String s="";
        Set set = hashMap.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext())
        {
            Map.Entry m = (Map.Entry)
                it.next();
            s=m.getValue().toString();
            System.out.println(m.getKey() + ":" + m.getValue());

        }
        return s;
    }
}


