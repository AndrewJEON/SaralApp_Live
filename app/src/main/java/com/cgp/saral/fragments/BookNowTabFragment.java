package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class BookNowTabFragment extends BaseFragment implements View.OnClickListener {
    View view;
    TextInputLayout preferredDate, preferredTime, name, preferredMobile, pincode;
    CheckBox termondition;
    TextView term;
    private static final String ARG_PAGE_NUMBER = "page_number";
    Calendar myCalendar = Calendar.getInstance();
    AppCompatButton submit;
    List<Userdata_Bean> userData;
    ProgressDialog progressDialog ;
    String TAG = "BookNow";
    String serviceId="";
    TextView serviceIdMessage;

    String AM_PM ="";

    public BookNowTabFragment() {

    }

    public static BookNowTabFragment newInstance(int page) {
        BookNowTabFragment fragment = new BookNowTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_booknow, container, false);

        ButterKnife.bind(this, view);
        preferredDate = (TextInputLayout) view.findViewById(R.id.tv_preferredDate);
        preferredTime = (TextInputLayout) view.findViewById(R.id.tv_time);
        preferredMobile = (TextInputLayout) view.findViewById(R.id.tv_regsecMobileE);
        pincode = (TextInputLayout) view.findViewById(R.id.tv_pincode);
        name = (TextInputLayout)view.findViewById(R.id.tv_usernameE);
        term = (TextView) view.findViewById(R.id.term);
        submit = (AppCompatButton) view.findViewById(R.id.btn_submit);
        serviceIdMessage = (TextView)view.findViewById(R.id.serviceIdMessage);
        termondition = (CheckBox) view.findViewById(R.id.tv_termcondition);
        submit.setOnClickListener(this);
        preferredDate.getEditText().addTextChangedListener(new CustomTextWatcher(preferredDate, submit, getActivity()));
        preferredTime.getEditText().addTextChangedListener(new CustomTextWatcher(preferredTime, submit, getActivity()));
        preferredMobile.getEditText().addTextChangedListener(new CustomTextWatcher(preferredMobile, submit, getActivity()));
        pincode.getEditText().addTextChangedListener(new CustomTextWatcher(pincode, submit, getActivity()));

        preferredDate.getEditText().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Get yesterday's date
                Calendar calendar = Calendar.getInstance();
                //calendar.add(Calendar.DATE, -1);

                //Set yesterday time milliseconds as date pickers minimum date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
               /* // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
            }
        });
        preferredTime.getEditText().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(getActivity(), time, myCalendar
                        .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),false).show();
            }
        });
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://saralvaastu.com/mobileapp-terms-and-conditions/"));
                startActivity(browserIntent);
            }
        });


        DataController dbController = DataController.getsInstance(getActivity());
        userData = dbController.getAllData();
        if (userData != null && !userData.isEmpty()) {
            name.getEditText().setText(userData.get(0).getUserFName());
            preferredMobile.getEditText().setText(userData.get(0).getContact1());
        }
            return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();

    }


    private void hideKeyboard() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


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
            preferredDate.getEditText().setText(strD);

        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            String mins="";
            int mHour = i;
            String hours="";

            if(i < 12) {
                AM_PM = "AM";

            } else if(i > 12) {
                AM_PM = "PM";
                mHour=mHour-12;
            }
            if(i==12){
                AM_PM = "PM";
            }else if(i==0){
                AM_PM = "AM";
                mHour = 12;
            }
            if (i1 < 10){
                mins= "0"+i1;
            }else{
                mins =String.valueOf(i1);
            }
            if (mHour < 10){
                hours= "0"+mHour;
            }else{
                hours =String.valueOf(mHour);
            }
            String strD = hours+":"+mins + " "+ AM_PM;
            preferredTime.getEditText().setText(strD);

        }
    };

    private String getFormatDate(Date d) {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        myFormat = sdf.format(d);
        return myFormat;

    }


    @Override
    public void onClick(View v) {

        String strMobile = preferredMobile.getEditText().getText().toString();
        String strPreferredDate = preferredDate.getEditText().getText().toString();
        String strPreferredTime = preferredTime.getEditText().getText().toString();
        String strPincode = pincode.getEditText().getText().toString();
        if (strMobile.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your preferred mobile number", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strName" + strName);
            return;
        }else if (strPincode.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your pincode", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strName" + strName);
            return;
        }else if (!strPincode.isEmpty() && strPincode.length()!= 6) {
                Toast.makeText(getActivity(), "Please enter your 6 digits pincode ", Toast.LENGTH_SHORT).show();
                return;
            //Log.e("NewBean", "Validation strName" + strName);

        }else if (strPreferredDate.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your preferred date", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strName" + strName);
            return;
        }else if (strPreferredTime.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your preferred time", Toast.LENGTH_SHORT).show();
            //Log.e("NewBean", "Validation strName" + strName);
            return;
        }else if(!strPreferredDate.isEmpty() && !strPreferredTime.isEmpty()){
            String dateTime = strPreferredDate+ " " + strPreferredTime;
            try {

                DateFormat format = new SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH);
                Date date = format.parse(dateTime);
                if(date.getTime() < System.currentTimeMillis()/1000){
                    Toast.makeText(getActivity(), "Please enter future time", Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch (Exception e){

            }
        }

        if (!Utils.isConnectedToInternet(getActivity())) {
            Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
            return;

        }

        if (termondition.isChecked()) {
            JsonObject dataFromBean = new JsonObject();
            if(userData != null){
                Userdata_Bean newbean = userData.get(0);

                dataFromBean.addProperty("UserId", "");
                //dataFromBean.addProperty("CreatedDate", Constants.getFormattedDate(getFormatDate(date)));

                dataFromBean.addProperty("UserName", "" + newbean.getContact1());
                dataFromBean.addProperty("FirstName", "" + newbean.getUserFName());

                dataFromBean.addProperty("LastName", "");
                dataFromBean.addProperty("DOB", newbean.getDob());

                dataFromBean.addProperty("MobileNumber", "" + newbean.getContact1());
                dataFromBean.addProperty("Language", "" + newbean.getLanguage());

                dataFromBean.addProperty("Gender", "" + newbean.getGender());
                dataFromBean.addProperty("State", "" + newbean.getState());
                dataFromBean.addProperty("PhotoPath", newbean.getImgurl());

                dataFromBean.addProperty("City", newbean.getCity());
                dataFromBean.addProperty("Address", newbean.getAddress());
                dataFromBean.addProperty("District", newbean.getDistrictName());
                dataFromBean.addProperty("Email", "" + newbean.getMail());
                if (newbean.getSocialtype().equalsIgnoreCase("fb")) {
                    dataFromBean.addProperty("FacebookId", "" + newbean.getSocial_id());
                } else if (newbean.getSocialtype().equalsIgnoreCase("gplus")) {
                    dataFromBean.addProperty("GoogleId", "" + newbean.getSocial_id());
                } else {
                    dataFromBean.addProperty("FacebookId", "");
                    dataFromBean.addProperty("GoogleId", "");
                }

            }
            dataFromBean.addProperty("serviceType","book now");
            dataFromBean.addProperty("altMobile",strMobile);
            dataFromBean.addProperty("pincode",strPincode);
            dataFromBean.addProperty("preferredDate",strPreferredDate);
            dataFromBean.addProperty("preferredTime",strPreferredTime);
            serviceId = "BN"+System.currentTimeMillis();
            dataFromBean.addProperty("serviceId",serviceId);


            GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                    Request.Method.POST, Constants.BOOKNOW_URL +"?apikey="+Constants.LUCKY_CHAT_APIKEY, JsonObject.class, null,
                    successListener(), errorListener(), dataFromBean);
            //
            int socketTimeout = 50000;//50 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            myReq.setRetryPolicy(policy);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "createReg");
            showProgressDialog(getActivity(), "Booking in progress..");

        }else{
            Toast.makeText(getActivity(), "Please Accept Terms & Conditions", Toast.LENGTH_SHORT).show();
        }
    }

    private Response.Listener<JsonObject> successListener() {
        return new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                if (response != null)
                {
                    Log.d(TAG, "Created successfully");
                    serviceIdMessage.setText("Request has been created successfully. Please quote service id "+ serviceId + " for future communication.");
                    submit.setVisibility(View.GONE);
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

    private void handleVolleyError(VolleyError error) {
        //if any error occurs in the network operations, show the TextView that contains the error message
    String str ="";
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
    public   void dismissDialog() {
        progressDialog.dismiss();
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
            String strPreferredDate = preferredDate.getEditText().getText().toString().trim();
           String  strPreferredTime = preferredDate.getEditText().getText().toString().trim();
            if (!strPreferredDate.isEmpty() && !strPreferredDate.isEmpty() && !strPreferredDate.isEmpty()) {
                btnsubmit.setBackgroundColor(getResources().getColor(R.color.blue));
                btnsubmit.setTextColor(getResources().getColor(R.color.text_white));

            } else {
                //Toast.makeText(getActivity()," Ohhhhh",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
