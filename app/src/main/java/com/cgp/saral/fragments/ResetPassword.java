package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cgp.saral.model.ChangePassResponse;
import com.cgp.saral.model.Data;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPassword extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PAGE_NUMBER = "page_number";
    View view;


    @Bind(R.id.btn_submit_confirm)
    AppCompatButton btn_submit;

    @Bind(R.id.curr_password)
    TextInputLayout curr_password;

    @Bind(R.id.new_password)
    TextInputLayout new_pass;

    @Bind(R.id.confirm_password)
    TextInputLayout conf_password;

    Bundle bundle;
    int flag = 0;
    @Bind(R.id.user_mobile_no)
    TextView mobile_no;

    String otpPass;

    String strPassword;

    public ResetPassword() {
        // Required empty public constructor
    }


    public static ResetPassword newInstance(int page) {
        ResetPassword fragment = new ResetPassword();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    String strData;
    DataController controller;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.change_password, container, false);
        ButterKnife.bind(this, view);
        receiveData();
        mobile_no.setText(Constants.GLOBAL_USER_CONT_NO);
        btn_submit.setOnClickListener(this);

        //curr_password.getEditText().setText(otpPass);

        controller = DataController.getsInstance(getActivity());
        //controller.createDb();

        return view;
    }

    public void receiveData() {
        bundle = getArguments();
        otpPass = bundle.getString("otp");
        Log.e("Reset Password", "bundle  OTP --> " + otpPass);
       /* if (flag==0)
        {
            Log.e("ForgotPassword_Verify", "flag " + flag);
            icon.setImageResource(R.drawable.ic_email);
            et_input.setHint("Email address");
            //et_input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        }
        else if (flag==1)
        {
            Log.e("ForgotPassword_Verify", "flag " + flag);
            icon.setImageResource(R.drawable.ic_call);
            et_input.setHint("Mobile number");
            et_input.setInputType(InputType.TYPE_CLASS_NUMBER);
           // et_input.setOnKeyListener(DigitsKeyListener.getInstance());
            //et_input.setInputType(InputType.TYPE_CLASS_PHONE);
            setEditTextMaxLength(10);
        }*/
    }


    @Override
    public void onClick(View v) {
        String strOTP=curr_password.getEditText().getText().toString();
        String strP = new_pass.getEditText().getText().toString();
        String strConfP = conf_password.getEditText().getText().toString();
        Log.e("OTP Entered","   --> "+strOTP);
        if(TextUtils.isEmpty(strOTP))
        {
            Toast.makeText(getActivity(), "Please enter One Time Password", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(strP))
        {
            Toast.makeText(getActivity(), "Please enter new password", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(strP))
        {
            Toast.makeText(getActivity(), "Please enter confirm password", Toast.LENGTH_LONG).show();
            return;
        }

        if(!strOTP.equals(otpPass))
        {
            Toast.makeText(getActivity(), "One Time Password entered doesn't match", Toast.LENGTH_LONG).show();
            return;
        }
        if (strP.equals(strConfP)) {
            strPassword = strConfP;
            if(strPassword.length()<7)
            {
                Toast.makeText(getActivity(), "Password Can't be less than 6 characters", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            Toast.makeText(getActivity(), "Password doesn't match", Toast.LENGTH_LONG).show();
            return;
        }

        Log.i("Change Password", "popping backstack");

        JsonObject data = new JsonObject();
        JsonObject dataFromBean = new JsonObject();

        dataFromBean.addProperty("UserName", Constants.GLOBAL_USER_CONT_NO);
        dataFromBean.addProperty("Password", otpPass);

        dataFromBean.addProperty("EmailId", "");
        dataFromBean.addProperty("NewPassword", strPassword);


        data.add("loginData", dataFromBean);
        data.add("source", Constants.getDeviceInfo());
        Log.e("Data Packet in JSON", "" + data.toString());


        GsonRequestPost<ChangePassResponse> myReq = new GsonRequestPost<>(
                Request.Method.POST, Constants.userChangePassword, ChangePassResponse.class, null,
                successListener(), errorListener(), data);
        showProgressDialog(getActivity(), "Change Password Processing...");
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "changePass");
        Log.e(" Url", " link" + myReq.toString());
        Log.e(" Change Pass Url", " link" + Constants.userChangePassword);
        //   fm.popBackStack();


        // }


    }

    private Response.Listener<ChangePassResponse> successListener() {
        return new Response.Listener<ChangePassResponse>() {
            @Override
            public void onResponse(ChangePassResponse response) {
                //  JsonObject jsonObject,jsonObjectLogin;
                if (response != null) {


                    Log.e("password", " pass getMessage " + response.getChangePasswordResult().getMessage());
                    boolean str = response.getChangePasswordResult().getSuccess();
                    if (str) {
                        dismissDialog();


                        Data d = response.getChangePasswordResult().getData();
                        Log.e("USer Data", "" + d.getFirstName());

                        if (d != null) {

                            Userdata_Bean bean = new Userdata_Bean();
                            bean.setUserId(d.getUserId());
                            bean.setUserFName(d.getFirstName() + " " + d.getLastName());
                            bean.setContact1(d.getUserName());
                            bean.setDob(d.getDOB());
                            bean.setGender(d.getGender().toString());
                            bean.setLanguage(d.getLanguage().toString());
                            bean.setMail(d.getEmail());
                            bean.setPassword(d.getPassword());

                            bean.setIntrest(d.getInterestsTag());
                            bean.setState("" + d.getState());

                            //bean.setCity(""+d.getCity());
                            bean.setAddress("" + d.getAddress());
                            bean.setDistrictName("" + d.getDistrictId());
                            bean.setLucky_No("" + d.getLuckyNumber());
                            bean.setLucky_Chart("" + d.getLuckyChart());



                            if (d.getPassword() != null) {
                                bean.setPassword(d.getPassword());
                            } else {
                                bean.setPassword("");
                            }
                            if (d.getPhotoPath() != null) {


                                if (!d.getPhotoPath().equals(""))
                                    bean.setImgurl(d.getPhotoPath());
                            }

                            /*String fbId = d.getFacebookId().toString();
                            String gId = d.getGoogleId().toString();
                            Log.e("Facebook Id", "" + fbId + "  google id " + gId);*/
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


                            Log.e("", " Social Id " +bean.getSocial_id()+ " Type --> "+bean.getSocialtype());
                            controller.deleteUsers();
                            controller.doprocessData(bean);
                            Constants.GLOBAL_USER_NAME = d.getFirstName() + " " + d.getLastName();
                            Constants.GLOBAL_USER_ID = d.getUserId();
                            Constants.GLOBAL_U_LUCK_NO = d.getLuckyNumber();
                            Constants.GLOBAL_USER_CONT = d.getUserName();

                            Log.e("Chart Data", "" + d.getLuckyChart());
                            if (d.getLuckyChart() != null && !d.getLuckyChart().equalsIgnoreCase("null")) {
                                Constants.GLOBAL_U_LUCK_CHART = d.getLuckyChart();
                            }

                            sendToHome(bean);
                           // handleFragment();
                          /*  getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });*/

                        } else {
                            // Constants.dismissDialog();
                            Toast.makeText(getActivity(), response.getChangePasswordResult().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        dismissDialog();
                        Toast.makeText(getActivity(), response.getChangePasswordResult().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        };
    }

/*
    boolean status=false;
    if (flag==0)
    {
        status=isValidEmail(strtext);
        if (status==true)
        {
            Fragment fragment = getFragmentManager().findFragmentByTag("Login_Home");
            if (fragment instanceof LoginHomeFragment) {

            }
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("Forgot Password", "popping backstack");
                fm.popBackStack();
            }

            Toast.makeText(getActivity(), "Thank you ! we are working will revert you back on your Email", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Enter valid email id", Toast.LENGTH_SHORT).show();
        }


    }*/


    private void sendToHome(Userdata_Bean bean)
    {
        Bundle b = new Bundle();
        b.putSerializable("user", bean);
        Intent i = new Intent(ResetPassword.this.getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtras(b);
        ResetPassword.this.getActivity().startActivity(i);
        ResetPassword.this.getActivity().finish();
    }
    private void handleFragment() {
        FragmentManager fm = getFragmentManager();
      //  int count = fm.getBackStackEntryCount();
        //Log.e("BackSatck in Loop", "" + count);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_ID, "2");

        Fragment ff = new RegistrationFragment(); //fm.findFragmentByTag("registration");
        if (ff != null) {
            Log.e("Get Through Tag", "not null" + ff);
        } else {
            ff = new RegistrationFragment();
            Log.e("In reg Transacation", " new obj");
        }

        ff.setArguments(bundle);
        Log.e("In reg Transacation", "12");
        FragmentTransaction trans = fm.beginTransaction();
        Log.e("after Transaction", "125757" + trans);

        trans.add(R.id.homeContainer, ff);
        Log.e("In reg Transacation", "345");
        //trans.commit();


        //trans.addToBackStack(null);
        //trans.commit();
        trans.commitAllowingStateLoss();
        Log.e("In reg Transacation", "678");
        fm.executePendingTransactions();

        // fm.popBackStack();

        // fm.popBackStackImmediate(getFragmentManager().getBackStackEntryCount() - 1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Toast.makeText(getActivity(), "Thank you ! Password has Changed Successfully", Toast.LENGTH_SHORT).show();

        Log.e("BackSatck in Loop", " ---> InBack Stack processed");

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


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progressDialog != null) {
            progressDialog.dismiss();
            // Constants.progressDialog = null;
        }

        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll("changePass");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    ProgressDialog progressDialog;
}
