package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cgp.saral.R;
import com.cgp.saral.model.Data;
import com.cgp.saral.model.ResetPassResponse;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;
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
public class ForgotPassword_Verify extends BaseFragment implements View.OnClickListener, Observer {

    private static final String ARG_PAGE_NUMBER = "page_number";
    View view;
    @Bind(R.id.et_verify)
    EditText et_input;
    @Bind(R.id.ic1)
    ImageView icon;
    @Bind(R.id.btn_submit)
    AppCompatButton btn_submit;
    @Bind(R.id.input_verify)
    TextInputLayout til_input;
    Bundle bundle;
    int flag = 0;

    String strtext;

    public ForgotPassword_Verify() {
        // Required empty public constructor
    }


    public static ForgotPassword_Verify newInstance(int page) {
        ForgotPassword_Verify fragment = new ForgotPassword_Verify();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    String strOTP = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forgate_password_verify, container, false);
        ButterKnife.bind(this, view);
        receiveData();
        btn_submit.setOnClickListener(this);

        //   controller = DataController.getsInstance(getActivity());
        //controller.createDb();

        return view;
    }

    public void receiveData() {
        bundle = getArguments();
        flag = bundle.getInt("flag");
        Log.e("ForgotPassword_Verify", "bundle " + bundle);
        if (flag == 0) {
            Log.e("ForgotPassword_Verify", "flag " + flag);
            icon.setImageResource(R.drawable.ic_email);
            et_input.setHint("Email address");
            //et_input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        } else if (flag == 1) {
            Log.e("ForgotPassword_Verify", "flag " + flag);
            icon.setImageResource(R.drawable.ic_call);
            et_input.setHint("Mobile number");
            et_input.setInputType(InputType.TYPE_CLASS_NUMBER);
            // et_input.setOnKeyListener(DigitsKeyListener.getInstance());
            //et_input.setInputType(InputType.TYPE_CLASS_PHONE);
            setEditTextMaxLength(10);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validCellPhone(String number) {
        if (number.length() != 10 && number.length() < 10) {
            Toast.makeText(getActivity(), "Please enter 10 digit mobile no", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    public void setEditTextMaxLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        et_input.setFilters(FilterArray);
    }

    @Override
    public void onClick(View v) {
        strtext = til_input.getEditText().getText().toString();
        boolean status = false;
        if (flag == 0) {
            status = isValidEmail(strtext);
            if (status == true) {
                Fragment fragment = getFragmentManager().findFragmentByTag("Login_Home");
                if (fragment instanceof LoginHomeFragment) {

                }
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("Forgot Password", "popping backstack");
                    fm.popBackStack();
                }

                Toast.makeText(getActivity(), "Thank you ! we are working will revert you back on your Email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Enter valid email id", Toast.LENGTH_SHORT).show();
            }


        } else if (flag == 1) {
            status = validCellPhone(strtext);
            if (status == true) {
                /*FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {*/
                Log.i("Forgot Password", "popping backstack");

                JsonObject data = new JsonObject();
                JsonObject dataFromBean = new JsonObject();

                dataFromBean.addProperty("UserName", strtext);
                dataFromBean.addProperty("Password", "");

                dataFromBean.addProperty("EmailId", "");
                dataFromBean.addProperty("NewPassword", "");


                data.add("loginData", dataFromBean);
                data.add("source", Constants.getDeviceInfo());
                Log.e("Data Packet in JSON", "" + data.toString());


                if (!Utils.isConnectedToInternet(getActivity())) {
                    Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
                    return;

                }

              /*  GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                        Request.Method.POST, Constants.userForgotPURL, JsonObject.class, null,
                        successListener(), errorListener(), data);
                myReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                showProgressDialog(getActivity(), "Forgot Password Request...");
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "forgotPassword");*/

                GsonRequestPost<ResetPassResponse> myReq = new GsonRequestPost<>(
                        Request.Method.POST, Constants.userForgotPURL, ResetPassResponse.class, null,
                        successListener(), errorListener(), data);
                myReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                showProgressDialog(getActivity(), "Forgot Password Request...");
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "forgotPassword");
                Log.e(" Url", " link" + myReq.toString());
                Log.e(" Url", " link" + Constants.userForgotPURL);

            } else {

            }

        }
    }

    private Response.Listener<ResetPassResponse> successListener() {
        return new Response.Listener<ResetPassResponse>() {
            @Override
            public void onResponse(ResetPassResponse response) {
                //  JsonObject jsonObject,jsonObjectLogin;
                if (response != null) {


                    Log.e("password", " pass getMessage " + response.getForgotPasswordResult().getMessage());

                    dismissDialog();


                    Data d = response.getForgotPasswordResult().getData();


                    if (d != null) {

                               /* Userdata_Bean bean = new Userdata_Bean();
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


                                Log.e("", "" + d.getLuckyChart());
                                controller.doprocessData(bean);
                                Constants.GLOBAL_USER_NAME = d.getFirstName() + " " + d.getLastName();
                                Constants.GLOBAL_USER_ID = d.getUserId();
                                Constants.GLOBAL_U_LUCK_NO = d.getLuckyNumber();
                                Constants.GLOBAL_USER_CONT = d.getUserName();

                                =d.getPassword();
                                Log.e("Chart Data", "" + d.getLuckyChart());
                                if (d.getLuckyChart() != null && !d.getLuckyChart().equalsIgnoreCase("null")) {
                                    Constants.GLOBAL_U_LUCK_CHART = d.getLuckyChart();
                                }*/


                        Constants.GLOBAL_USER_NAME = d.getFirstName() + " " + d.getLastName();
                        Constants.GLOBAL_USER_ID = d.getUserId();
                        Constants.GLOBAL_U_LUCK_NO = d.getLuckyNumber();
                        Constants.GLOBAL_USER_CONT = d.getUserName();
                        Constants.GLOBAL_USER_CONT_NO = d.getUserName();

                        strOTP = d.getPassword();
                        Log.e("OTP", "" + strOTP);
                        handleFragment(strOTP);


                    } else {
                        // Constants.dismissDialog();
                        Toast.makeText(getActivity(), response.getForgotPasswordResult().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                        /*}else
                        {   dismissDialog();
                            Toast.makeText(getActivity(), response.getForgotPasswordResult().getMessage(), Toast.LENGTH_SHORT).show();
                        }*/

                }

            }
        };
    }


    private void handleFragment(String strOTP) {
        Log.e("In Handle Frag", "");
        Bundle b = new Bundle();
        b.putString("otp", strOTP);
        FragmentManager fm = getFragmentManager();

        Fragment ff = new ResetPassword();
        ff.setArguments(b);
        FragmentTransaction trans = fm.beginTransaction();

        trans.addToBackStack(null);
        trans.replace(R.id.homeContainer, ff);
        Log.e("In Handle Frag", "b4 Commit");

        trans.commit();

        Log.e("In Handle Frag", "After Commit");
        // trans.commitAllowingStateLoss();
        // fm.executePendingTransactions();

    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.i("Debug: MainActivity", error.toString());
                handleVolleyError(error);
                dismissDialog();
                retryReq();

            }
        };
    }

    private void retryReq()
    {
        btn_submit.setText("RE TRY");
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

        /*if (progressDialog != null) {
            progressDialog.dismiss();
            // Constants.progressDialog = null;
        }*/

        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll("forgotPassword");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void update(Observable observable, Object data) {

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
