package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cgp.saral.R;
import com.cgp.saral.adapter.StateDistAdapter;
import com.cgp.saral.adapter.UserProfileAdapter;
import com.cgp.saral.customviews.MultiSpinner;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.CreateUserResponse;
import com.cgp.saral.model.Language;
import com.cgp.saral.model.LocationItems;
import com.cgp.saral.model.UserData;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.SharedPreferenceManager;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationSecondFragment extends Fragment implements View.OnClickListener {
    View view;
    Bundle bundle;
    /* Bean class initilize*/
    Userdata_Bean bean;
    Userdata_Bean newbean;
    /*All spinner Adapter */
    UserProfileAdapter genderAdapter=null, stateAdapter=null;//, langAdapter=null, cityAdapter=null ;
    //StateDistAdapter distAdapter=null;
    /*Components*/
    TextInputLayout name, mobile, email, dob;//, address, district;
    CheckBox termondition;
    ImageView back;
    TextView title,term;
    AppCompatButton submit;
    ProgressDialog progressDialog ;
    MaterialSpinner gender, state;//, language, spinnercity, spinnerdist;
    String strContact, strName, strEmail, strDOB, strGender = "", strState = "";//, strLanguage = "", strIntrest = "", strState = "", strDist = "", strAddress = "";
    /*set spinner data in hashmap with Arraylist*/
  //  public static HashMap<Integer, String> langMap = new HashMap<>();
    public static HashMap<Integer, String> gendremap = new HashMap<>();
    public static HashMap<Integer, String> statemap = new HashMap<>();
   // public static HashMap<Integer, String> cityMap = new HashMap<>();
   // public static HashMap<Integer, String> distMap = new HashMap<>();
    ArrayList<String> langData;
    ArrayList<String> gendreData;
    ArrayList<String> stateData;

    ArrayList<String> cityData;
    ArrayList<String> distData;


    String intrestides;

    DataController controller;
    String strintrest = "";
    Calendar myCalendar = Calendar.getInstance();
   // private MultiSpinner spinner;

    /*Shareprefrences */
    SharedPreferences preferences;
    SharedPreferences.Editor lngeditor, stateedit, genedit, distEdit;



    ArrayList<LocationItems> stateL;

    SharedPreferences.Editor editor;

    public RegistrationSecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration_second, container, false);
        preferences = getActivity().getSharedPreferences(Constants.PREFS_NAME_EDIT, Context.MODE_PRIVATE);
        lngeditor = preferences.edit();
        stateedit = preferences.edit();
        genedit = preferences.edit();
        distEdit=preferences.edit();
        editor=preferences.edit();
        controller = DataController.getsInstance(getActivity());

     //   setRetainInstance(true);
        /*initilization of hashmap and arraylist*/
        variableInit();
        /*get data from another fragment*/
        reciveData();
         /*initilization of component*/
        inIt(view);

        setAdapter();

        //controller.createDb();


        return view;
    }


    public void variableInit() {

       /* ArrayList<Language> langs= controller.languageList();

        Collections.sort(langs, new Comparator<Language>() {
            public int compare(Language v1, Language v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
*/
        /*for(Language l:langs)
        {
            langMap.put(Integer.valueOf(l.getId()),l.getName());
        }

        HashMap<Integer, String> langMapL=Utils.sortByValues(langMap);

        langMap.clear();
        langMap=langMapL;
        langData = new ArrayList<>(langMap.values());*/
       // langData = new ArrayList<>(langMap.values());

        gendremap.put(200001, "Male");
        gendremap.put(200002, "Female");
        gendreData = new ArrayList<>(gendremap.values());

        stateL =controller.locationList(Constants.LOCATION_STATE);
       /* Collections.sort(stateL, new Comparator<LocationItems>() {
            public int compare(LocationItems v1, LocationItems v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });*/


        for(LocationItems ll :stateL)
        {

            statemap.put(Integer.valueOf(ll.getId()), ll.getName());
        }


        HashMap<Integer, String> statemapL=Utils.sortByValues(statemap);

        statemap.clear();
        statemap=statemapL;


        stateData = new ArrayList<>(statemap.values());


    }

    public void inIt(View view) {
        title = (TextView) view.findViewById(R.id.txt_title);
        term = (TextView) view.findViewById(R.id.term);
        title.setText("Personal Information");
        submit = (AppCompatButton) view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
        termondition = (CheckBox) view.findViewById(R.id.tv_termcondition);
        name = (TextInputLayout) view.findViewById(R.id.tv_username);
        back = (ImageView) view.findViewById(R.id.iv_back);
        mobile = (TextInputLayout) view.findViewById(R.id.tv_regsecMobile);
        email = (TextInputLayout) view.findViewById(R.id.tv_regsecMail);
        dob = (TextInputLayout) view.findViewById(R.id.tv_dob);
       // language = (MaterialSpinner) view.findViewById(R.id.spinnerlanguage);
        state = (MaterialSpinner) view.findViewById(R.id.spinnerstate);
       // spinner = (MultiSpinner) view.findViewById(R.id.spinnerintrest);
        gender = (MaterialSpinner) view.findViewById(R.id.spinnergender);
      /*  spinnercity = (MaterialSpinner) view.findViewById(R.id.spinnercity);

        spinnerdist = (MaterialSpinner) view.findViewById(R.id.spinnerdist);
       // address = (TextInputLayout) view.findViewById(R.id.tv_address);*/

        name.getEditText().setKeyListener(TextKeyListener.getInstance());


        name.getEditText().setText(bean.getUserFName());

        if(bean.getContact1() !=null && !bean.getContact1().isEmpty())
        {
            mobile.getEditText().setText(bean.getContact1());
            mobile.getEditText().setOnKeyListener(null);
            mobile.getEditText().setEnabled(false);
            mobile.getEditText().setFocusable(false);
        }
        if(bean.getMail() !=null && !bean.getMail().isEmpty())
        {
            email.getEditText().setText(bean.getMail());
            email.getEditText().setOnKeyListener(null);
            email.getEditText().setEnabled(false);
            email.getEditText().setFocusable(false);
        }


        dob.getEditText().setText(bean.getDob());
        // tv_intrestlist.setText(strintrest_append);
        name.getEditText().addTextChangedListener(new CustomTextWatcher(name, submit, getActivity()));
        mobile.getEditText().addTextChangedListener(new CustomTextWatcher(mobile, submit, getActivity()));
        dob.getEditText().addTextChangedListener(new CustomTextWatcher(dob, submit, getActivity()));
        email.getEditText().addTextChangedListener(new CustomTextWatcher(email, submit, getActivity()));


        dob.getEditText().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //Log.e("inIt", "strName " + strName);
        //Log.e("inIt", "strContact " + strContact);
        //Log.e("inIt", "strDOB " + strDOB);
        //Log.e("inIt", "strIntrest " + strIntrest);
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://saralvaastu.com/mobileapp-terms-and-conditions/"));
                startActivity(browserIntent);
            }
        });
    }


    public void setAdapter() {


        /*cityAdapter = new UserProfileAdapter(getActivity(), 1, cityMap);
        spinnercity.setAdapter(cityAdapter);
        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == -1) && cityData.size() >= position) {
                    //Log.e("onItemSelected", "position hash" + langData.get(position).toString());
                    strDist = Constants.getdata(cityData.get(position).toString(), cityMap);

                    Log.e("onItemSelected", "position " + position);
                    // Log.e("onItemSelected", "strLanguage  " + strLanguage + " gender " + strGender + " State " + strState);
                } else {
                    Log.e("onItemSelected", "return  mt");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        langAdapter = new UserProfileAdapter(getActivity(), 1, langMap);
        language.setAdapter(langAdapter);
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == -1) && langData.size() >= position) {
                    //Log.e("onItemSelected", "position hash" + langData.get(position).toString());
                    strLanguage = Constants.getdata(langData.get(position).toString(), langMap);
                    lngeditor.putInt(Constants.PREFS_LANG_POSITION, position);
                    lngeditor.commit();
                    Log.e("onItemSelected", "position " + position);
                    // Log.e("onItemSelected", "strLanguage  " + strLanguage + " gender " + strGender + " State " + strState);
                } else {
                   // Toast.makeText(getActivity(),"Please select the Language",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        genderAdapter = new UserProfileAdapter(getActivity(), 1, gendremap);
        gender.setAdapter(genderAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == -1) && gendreData.size() >= position) {


                    strGender = Constants.getdata(gendreData.get(position).toString(), gendremap);

                    genedit.putInt(Constants.PREFS_GENDER_POSITION, position);
                    genedit.commit();
                    Log.e("onItemSelected", "Gender position " + position);
                    //Log.e("onItemSelected", "strLanguage  " + strLanguage + " gender " + strGender + " State " + strState);
                } else {
                   // Toast.makeText(getActivity(),"Please select the Gender",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateAdapter = new UserProfileAdapter(getActivity(), 1, statemap);
        state.setAdapter(stateAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == -1) && stateData.size() >= position) {

                    strState = Constants.getdata(stateData.get(position).toString(), statemap);



                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateDist(strState);
                        }
                    });
                    stateedit.putInt(Constants.PREFS_STATE_POSITION, position);
                    stateedit.commit();
                    Log.e("onItemSelected", "State position " + position +"...."+strState);
                    // Log.e("onItemSelected", "strLanguage  " + strLanguage + " gender " + strGender + " State " + strState);
                } else {
                    //Toast.makeText(getActivity(),"Please select the State",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* // intrestAdapter=new UserProfileAdapter(getActivity(), 1,intrestData);
        spinner.setItems(Constants.intrestData);
        strIntrest = spinner.getSelectedItemsAsString();*/

        //spinner.setAdapter(intrestAdapter);
        //spinner.setOnItemSelectedListener(this);


    }

    public void reciveData() {
        bundle = getArguments();

        bean = (Userdata_Bean) bundle.getSerializable("userdata");
        Log.e("RegistSecondDragment", "Bean Data " + bean);
        // Log.e("RegistrationSecond","Data "+rc_contact_no+" and Password "+rc_password);

    }

    public boolean validCellPhone(String number) {
        if (number.length() != 10 && number.length() < 10) {
            Toast.makeText(getActivity(), "Please enter 10 digit mobile no", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }


    private void updateDist(String strState)
    {
        /*Log.e("State for district",""+strState +"   --> "+stateL.size());

        ArrayList<LocationItems> distFiltered=controller.locationListFiltered(Constants.LOCATION_DIST,strState);

       // stateL =controller.locationList(Constants.LOCATION_STATE);
       *//* Collections.sort(distFiltered, new Comparator<LocationItems>() {
            public int compare(LocationItems v1, LocationItems v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });*//*

        if(distFiltered.size()<=0)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), " No District for this State ", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        Log.e(" District Size", "" + distFiltered.size());

        if(distMap.size()>0)
        {
            distMap.clear();
        }



        for(LocationItems ll:distFiltered)
        {
            distMap.put(Integer.valueOf(ll.getId()), ll.getName());
        }

        strDist="";

        HashMap<Integer, String> distMapL=Utils.sortByValues(distMap);

        distMap.clear();
        distMap=distMapL;

        distData = new ArrayList<>(distMap.values());
       // distAdapter.notifyDataSetChanged();

        Log.e("  ---> ","  "+distMap.size());
        distAdapter = new StateDistAdapter(getActivity(), distMap);
        spinnerdist.setAdapter(distAdapter);
        spinnerdist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == -1) && distData.size() >= position) {
                    //Log.e("onItemSelected", "position hash" + langData.get(position).toString());
                    strDist = Constants.getdata(distData.get(position).toString(), distMap);

                    distEdit.putInt(Constants.PREFS_DIST_POSITION, position);
                    distEdit.commit();
                    Log.e("onItemSelected", "position " + position + " ---->"+strDist);
                    // Log.e("onItemSelected", "strLanguage  " + strLanguage + " gender " + strGender + " State " + strState);
                } else {
                    //Toast.makeText(getActivity(),"Please select the District",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    @Override
    public void onClick(View v) {

       /* List<Integer> id = spinner.getSelectedIndices();
        intrestides = Utils.intrestValue(id);
        Log.e("Intrest data", " Selected Data intrest" + strIntrest);
        Log.e("Intrest data", " Selected Data intrest id length " + id.size() + " and data " + intrestides);


        strIntrest = spinner.getSelectedItemsAsString();
        Log.e("Intrest data", " Selected Data intrest" + strIntrest);
        //  Log.e("Intrest data", " Selected Data" + strIntrest);*/

        // strIntrest=tv_intrestlist.getText().toString().trim();
        strName = name.getEditText().getText().toString().trim();
        strContact = mobile.getEditText().getText().toString().trim();
        strDOB = dob.getEditText().getText().toString().trim();
        strEmail = email.getEditText().getText().toString().trim();

       // strAddress = address.getEditText().getText().toString().trim();
        /*if (strContact.length() != 10 && strContact.length() < 10) {
            Toast.makeText(getActivity(), "Please enter mobile no.", Toast.LENGTH_SHORT).show();
        }*/
        newbean = new Userdata_Bean();

        newbean.setUserFName(strName);
        newbean.setContact1(strContact);
        newbean.setMail(strEmail);
        newbean.setDob(strDOB);
        newbean.setGender(strGender);
        newbean.setPassword(bean.getPassword());
       // newbean.setLanguage(strLanguage);
        newbean.setSocial_id(bean.getSocial_id());
        if (bean.getSocialtype() != null) {
            newbean.setSocialtype(bean.getSocialtype());
        } else {
            newbean.setSocialtype("normal");
        }

        newbean.setState(strState);
       // newbean.setIntrest(strIntrest);
        newbean.setImgurl(bean.getImgurl());
        Log.e("NewBean", "bean " + newbean.getSocialtype());


        if (strName.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strName" + strName);
            return;
        } else if (!strName.isEmpty() && strDOB.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your DOB", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strDOB" + strDOB);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && strContact.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter mobile no.", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strContact" + strContact);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && strContact.startsWith("0")) {

            Toast.makeText(getActivity(), "Mobile Number Can't be started from 0", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strContact.startsWith(\"0\")" + 3);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strContact.startsWith("0") && !validCellPhone(strContact)) {

            Toast.makeText(getActivity(), "Please enter 10 digit mobile no.", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation validCellPhone");
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && strEmail.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your email id", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 5);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && !Utils.isValidEmail(strEmail)) {
            Toast.makeText(getActivity(), "Please enter a valid email id", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 6);
            return;

        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && strGender.isEmpty()) {
            Toast.makeText(getActivity(), "Please select gender", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 7);
            return;
        }else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && !strGender.isEmpty() && strState.isEmpty()) {
            Toast.makeText(getActivity(), "Please select your state", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 9);
            return;
        } /* else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && !strGender.isEmpty() && strLanguage.isEmpty()) {
            Toast.makeText(getActivity(), "Please select preferred language", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 8);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && !strGender.isEmpty() && !strLanguage.isEmpty() && strState.isEmpty()) {
            Toast.makeText(getActivity(), "Please select your state", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 9);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && !strGender.isEmpty() && !strLanguage.isEmpty() && !strState.isEmpty() && strDist.isEmpty()) {
            Toast.makeText(getActivity(), "Please select your district", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 9);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && !strGender.isEmpty() && !strLanguage.isEmpty() && !strState.isEmpty() && !strDist.isEmpty() && strAddress.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your Address", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 9);
            return;
        } else if (!strName.isEmpty() && !strDOB.isEmpty() && !strContact.isEmpty() && !strEmail.isEmpty() && !strGender.isEmpty() && !strLanguage.isEmpty() && !strState.isEmpty() && !strDist.isEmpty() && !strAddress.isEmpty() && intrestides.isEmpty()) {
            Toast.makeText(getActivity(), "Please select your Interest", Toast.LENGTH_SHORT).show();
            Log.e("NewBean", "Validation " + 10);
            return;
        } */else {

            processData();
        }


    }

    private void processData() {
        if (!Utils.isConnectedToInternet(getActivity())) {
            Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
            return;

        }
        Log.e("NewBean", "Validation " + 10);
        if (termondition.isChecked()) {
            Log.e("NewBean", "Validation " + 11);

            Date date = new Date();
            Constants.USER_PROFILE_PIC=newbean.getImgurl();

            JsonObject data = new JsonObject();
            JsonObject dataFromBean = new JsonObject();

            dataFromBean.addProperty("UserId", "");
            //dataFromBean.addProperty("CreatedDate", Constants.getFormattedDate(getFormatDate(date)));

            dataFromBean.addProperty("UserName", "" + newbean.getContact1());
            dataFromBean.addProperty("FirstName", "" + newbean.getUserFName());

            dataFromBean.addProperty("LastName", "");
            dataFromBean.addProperty("DOB", Constants.getFormattedDate(newbean.getDob()));

            dataFromBean.addProperty("MobileNumber", "" + newbean.getContact1());
            //dataFromBean.addProperty("Language", "" + newbean.getLanguage());

            dataFromBean.addProperty("Gender", "" + newbean.getGender());
            dataFromBean.addProperty("State", "" + newbean.getState());
             dataFromBean.addProperty("PhotoPath", newbean.getImgurl());

           /* dataFromBean.addProperty("City", strDist);
            dataFromBean.addProperty("Address", strAddress);
            dataFromBean.addProperty("DistrictId", strDist);*/
            dataFromBean.addProperty("Email", "" + newbean.getMail());

            dataFromBean.addProperty("Password", "" + newbean.getPassword());
           // dataFromBean.addProperty("CreatedBy", "9");
            dataFromBean.addProperty("Status", "2");

            if (newbean.getSocialtype().equalsIgnoreCase("fb")) {
                dataFromBean.addProperty("FacebookId", "" + newbean.getSocial_id());
            } else if (newbean.getSocialtype().equalsIgnoreCase("gplus")) {
                dataFromBean.addProperty("GoogleId", "" + newbean.getSocial_id());
            } else {
                dataFromBean.addProperty("FacebookId", "");
                dataFromBean.addProperty("GoogleId", "");
            }


            dataFromBean.addProperty("RoleId", "3");


           // dataFromBean.addProperty("InterestsTag", "" + intrestides);

            data.add("userInfo", dataFromBean);
            data.add("sourceInfo", Constants.getDeviceInfo());
            Log.e("Data Packet in JSON", "" + data.toString());


            GsonRequestPost<CreateUserResponse> myReq = new GsonRequestPost<>(
                    Request.Method.POST, Constants.userRegURL, CreateUserResponse.class, null,
                    successListener(), errorListener(), data);
           //
            int socketTimeout = 50000;//50 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            myReq.setRetryPolicy(policy);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "createReg");
            showProgressDialog(getActivity(), "Registration in progress..");


        } else {
            Toast.makeText(getActivity(), "Please Accept Terms & Conditions", Toast.LENGTH_SHORT).show();
        }
    }


    private Response.Listener<CreateUserResponse> successListener() {
        return new Response.Listener<CreateUserResponse>() {
            @Override
            public void onResponse(CreateUserResponse response) {

                Log.e("Create Response", "" + response.getCreateUserResult().toString());


                if (response != null)
                {

                    doPostOperation(response);

                }else
                {

                }
                dismissDialog();
            }
        };
    }
    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.i("Debug: MainActivity", error.toString());
                handleVolleyError(error);
                dismissDialog();
            }
        };
    }

    String str;


    private void doPostOperation(CreateUserResponse response)
    {


            //    Log.e("Create Response-->!ull",""+response.getCreateUserResult().getData().toString());
            if (response.getCreateUserResult().getSuccess().toString().equals("true")) {


                UserData d = response.getCreateUserResult().getData();
                if(d !=null) {
                    showProgressDialog(getActivity(), "Sync...");

                    Userdata_Bean bean = new Userdata_Bean();
                    bean.setUserId(d.getUserId());
                    bean.setImgurl("" + Constants.USER_PROFILE_PIC);
                    bean.setUserFName(d.getFirstName() + " " + d.getLastName());
                    bean.setContact1(d.getMobileNumber());
                    bean.setDob(d.getDOB());
                    bean.setGender(d.getGender());
                   // bean.setLanguage(d.getLanguage());
                    bean.setMail(d.getEmail());
                    if (d.getPassword() != null) {
                        bean.setPassword(d.getPassword());
                    } else {
                        bean.setPassword("");
                    }

                    if (!(d.getFacebookId() ==null)&& !"".equals(d.getFacebookId())) {
                        bean.setSocial_id(d.getFacebookId());
                        bean.setSocialtype("fb");
                    } else if (!(d.getGoogleId()==null)&& !"".equals(d.getGoogleId())) {
                        bean.setSocial_id(d.getGoogleId());
                        bean.setSocialtype("gplus");
                    } else {
                        bean.setSocial_id("");
                        bean.setSocialtype("normal");
                    }
                    // bean.setImgurl();
                   // bean.setIntrest(d.getInterestsTag());
                   // bean.setState(d.getState());

                    //bean.setCity(d.getCity());
                   // bean.setAddress(d.getAddress());
                   // bean.setDistrictName(d.getDistrictId());
                    bean.setLucky_No("" + d.getLuckyNumber());
                    if (d.getLuckyChart() != null && !d.getLuckyChart().equalsIgnoreCase("null") && !d.getLuckyChart().isEmpty()) {
                        bean.setLucky_Chart("" + d.getLuckyChart());
                        Constants.GLOBAL_U_LUCK_CHART = ""+d.getLuckyChart();
                        SharedPreferenceManager.getSharedInstance().setStringInPreferences("LUCKY_CHAT",d.getLuckyChart());
                    }else
                    {
                        bean.setLucky_Chart("");
                    }
                   // bean.setStatus()

                    Constants.GLOBAL_USER_NAME = d.getFirstName() + " " + d.getLastName();
                    Constants.GLOBAL_USER_ID = d.getUserId();
                    Constants.GLOBAL_U_LUCK_NO = d.getLuckyNumber();
                    Constants.GLOBAL_USER_CONT=d.getUserName();

                    Log.e("User id", "" + Constants.GLOBAL_USER_ID);

                    Log.e("Chart Data", "" + Constants.GLOBAL_U_LUCK_CHART);

                    controller.deleteUsers();
                    controller.doprocessData(bean);

                    checkUserStatus();

                    dismissDialog();


                    String strMessage="Thank you for registering with Saral Vaastu. May your life be filled with joy and happiness.";
                   // Toast.makeText(getActivity().getApplicationContext(), , Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();
                    b.putSerializable("user", bean);

                    Utils.showAlert(this, strName, strMessage,  b);


                }else
                {
                   // Constants.dismissDialog();
                    Toast.makeText(getActivity(), response.getCreateUserResult().getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
            else {
               // Constants.dismissDialog();
                Toast.makeText(getActivity(), response.getCreateUserResult().getMessage(), Toast.LENGTH_SHORT).show();
            }



    }

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
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }


    public void Is_Valid_Person_Name(EditText edt) throws NumberFormatException {
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Accept Alphabets Only.");
            strName = null;
        } else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
            edt.setError("Accept Alphabets Only.");
            strName = null;
        } else {
            strName = edt.getText().toString();
        }

    }

    public  void showProgressDialog(Activity ctx, String msg) {


        if(progressDialog ==null)
        {
            progressDialog = new ProgressDialog(ctx);
        }

        progressDialog.getLayoutInflater();
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public   void dismissDialog() {
        progressDialog.dismiss();
    }
    private class CustomTextWatcher implements TextWatcher {
        View view;

        AppCompatButton btnsubmit;
        Context context;


        public CustomTextWatcher(View txt, AppCompatButton btn, Context ctx) {
            this.view = txt;
            this.context = ctx;
            this.btnsubmit = btn;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            btnsubmit.setBackgroundColor(getResources().getColor(R.color.btn_background));
            btnsubmit.setTextColor(getResources().getColor(R.color.textColor));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            strName = name.getEditText().getText().toString().trim();
            strContact = mobile.getEditText().getText().toString().trim();
            strDOB = dob.getEditText().getText().toString().trim();
            strEmail = email.getEditText().getText().toString().trim();
            if (!strName.matches("[a-zA-Z ]+")) {
                Toast.makeText(getActivity(), "Accept Alphabets Only.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Is_Valid_Person_Name(name.getEditText());

            if (strName.length() >= 49) {
                Toast.makeText(getActivity(), "Can not enter more than 50 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!strName.isEmpty() && !strContact.isEmpty() && !strDOB.isEmpty() && !strEmail.isEmpty()) {
                btnsubmit.setBackgroundColor(getResources().getColor(R.color.blue));
                btnsubmit.setTextColor(getResources().getColor(R.color.text_white));

            } else {
                //Toast.makeText(getActivity()," Ohhhhh",Toast.LENGTH_SHORT).show();
            }
        }
    }


/*  Date picker */

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //myCalendar.add(Calendar.DATE, 1);

            String strD = getFormatDate(myCalendar.getTime());
            /*validate dob */
            Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -14);
            if (minAdultAge.before(userAge)) {
                Toast.makeText(getActivity(), "Invalid DOB. You must be above 14 years", Toast.LENGTH_SHORT).show();
            } else {
                dob.getEditText().setText(strD);
                Log.e("Date", " DOB tr minAdultAge " + minAdultAge);
                Log.e("Date", " DOB tr user " + userAge);
            }

        }
    };


    private String getFormatDate(Date d) {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        myFormat = sdf.format(d);
        return myFormat;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


            if (progressDialog != null) {
                progressDialog.dismiss();
               // Constants.progressDialog = null;
            }


     //   Constants.dismissDialog();
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll("createReg");
    }


    public void checkUserStatus() {
        boolean status;
        status = controller.isUserExist();

        if (status == true) {
            editor.putBoolean(Constants.PREFS_KEY, true);
            if(!Constants.GLOBAL_USER_ID.equals(""))
            {
                editor.putString(Constants.PREFS_USER_ID, Constants.GLOBAL_USER_ID);
            }

            editor.commit();
        } else if (status == false) {
            editor.putBoolean(Constants.PREFS_KEY, false);
            editor.commit();
        }
    }

}
