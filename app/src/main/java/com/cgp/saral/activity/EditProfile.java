package com.cgp.saral.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.cgp.saral.adapter.StateDistAdapter;
import com.cgp.saral.adapter.UserProfileAdapter;
import com.cgp.saral.customviews.MultiSpinner;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.Language;
import com.cgp.saral.model.LocationItems;
import com.cgp.saral.model.UpdateResponse;
import com.cgp.saral.model.UpdateUserResult;
import com.cgp.saral.model.UserData;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    UserProfileAdapter langAdapter = null, genderAdapter = null, stateAdapter = null, cityAdapter = null;
    StateDistAdapter distAdapter = null;
    TextInputLayout name, mobile, email, dob, address,genderTI;
    CheckBox termondition;
    ImageView back;
    EditText contact;
    AppCompatButton submit;
    TextView term;
    MaterialSpinner  state, language, spinnerdist;
    MultiSpinner intrest;
    Calendar myCalendar = Calendar.getInstance();
    List<Userdata_Bean> userdata;
    HashMap<Integer, String> langMap = new HashMap<>();
    HashMap<Integer, String> gendremap = new HashMap<>();
    HashMap<Integer, String> statemap = new HashMap<>();
    HashMap<Integer, String> distMap = new HashMap<>();

    ImageView usrPic;
    ImageView ivEdit;
    int statePoss = 0;
    int distPoss = 0;
    int languagePos = 0;
    int genderPoss=0;

    ProgressDialog progressDialog;
    ArrayList<String> langData = new ArrayList<>();
    ArrayList<String> gendreData = new ArrayList<>();
    ArrayList<String> stateData = new ArrayList<>();
    ArrayList<String> distData = new ArrayList<>();

    Intent intent;
    String strLanguage, strIntrest, strState, strDist, strAddress;
    Userdata_Bean newbean, bean;
    DataController controller;
    int langPos, statePos, genPos, distPos;
    Bundle bundle = new Bundle();
    SharedPreferences preferences;
    SharedPreferences.Editor lngeditor, stateedit, genedit, distEdit;

    String intrestides, strGender;
    int[] strspl;
    Context ctx;
  //  private MultiSpinner spinner;

    ArrayList<LocationItems> stateL;

    boolean statusStateDist;
  //  String strGenderD="";

    private final int SELECT_PHOTO = 1;
    String localProfileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_edit);
        intent = getIntent();
        preferences = getSharedPreferences(Constants.PREFS_NAME_EDIT, Context.MODE_PRIVATE);
        lngeditor = preferences.edit();
        stateedit = preferences.edit();
        genedit = preferences.edit();
        distEdit = preferences.edit();
        recievePrefrences();
        controller = DataController.getsInstance(this);

        // userdata= (List<Userdata_Bean>) intent.getExtras().getSerializable("user");

        userdata = controller.getAllData();
        if (null != userdata) {
            bean = userdata.get(0);
        } else {
            Log.e("Edit Profile ", "bean null");
        }
        inIt();
        variableInit();
        setAdapter();
    }

    public void recievePrefrences() {
        langPos = preferences.getInt(Constants.PREFS_LANG_POSITION, 0);
        genPos = preferences.getInt(Constants.PREFS_GENDER_POSITION, 0);
        statePos = preferences.getInt(Constants.PREFS_STATE_POSITION, 0);
        distPos = preferences.getInt(Constants.PREFS_DIST_POSITION, 0);
        Log.e("Position ", " Lang pos " + langPos);
        Log.e("Position ", " Gender pos " + genPos);
        Log.e("Position ", " State pos " + statePos);
        Log.e("Position ", " Dist pos " + distPos);


    }

    public void inIt() {
        ctx = getApplicationContext();
        submit = (AppCompatButton) findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);

        spinnerdist = (MaterialSpinner) findViewById(R.id.spinnerdistE);
        address = (TextInputLayout) findViewById(R.id.tv_addressE);

        termondition = (CheckBox) findViewById(R.id.tv_termcondition);
        termondition.setVisibility(View.INVISIBLE);
        name = (TextInputLayout) findViewById(R.id.tv_usernameE);
        back = (ImageView) findViewById(R.id.iv_back);
        mobile = (TextInputLayout) findViewById(R.id.tv_regsecMobileE);
        mobile.getEditText().setKeyListener(null);
        email = (TextInputLayout) findViewById(R.id.tv_regsecMailE);
        dob = (TextInputLayout) findViewById(R.id.tv_dobE);
        language = (MaterialSpinner) findViewById(R.id.spinnerlanguageE);
      //  spinner = (MultiSpinner) findViewById(R.id.spinnerintrestE);
        state = (MaterialSpinner) findViewById(R.id.spinnerstateE);
        intrest = (MultiSpinner) findViewById(R.id.spinnerintrestE);
        genderTI = (TextInputLayout) findViewById(R.id.gender);
        term = (TextView) findViewById(R.id.term);
        term.setVisibility(View.INVISIBLE);


        // gender.setOnItemClickListener(null);

        usrPic = (ImageView) findViewById(R.id.iv_userpro);
        ivEdit = (ImageView) findViewById(R.id.iv_edit);
        if (bean != null) {

            if (bean.getImgurl()!=null){
                Picasso.with(ctx).load(bean.getImgurl())
                        .placeholder(R.drawable.ic_dp_grey).error(R.drawable.ic_dp_grey).into(usrPic);
            }else{
                usrPic.setImageResource(R.drawable.ic_dp_grey);
            }
            address.getEditText().setText(bean.getAddress());
            name.getEditText().setText(bean.getUserFName());
            name.getEditText().setOnKeyListener(null);
            name.getEditText().setEnabled(false);
            name.getEditText().setFocusable(false);
            mobile.getEditText().setText(bean.getContact1());
            mobile.getEditText().setOnKeyListener(null);
            mobile.getEditText().setEnabled(false);
            mobile.getEditText().setFocusable(false);
            email.getEditText().setText(bean.getMail());
            email.getEditText().setOnKeyListener(null);
            email.getEditText().setEnabled(false);
            email.getEditText().setFocusable(false);
            dob.getEditText().setText(bean.getDob().substring(0, 10));
            dob.getEditText().setOnKeyListener(null);
            dob.getEditText().setEnabled(false);
            dob.getEditText().setFocusable(false);
            genderTI.getEditText().setEnabled(false);
            genderTI.getEditText().setOnKeyListener(null);

            genderTI.getEditText().setFocusable(false);
            try {
              String  strGenderD = bean.getGender();
                Log.e("gender",""+strGenderD);
                if (strGenderD.equals("200001")) {
                    // gender.setFloatingLabelText("Male");
                    genderTI.getEditText().setText("Male");
                    //genPos=1;
                } else {
                    genderTI.getEditText().setText("Female");
                    // genPos=0;
                }
            }catch (Exception ex)
            {
                Log.e("gender",""+ex.toString());
            }

            String faver = bean.getIntrest();

            Log.e("Intrested Tag", "" + faver);
            strspl = new int[faver.length()];
            for (int i = 0; i < faver.length(); i++) {
                strspl[i] = Character.getNumericValue(faver.charAt(i))-1;
            }


            for (int i = 0; i < strspl.length; i++) {
                Log.e("Interest Field", "" + strspl[i]);
            }
            //    strspl = Arrays.copyOfRange(strspl, 0, strspl.length);
            intrest.setItems(Constants.intrestData);
            intrest.setSelection(strspl);


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Launching Action pic for selecting photo*/
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }
            });

        }
    }

    /* Catching selected photo from exter intent*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        localProfileImg = imageUri.toString();
                       // usr_pic.setImageBitmap(selectedImage);
                        Picasso.with(ctx).load(localProfileImg)
                                .placeholder(R.drawable.ic_dp_grey).error(R.drawable.ic_dp_grey).into(usrPic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public void variableInit() {

        ArrayList<Language> langs = controller.languageList();

        for (Language l : langs) {
            langMap.put(Integer.valueOf(l.getId()), l.getName());
        }
        HashMap<Integer, String> langMapL=Utils.sortByValues(langMap);

        langMap.clear();
        langMap=langMapL;
        langData = new ArrayList<>(langMap.values());



        stateL = controller.locationList(Constants.LOCATION_STATE);
        for (LocationItems ll : stateL) {

            statemap.put(Integer.valueOf(ll.getId()), ll.getName());
        }

        HashMap<Integer, String> statemapL=Utils.sortByValues(statemap);

        statemap.clear();
        statemap=statemapL;
        stateData = new ArrayList<>(statemap.values());
        updateDist(bean.getState());




    }

    private void updateDist(String strState) {
        Log.e("State for district", "" + strState + "   --> " + stateL.size());

        ArrayList<LocationItems> distFiltered = controller.locationListFiltered(Constants.LOCATION_DIST, strState);

        if (distFiltered.size() <= 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), " No District for this State ", Toast.LENGTH_LONG).show();
                }
            });

            return;
        }

        Log.e(" District Size", "" + distFiltered.size());

       // Log.e(" District Size", "" + distFiltered.size());

        if(distMap.size()>0)
        {
            distMap.clear();
        }


        for (LocationItems ll : distFiltered) {
            distMap.put(Integer.valueOf(ll.getId()), ll.getName());
        }

        strDist = "";

        HashMap<Integer, String> distMapL=Utils.sortByValues(distMap);

        distMap.clear();
        distMap=distMapL;

        distData = new ArrayList<>(distMap.values());
        // distAdapter.notifyDataSetChanged();

        Log.e("  ---> ", "  " + distMap.size());
        distAdapter = new StateDistAdapter(getApplicationContext(), distMap);
        spinnerdist.setAdapter(distAdapter);
        if(distPos <distMap.size()) {
            spinnerdist.setSelection(distPos + 1);
        }else
        {
            spinnerdist.setSelection(1);
        }
        spinnerdist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == -1) && distData.size() >= position) {
                    //Log.e("onItemSelected", "position hash" + langData.get(position).toString());
                    strDist = Constants.getdata(distData.get(position).toString(), distMap);

                    distPoss = position;


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

    }

    public void setAdapter() {


        // spinner.setItems(Constants.intrestData);
        strIntrest = intrest.getSelectedItemsAsString();

        langAdapter = new UserProfileAdapter(EditProfile.this, 1, langMap);
        language.setAdapter(langAdapter);
        language.setSelection(langPos + 1);
        Log.e("Adapter Size ", " Adapter pos " + langAdapter.getCount());
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!(position == -1) && langData.size() >= position) {

                    Log.e("onItemSelected", "position hash" + langData.get(position).toString());
                    strLanguage = Constants.getdata(langData.get(position).toString(), langMap);

                    languagePos = position;
                    Log.e("onItemSelected", "strLanguage  " + strLanguage);
                } else {
                    Log.e("onItemSelected", "return  mt");
                    // language.setSelection(Integer.parseInt(ides));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  genderAdapter = new UserProfileAdapter(EditProfile.this, 1, gendremap);
        gender.setAdapter(genderAdapter);
        gender.setSelection(genPos);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!(position == -1) && gendreData.size() >= position) {


                    strGender = Constants.getdata(gendreData.get(position).toString(), gendremap);
                    genderPoss = position;

                    Log.e("onItemSelected", "strLanguage  " + strLanguage + " gender " + strGender + " State " + strState);
                } else {
                    //gender.setSelection(Integer.parseInt(ides));
                    Log.e("onItemSelected", "return  mt");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        stateAdapter = new UserProfileAdapter(EditProfile.this, 1, statemap);
        state.setAdapter(stateAdapter);
        state.setSelection(statePos + 1);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!(position == -1) && stateData.size() >= position) {

                    strState = Constants.getdata(stateData.get(position).toString(), statemap);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // distPos=0;
                            updateDist(strState);
                        }
                    });
                    statePoss = position;

                    Log.e("onItemSelected", "strLanguage  " + strLanguage + " State " + strState);
                } else {

                    Log.e("onItemSelected", "return  mt");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


     /*   distAdapter = new StateDistAdapter(EditProfile.this, distMap);
        spinnerdist.setAdapter(distAdapter);
        spinnerdist.setSelection(distPos + 1);
        spinnerdist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!(position == -1) && distData.size() >= position) {

                    strDist = Constants.getdata(distData.get(position).toString(), distMap);
                    distEdit.putInt(Constants.PREFS_DIST_POSITION, position);
                    distEdit.commit();
                    Log.e("onItemSelected", " Dist " + strDist);
                } else {

                    Log.e("onItemSelected", "return  mt");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }


    @Override
    public void onClick(View v) {


        List<Integer> id = intrest.getSelectedIndices();
        intrestides = Utils.intrestValue(id);
        Log.e("Intrest data", " Selected Data intrest" + strIntrest);
        Log.e("Intrest data", " Selected Data intrest id length " + id.size() + " and data " + intrestides);


        strIntrest = intrest.getSelectedItemsAsString();
        // strIntrest=tv_intrestlist.getText().toString().trim();

        strAddress = address.getEditText().getText().toString().trim();


        newbean = new Userdata_Bean();

        newbean.setUserFName(bean.getUserFName());
        newbean.setContact1(bean.getContact1());
        newbean.setMail(bean.getMail());
        newbean.setDob(bean.getDob());
        newbean.setGender(bean.getGender());
        newbean.setLanguage(strLanguage);
        newbean.setState(strState);
        newbean.setIntrest(strIntrest);
        newbean.setAddress(strAddress);
        newbean.setDistrictName(strDist);


        if (strState.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please select your state", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strState.isEmpty() && strDist.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please select your district", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 9);
            return;
        }else if (!strState.isEmpty() && !strDist.isEmpty()&& !isSateDist(strState,strDist)) {

            Toast.makeText(EditProfile.this, "Selected district is not in Selected State", Toast.LENGTH_LONG).show();
            return;

        } else if (!strState.isEmpty() && !strDist.isEmpty() && strAddress.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please enter your Address", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation " + 9);
            return;
        } else if (!strState.isEmpty() && !strDist.isEmpty() && !strAddress.isEmpty() && strLanguage.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please select preffered language", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strLanguage.isEmpty() && !strState.isEmpty() && !strDist.isEmpty() && !strAddress.isEmpty() && intrestides.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please select your Interest", Toast.LENGTH_SHORT).show();
            Log.e("NewBean", "Validation " + 10);
            return;
        } else {
            processData();
        }
    }


    private boolean isSateDist(final String strState, final String strDist)
    {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<LocationItems> distFiltered = controller.locationListFiltered(Constants.LOCATION_DIST, strState);
                if(distFiltered.size()>0)
                {
                    for(LocationItems l:distFiltered)
                    {
                        if(!l.getParentId().equals(strState))
                        {
                            Toast.makeText(EditProfile.this, "Selected district is not in Selected State", Toast.LENGTH_SHORT).show();
                            //Log.e("NewBean", "Validation " + 9);
                            statusStateDist =false;
                        }else {
                            statusStateDist=true;
                        }
                    }
                }else
                {
                    Toast.makeText(EditProfile.this, "Selected district is not in Selected State", Toast.LENGTH_SHORT).show();
                    //Log.e("NewBean", "Validation " + 9);
                    statusStateDist=false;
                }
            }
        });
        return statusStateDist;
    }

    private void processData() {

        if (!Utils.isConnectedToInternet(this)) {
            Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
            return;

        }

        Date date = new Date();

        JsonObject data = new JsonObject();
        JsonObject dataFromBean = new JsonObject();

        dataFromBean.addProperty("UserId", bean.getUserId());
        dataFromBean.addProperty("CreatedDate", Constants.getFormattedDate(getFormatDate(date)));

        dataFromBean.addProperty("UserName", "" + newbean.getContact1());
        dataFromBean.addProperty("FirstName", "" + newbean.getUserFName());

        dataFromBean.addProperty("LastName", "");
        dataFromBean.addProperty("DOB", newbean.getDob());


        dataFromBean.addProperty("MobileNumber", "" + newbean.getContact1());
        dataFromBean.addProperty("Language", "" + newbean.getLanguage());

        dataFromBean.addProperty("Gender", "" + newbean.getGender());
        dataFromBean.addProperty("State", "" + newbean.getState());
        // dataFromBean.addProperty("State", "7");

        // dataFromBean.addProperty("City", "Mumbai");
        dataFromBean.addProperty("Address", strAddress);
        dataFromBean.addProperty("DistrictId", strDist);
        dataFromBean.addProperty("Email", "" + newbean.getMail());

        dataFromBean.addProperty("Password", "" + bean.getPassword());
        //dataFromBean.addProperty("CreatedBy", "9");
        dataFromBean.addProperty("Status", "2");

        Log.e("Social Type", "" + bean.getSocialtype());
        try {
            if (!bean.getSocialtype().equalsIgnoreCase("null")) {
                if (bean.getSocialtype().equalsIgnoreCase("fb")) {
                    dataFromBean.addProperty("FacebookId", "" + bean.getSocial_id());
                } else if (bean.getSocialtype().equalsIgnoreCase("gplus")) {
                    dataFromBean.addProperty("GoogleId", "" + bean.getSocial_id());
                } else {
                    dataFromBean.addProperty("FacebookId", "");
                    dataFromBean.addProperty("GoogleId", "");
                }
            } else {
                dataFromBean.addProperty("FacebookId", "");
                dataFromBean.addProperty("GoogleId", "");
            }
        } catch (Exception ex) {
            dataFromBean.addProperty("FacebookId", "");
            dataFromBean.addProperty("GoogleId", "");
        }

        dataFromBean.addProperty("RoleId", "3");
        if(localProfileImg != null){
            dataFromBean.addProperty("PhotoPath", localProfileImg);
        }else {
            dataFromBean.addProperty("PhotoPath", bean.getImgurl());
        }
        dataFromBean.addProperty("InterestsTag", "" + intrestides);

        data.addProperty("modifiedBy", bean.getUserId());
        data.add("userInfo", dataFromBean);
        data.add("sourceInfo", Constants.getDeviceInfo());

        Log.e("Data Packet in JSON", "" + data.toString());


        GsonRequestPost<UpdateResponse> myReq = new GsonRequestPost<>(
                Request.Method.POST, Constants.userUpdateURL, UpdateResponse.class, null,
                successListener(), errorListener(), data);

        VolleySingleton.getInstance(ctx).addToRequestQueue(myReq, "updateUser");
        showProgressDialog(this, "User Update in progress..");


    }


    private Response.Listener<UpdateResponse> successListener() {
        return new Response.Listener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse response) {

                Log.e("User Update Resp", "" + response);


                if (response != null) {
                    UpdateUserResult result = response.getUpdateUserResult();


                    UserData data = result.getData();

                    if (data != null) {


                        if (result.getSuccess().toString().equals("true")) {


                            UserData d = response.getUpdateUserResult().getData();

                            Userdata_Bean bean = new Userdata_Bean();
                            bean.setUserId(d.getUserId());
                            bean.setUserFName(d.getFirstName() + " " + d.getLastName());
                            bean.setContact1(d.getUserName());
                            bean.setDob(d.getDOB());
                            bean.setGender(d.getGender());
                            bean.setLanguage(d.getLanguage());
                            bean.setMail(d.getEmail());
                            //bean.setPassword(d.getPassword());

                            if (d.getPassword() != null) {
                                bean.setPassword(d.getPassword());
                            } else {
                                bean.setPassword("");
                            }
                            if (d.getPhotoPath() != null) {


                                if (!d.getPhotoPath().equals(""))
                                    bean.setImgurl(d.getPhotoPath());
                            }

                           /* String fbId = d.getFacebookId();
                            String gId = d.getGoogleId();
                            Log.e("Facebook Id", "" + fbId + "  google id " + gId);
                            if (!("null".equalsIgnoreCase(fbId))) {
                                bean.setSocial_id(fbId);
                                bean.setSocialtype("fb");
                            } else if (!("null".equalsIgnoreCase(gId))) {
                                bean.setSocial_id(gId);
                                bean.setSocialtype("gplus");
                            } else {
                                bean.setSocial_id("");
                                bean.setSocialtype("normal");
                            }*/
                            if (!("null".equalsIgnoreCase(""+d.getFacebookId()))) {
                                bean.setSocial_id(d.getFacebookId()+"");
                                bean.setSocialtype("fb");
                            } else if (!("null".equalsIgnoreCase(""+d.getGoogleId()))) {
                                bean.setSocial_id(d.getGoogleId()+"");
                                bean.setSocialtype("gplus");
                            } else {
                                bean.setSocial_id("");
                                bean.setSocialtype("normal");
                            }

                            // bean.setImgurl();
                            bean.setIntrest(d.getInterestsTag());
                            bean.setState(d.getState());

                            bean.setCity(d.getCity());
                            bean.setAddress(d.getAddress());
                            bean.setDistrictName(d.getDistrictId());
                            bean.setLucky_No(d.getLuckyNumber());
                            bean.setLucky_Chart(d.getLuckyChart());

                            controller.deleteUsers();
                            controller.doprocessData(bean);

                            Constants.GLOBAL_USER_NAME = d.getFirstName() + " " + d.getLastName();
                            Constants.GLOBAL_USER_ID = d.getUserId();
                            Constants.GLOBAL_U_LUCK_NO = d.getLuckyNumber();
                            Constants.GLOBAL_USER_CONT = d.getUserName();

                            distEdit.putInt(Constants.PREFS_DIST_POSITION, distPoss);
                            distEdit.commit();

                            lngeditor.putInt(Constants.PREFS_LANG_POSITION, languagePos);
                            lngeditor.commit();

                            stateedit.putInt(Constants.PREFS_STATE_POSITION, statePoss);
                            stateedit.commit();
                            genedit.putInt(Constants.PREFS_GENDER_POSITION, genderPoss);
                            genedit.commit();
                            Log.e("Chart Data", "" + d.getLuckyChart());
                            if (d.getLuckyChart() != null && !d.getLuckyChart().equalsIgnoreCase("null")) {
                                Constants.GLOBAL_U_LUCK_CHART = d.getLuckyChart();
                            }

                            dismissDialog();
                            startActivity(new Intent(EditProfile.this, MainActivity.class));
                            finish();


                        } else {
                            dismissDialog();
                            Toast.makeText(EditProfile.this, "Opps !!! " + result.getMessage() + " Please contact the Administrator", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dismissDialog();
                        Toast.makeText(EditProfile.this, "Opps !!! " + result.getMessage() + " Please contact the Administrator", Toast.LENGTH_SHORT).show();

                    }


                }


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
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }


    private String getFormatDate(Date d) {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        myFormat = sdf.format(d);
        return myFormat;

    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            //Constants.progressDialog = null;
        }
    }
}
