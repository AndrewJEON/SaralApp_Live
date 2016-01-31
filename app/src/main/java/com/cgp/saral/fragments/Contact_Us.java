package com.cgp.saral.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cgp.saral.R;
import com.cgp.saral.myutils.SharedPreferenceManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WeeSync on 17/10/15.
 */
public class Contact_Us extends Fragment {

    View view;

    private static final String ARG_PAGE_NUMBER = "page_number";
    @Bind(R.id.contactList)
    ListView contacts;

    @Bind(R.id.tvCorpPhone)
    TextView tvCorpPhone;
    @Bind(R.id.tvCorpEmail)
    TextView tvCorpEmail;

    public Contact_Us() {

    }

    public static Contact_Us newInstance(int page) {
        Contact_Us fragment = new Contact_Us();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_us, container, false);
        ButterKnife.bind(this,view);
        JSONObject corporate = null;
        ArrayList<HashMap<String, String>> feedList= new ArrayList<HashMap<String, String>>();
        try {
                String jsonString = SharedPreferenceManager.getSharedInstance().getStringFromPreferances("contacts");

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray branches = jsonObject.getJSONArray("branches");
            Gson gson=new Gson();
            HashMap<String,String> map=new HashMap<String,String>();

            for(int i=0;i<branches.length();i++) {
                    map=(HashMap<String,String>) gson.fromJson(branches.get(i).toString(), map.getClass());
                    feedList.add(map);
                }
                corporate = jsonObject.getJSONObject("corporate");
        }catch(Exception e){
            Log.e("ContactUs","Unable to parse contacts :"+ e.getMessage());
        }

        if(corporate != null){
            try {
                tvCorpPhone.setText(corporate.getString("phone"));
                tvCorpEmail.setText(corporate.getString("email"));
            }catch(Exception e){
                Log.e("ContactUs","Unable to parse contacts :"+ e.getMessage());
            }
        }
        /*ArrayList<HashMap<String, String>> feedList= new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Mumbai");
        map.put("phone", "+91 2203004044");

        feedList.add(map);

        map = new HashMap<String, String>();
        map.put("name", "Pune");
        map.put("phone", "+91 2203452454");
        feedList.add(map);
        map = new HashMap<String, String>();
        map.put("name", "Hyderabad");
        map.put("phone", "+91 4003452454");
        feedList.add(map);*/

        SimpleAdapter contactsAdapter = new SimpleAdapter(getActivity(), feedList, R.layout.contact_row, new String[]{"name", "phone"}, new int[]{R.id.textViewName, R.id.textViewPhone});
        contacts.setAdapter(contactsAdapter);
        return view;
    }
}
