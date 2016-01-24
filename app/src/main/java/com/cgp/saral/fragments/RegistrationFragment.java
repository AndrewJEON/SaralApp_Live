package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.cgp.saral.activity.MainActivity;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.LocationItems;
import com.cgp.saral.model.LoginResponse;
import com.cgp.saral.model.LoginResult;
import com.cgp.saral.model.UserData;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.cgp.saral.tuarguide.Showcase_guide;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {
    View view;
    FragmentManager fragmentManager;
    String strcontact = "", strpass = "", strcon_pass = "";
    DataController dataController;
    @Bind(R.id.btn_next1)
    AppCompatButton next;
    //TextInputLayout confp;
    TextInputLayout mobile_no, password;
    TextInputLayout conf_passsord;
    EditText confpass;
    TextView login, havean_ac;
    ImageView iv_confpass;
    Bundle bundle = new Bundle();
    Bundle arg = new Bundle();
    TextView tv;
    String id;
    Typeface typeface;

    // DataController controller;
    Userdata_Bean bean;

    SharedPreferences preferences;
    SharedPreferences.Editor editor,state,dist;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        setRetainInstance(true);


        dataController =DataController.getsInstance(getActivity());
        // dataController.createDb();
        //typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto/Roboto-Medium.ttf");
        login = (TextView) view.findViewById(R.id.tv_login);
        havean_ac = (TextView) view.findViewById(R.id.haveanac);
        mobile_no = (TextInputLayout) view.findViewById(R.id.tv_lableemail);
        tv = (TextView) view.findViewById(R.id.tv_test);
        password = (TextInputLayout) view.findViewById(R.id.tv_lablepass);
        iv_confpass = (ImageView) view.findViewById(R.id.tv_forgatepass);
        confpass = (EditText) view.findViewById(R.id.et_confpassword);
        conf_passsord = (TextInputLayout) view.findViewById(R.id.tv2);
        next = (AppCompatButton) view.findViewById(R.id.btn_next1);

      //  login.setTypeface(typeface);
       // next.setTypeface(typeface);

        arg = getArguments();
        id = arg.getString(Constants.FRAGMENT_ID);
        Log.e("oncreateview", " id " + id);
        if (id.equalsIgnoreCase("1")) {
            confpass.setVisibility(View.VISIBLE);
            iv_confpass.setVisibility(View.VISIBLE);
            conf_passsord.setVisibility(View.VISIBLE);
            havean_ac.setVisibility(View.INVISIBLE);
            login.setVisibility(View.VISIBLE);
            login.setText("Already have an account, Login");
            next.setText("NEXT");
        } else if (id.equalsIgnoreCase("2")) {
            confpass.setVisibility(View.INVISIBLE);
            iv_confpass.setVisibility(View.INVISIBLE);
            conf_passsord.setVisibility(View.INVISIBLE);
            havean_ac.setVisibility(View.VISIBLE);
            havean_ac.setText("Forgot Password ?");
            login.setVisibility(View.INVISIBLE);
            next.setText("LOGIN");
        }
        next.setOnClickListener(this);
        login.setOnClickListener(this);
        havean_ac.setOnClickListener(this);
        mobile_no.getEditText().addTextChangedListener(new CustomTextWatcher(mobile_no, next, id));
        password.getEditText().addTextChangedListener(new CustomTextWatcher(password, next, id));
        conf_passsord.getEditText().addTextChangedListener(new CustomTextWatcher(conf_passsord, next, id));

        preferences = getActivity().getSharedPreferences(Constants.PREFS_NAME_EDIT, Context.MODE_PRIVATE);

        editor = preferences.edit();
        state = preferences.edit();

        dist = preferences.edit();


        password.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.action_login) {
                    buttonClick();
                    return true;
                }
                return false;
            }
        });
        return view;
    }


    public void startTransaction(Fragment fragment, Userdata_Bean bean) {
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        bundle.putSerializable("userdata", bean);
        fragment.setArguments(bundle);
        transaction.replace(R.id.homeContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validCellPhone(String number) {
        if (number.length() != 10 && number.length() < 10) {
            Toast.makeText(getActivity(), "Please enter 10 digit mobile no", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    public boolean validPassword(String pass) {
        if (pass.length() > 5) {

            return true;
        } else {

            //Toast.makeText(getActivity(), "Please enter valid password", Toast.LENGTH_SHORT).show();
             Toast.makeText(getActivity(),"Password can't be less than 6 characters",Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        strpass = password.getEditText().getText().toString();
        strcon_pass = conf_passsord.getEditText().getText().toString();
        strcontact = mobile_no.getEditText().getText().toString();



        if (v == login) {

            bundle.putString(Constants.FRAGMENT_ID, "2");
            Fragment fragment = new LoginFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, fragment,"login").
                    addToBackStack(null).commit();
        }
        if (v == havean_ac) {
            Fragment fragment = new ForgotPassword_Options();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, fragment,"forgotPass").
                    addToBackStack(null).commit();
        } else if (v == next) {


            buttonClick();
        }


    }

    public void buttonClick() {
        String btnName = next.getText().toString().trim();
        if (btnName.equalsIgnoreCase("NEXT")) {
            if(!strpass.equals(strcon_pass))
            {
                Toast.makeText(getActivity(),"Password mismatch",Toast.LENGTH_SHORT).show();
                return;
            }
            insertData();
        } else if (btnName.equalsIgnoreCase("LOGIN")) {
            Log.e(" Login", " step 1");

            if (strcontact.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter 10 digit mobile no.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!strcontact.isEmpty() && strcontact.length() < 10) {
                Toast.makeText(getActivity(), "Mobile No Can't be less than 10 digits", Toast.LENGTH_SHORT).show();
                return;
            } else if (!strcontact.isEmpty() && strcontact.length() == 10 && strcontact.startsWith("0")) {
                Toast.makeText(getActivity(), "oops !!! Mobile No Can't be started with 0 :-)", Toast.LENGTH_SHORT).show();
                return;
            } else if (!strcontact.isEmpty() && strpass.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }else if (!strcontact.isEmpty() && !strpass.isEmpty()) {

                Userdata_Bean bean = dataController.login(strcontact, "normal", strpass);
                if (bean != null) {



                    if (bean.getContact1().equalsIgnoreCase(strcontact) && bean.getPassword().equalsIgnoreCase(strpass)) {

                        Constants.GLOBAL_USER_NAME = bean.getUserFName() + " " + bean.getUserLName();
                        Constants.GLOBAL_USER_ID = bean.getUserId();
                        Constants.GLOBAL_U_LUCK_NO = bean.getLucky_No();
                        Constants.GLOBAL_USER_CONT = bean.getContact1();

                        updateUserStatus(bean);

                        Log.e("Chart Data", "" + bean.getLucky_Chart());
                        if (bean.getLucky_Chart() != null && !bean.getLucky_Chart().equalsIgnoreCase("null")) {
                            Constants.GLOBAL_U_LUCK_CHART = bean.getLucky_Chart();
                        }
                        ArrayList<LocationItems> loc_st= dataController.locationList(Constants.LOCATION_STATE);
                        String parentid="";
                        Log.e(" RegFRAG"," LOG FRAGGG "+loc_st.size());
                        Log.e(" RegFRAG"," LOG FRAGGG "+bean.getState());
                        String iid=bean.getState();

                        for (int i=0;i<loc_st.size();i++)
                        {
                            if (iid.equalsIgnoreCase(loc_st.get(i).getId()))
                            {
                                Log.e(" RegFRAG"," LOG FRAGGG "+i);
                                state.putInt(Constants.PREFS_STATE_POSITION, i);
                                parentid=""+iid;
                                state.commit();

                            }

                        }
                        Log.e(" RegFRAG"," LOG FRAGGG "+Constants.PREFS_STATE_POSITION);

                        ArrayList<LocationItems> loc_dist= dataController.locationListFiltered(Constants.LOCATION_DIST, parentid);
                        String dist1=bean.getDistrictName();
                        for (int i=0;i<loc_dist.size();i++)
                        {
                            if (dist1.equalsIgnoreCase(loc_dist.get(i).getId()))
                            {
                                Log.e(" RegFRAG"," LOG FRAGGG dist "+i);
                                dist.putInt(Constants.PREFS_DIST_POSITION, i);
                                dist.commit();

                            }

                        }
                        Log.e(" RegFRAG"," LOG FRAGGG "+Constants.PREFS_DIST_POSITION);

                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        Toast.makeText(getActivity(), "UserName or Password Wrong", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    JsonObject data = new JsonObject();
                    JsonObject dataFromBean = new JsonObject();

                    dataFromBean.addProperty("UserName", strcontact);
                    dataFromBean.addProperty("Password", strpass);

                    dataFromBean.addProperty("EmailId", "");
                    dataFromBean.addProperty("NewPassword", "");


                    data.add("loginData", dataFromBean);
                    data.add("source", Constants.getDeviceInfo());
                    Log.e("Data Packet in JSON", "" + data.toString());


                    GsonRequestPost<LoginResponse> myReq = new GsonRequestPost<>(
                            Request.Method.POST, Constants.userLogin, LoginResponse.class, null,
                            successListenerLogin(), errorListener(), data);
                    showProgressDialog(getActivity(), "Authorizing User...");
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "createReg");
                }
                Log.e(" Login", " bean is null step 7");

                // Toast.makeText(getActivity(),"Invalid ID or Password. Please try again",Toast.LENGTH_SHORT).show();
            } else {
                Log.e(" Login", " step 8");
                Toast.makeText(getActivity(), "Enter details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUserStatus(Userdata_Bean bean) {

        long id;
        id = dataController.updateStausofExistingU(1,bean.getContact1(),bean.getUserId());

        if (id >0) {
            editor.putBoolean(Constants.PREFS_KEY, true);
            if(Constants.GLOBAL_USER_ID !="")
            {
                editor.putString(Constants.PREFS_USER_ID, bean.getUserId());
            }

            editor.commit();
        } else if (id <0) {
            editor.putBoolean(Constants.PREFS_KEY, false);
            editor.commit();
        }


    }

    public void insertData() {
        if (strcontact.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter mobile no.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strcontact.isEmpty() && strcontact.startsWith("0")) {

            Toast.makeText(getActivity(), "Mobile Number Can't be started from 0 :-) ", Toast.LENGTH_SHORT).show();
            return;


        } else if (!strcontact.isEmpty() && !strcontact.startsWith("0") && !validCellPhone(strcontact)) {

            Toast.makeText(getActivity(), "Please enter valid mobile no.", Toast.LENGTH_SHORT).show();
            return;


        } else if (!strcontact.isEmpty() && strpass.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }/* else if (!strcontact.isEmpty() && !strpass.isEmpty() && strpass.length() < 20) {
            Toast.makeText(getActivity(), "Password must be more than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }*/ else if (!strcontact.isEmpty() && !strpass.isEmpty() && strcon_pass.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter confirm password", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strcontact.isEmpty() && (!strpass.isEmpty() || !strcon_pass.isEmpty())) {
            boolean status = validPassword(strpass);
            if (status == true) {

                JsonObject data = new JsonObject();
                JsonObject dataFromBean = new JsonObject();

                dataFromBean.addProperty("UserName", strcontact);
                dataFromBean.addProperty("Password", "");

                dataFromBean.addProperty("EmailId", "");
                dataFromBean.addProperty("NewPassword", "");


                data.add("loginData", dataFromBean);
                data.add("source", Constants.getDeviceInfo());
                Log.e("Data Packet in JSON", "" + data.toString());


                GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                        Request.Method.POST, Constants.userIsRegisteredUserURL, JsonObject.class, null,
                        successListener(), errorListener(), data);
                showProgressDialog(getActivity(), "Checking Availability...");
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "createReg");

            }

        }


    }


    private Response.Listener<JsonObject> successListener() {
        return new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                JsonObject jsonObject, jsonObjectLogin;
                if (response != null) {
                    jsonObject = response.getAsJsonObject("IsRegisteredUserResult");
                    if (jsonObject != null) {
                        Log.e("Response Packet", "Data Size jsonObject: " + jsonObject.toString());

                        JsonElement jG = jsonObject.get("Data");
                        JsonElement jMessage = jsonObject.get("Message");

                        Log.e("Response Packet", "Message: " + jMessage.toString());
                        dismissDialog();
                        if (jG.toString().equalsIgnoreCase("false")) {

                            if (!jMessage.toString().contains("Invalid")) {
                                Log.e("Response Packet", "Message: " + jMessage.toString());
                                check();
                            } else {
                                Toast.makeText(getActivity(), "" + jMessage.toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }


                            Log.e("Response Packet", "Data Size gj: " + jG.toString());
                        } else {

                            Toast.makeText(getActivity(), "Contact no. already existed, it seems you are returning back, please login... ", Toast.LENGTH_SHORT).show();

                            Log.e("Response Packet", "Data Size : " + response.toString());
                        }
                    }


                }


            }
        };
    }

    private Response.Listener<LoginResponse> successListenerLogin() {
        return new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {

                if (response != null) {
                    LoginResult result = response.getLoginResult();

                    if (result != null) {
                        if (result.getSuccess().toString().equals("true")) {


                            UserData d = response.getLoginResult().getData();

                            Userdata_Bean bean = new Userdata_Bean();
                            if(null !=d.getUserId())
                            {
                                bean.setUserId(d.getUserId());
                            }

                            bean.setUserFName(d.getFirstName() + " " + d.getLastName());
                            bean.setContact1(d.getUserName());
                            bean.setDob(d.getDOB());
                            bean.setGender(d.getGender());
                            bean.setLanguage(d.getLanguage());
                            bean.setMail(d.getEmail());
                            if (d.getPassword() != null) {
                                bean.setPassword(d.getPassword());
                            } else {
                                bean.setPassword("");
                            }

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
                            if(d.getPhotoPath() !=null)
                            {


                                if (!d.getPhotoPath().equals(""))
                                    bean.setImgurl(d.getPhotoPath());
                            }
                            bean.setIntrest(d.getInterestsTag());
                            bean.setState(d.getState());

                            //bean.setCity(d.getCity());
                            bean.setAddress(d.getAddress());
                            bean.setDistrictName(d.getDistrictId());
                            if (d.getLuckyNumber() != null) {
                                bean.setLucky_No(d.getLuckyNumber());
                            } else {
                                bean.setLucky_No("");
                            }
                            if (d.getLuckyChart() != null) {
                                bean.setLucky_Chart(d.getLuckyChart());
                            } else {
                                bean.setLucky_Chart("");
                            }


                            dataController.deleteUsers();
                            dataController.doprocessData(bean);

                            Constants.GLOBAL_USER_NAME = d.getFirstName() + " " + d.getLastName();
                            Constants.GLOBAL_USER_ID = d.getUserId();
                            Constants.GLOBAL_U_LUCK_NO = d.getLuckyNumber();
                            Constants.GLOBAL_USER_CONT = d.getUserName();

                            Log.e("User id", "" + Constants.GLOBAL_USER_ID);

                            Log.e("Chart Data", "" + d.getLuckyChart());
                            if (d.getLuckyChart() != null && !d.getLuckyChart().equalsIgnoreCase("null")) {
                                Constants.GLOBAL_U_LUCK_CHART = d.getLuckyChart();
                            }

                            ArrayList<LocationItems> loc_st= dataController.locationList(Constants.LOCATION_STATE);

                            Log.e(" RegFRAG"," LOG FRAGGG "+loc_st.size());
                            Log.e(" RegFRAG"," LOG FRAGGG "+bean.getState());
                            String iid=bean.getState();
                            String distid="";
                            for (int i=0;i<loc_st.size();i++)
                            {
                                if (iid.equalsIgnoreCase(loc_st.get(i).getId()))
                                {
                                    Log.e(" RegFRAG"," LOG FRAGGG "+i);
                                    state.putInt(Constants.PREFS_STATE_POSITION, i);
                                    distid=""+iid;
                                    state.commit();

                                }

                            }
                            Log.e(" RegFRAG"," LOG FRAGGG "+Constants.PREFS_STATE_POSITION);

                            ArrayList<LocationItems> loc_dist= dataController.locationListFiltered(Constants.LOCATION_DIST, distid);
                             String dist1=bean.getDistrictName();
                            for (int i=0;i<loc_dist.size();i++)
                            {
                                if (dist1.equalsIgnoreCase(loc_dist.get(i).getId()))
                                {
                                    Log.e(" RegFRAG"," LOG FRAGGG dist "+i);
                                    dist.putInt(Constants.PREFS_DIST_POSITION, i);
                                    dist.commit();

                                }

                            }
                            Log.e(" RegFRAG"," LOG FRAGGG "+Constants.PREFS_DIST_POSITION);




                            checkUserStatus();
                            dismissDialog();


                            preferences = getActivity().getSharedPreferences(Constants.PREF_TOURGUIDE_NAME, Context.MODE_PRIVATE);
                            boolean status = preferences.getBoolean(Constants.PREFS_TOURGUIDE_KEY, false);
                            if (status == false) {
                                Bundle b = new Bundle();
                                b.putSerializable("user", bean);
                                startActivity(new Intent(RegistrationFragment.this.getActivity(), Showcase_guide.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(b));
                                RegistrationFragment.this.getActivity().finish();
                            }else {
                                Bundle b = new Bundle();
                                b.putSerializable("user", bean);
                                Intent i = new Intent(RegistrationFragment.this.getActivity(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtras(b);
                                RegistrationFragment.this.getActivity().startActivity(i);
                                RegistrationFragment.this.getActivity().finish();
                            }

                           /* Bundle b = new Bundle();
                            b.putSerializable("user", bean);
                            Intent i = new Intent(RegistrationFragment.this.getActivity(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtras(b);
                            RegistrationFragment.this.getActivity().startActivity(i);
                            RegistrationFragment.this.getActivity().finish();
*/

                        } else {
                            dismissDialog();
                            Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

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
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }

    public void check() {
        if (strpass.equalsIgnoreCase(strcon_pass)) {
            Userdata_Bean bean = new Userdata_Bean();
            bean.setContact1(strcontact);
            bean.setPassword(strcon_pass);

            Log.e("Getdata", " check " + strcontact);
            Log.e("Getdata", " check " + strpass);
            Log.e("Getdata", " check " + strcon_pass);

            startTransaction(new RegistrationSecondFragment(), bean);
        } else {
            Toast.makeText(getActivity(), "Password Not Match", Toast.LENGTH_SHORT).show();
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        View view;
        /*TextInputLayout mobile_no;
        TextInputLayout password;
         TextInputLayout conf_passsord;*/
        AppCompatButton btnNext;
        String myid;


        public CustomTextWatcher(View txt, AppCompatButton btn, String ctx) {
            this.view = txt;
            this.myid = ctx;
            this.btnNext = btn;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            btnNext.setBackgroundColor(getResources().getColor(R.color.btn_background));
            btnNext.setTextColor(getResources().getColor(R.color.textColor));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("afterTextChanged", " id " + myid);
            String text = s.toString();
            String strpass = password.getEditText().getText().toString();
            String strcon_pass = conf_passsord.getEditText().getText().toString();
            String strcontact = mobile_no.getEditText().getText().toString();


            if (myid.equalsIgnoreCase("1")) {
                if (strcon_pass.length() > 0 && strpass.length() > 0 && strcontact.length() > 0) {
                    btnNext.setBackgroundColor(getResources().getColor(R.color.blue));
                    btnNext.setTextColor(getResources().getColor(R.color.text_white));
                    Log.e("1", " id " + myid);
                    //tv.setText("hurrrreeeys...");
                }
            } else if (myid.equalsIgnoreCase("2")) {
                if (strcontact.length() > 0 && strpass.length() > 0) {
                    btnNext.setBackgroundColor(getResources().getColor(R.color.blue));
                    btnNext.setTextColor(getResources().getColor(R.color.text_white));
                    Log.e("2", " id " + myid);
                    //tv.setText("hurrrreeeys...");
                }

            }
        }
    }


    public void checkUserStatus() {
        boolean status;
        status = dataController.isUserExist();

        if (status == true) {
            editor.putBoolean(Constants.PREFS_KEY, true);
            if(Constants.GLOBAL_USER_ID !="")
            {
                editor.putString(Constants.PREFS_USER_ID, Constants.GLOBAL_USER_ID);
            }

            editor.commit();
        } else if (status == false) {
            editor.putBoolean(Constants.PREFS_KEY, false);
            editor.commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            //Constants.progressDialog = null;
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

    ProgressDialog progressDialog;
}
