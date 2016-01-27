package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.cgp.saral.model.IsRegisteredBySocial;
import com.cgp.saral.model.IsRegisteredBySocialResult;
import com.cgp.saral.model.LocationItems;
import com.cgp.saral.model.UserData;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.cgp.saral.social.helper.FbConnectHelper;
import com.cgp.saral.social.helper.GooglePlusSignInHelper;
import com.cgp.saral.tuarguide.Showcase_guide;
import com.facebook.GraphResponse;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements FbConnectHelper.OnFbSignInListener,
        GooglePlusSignInHelper.OnGoogleSignInListener {

    ProgressDialog progressDialog;
    private static final String TAG = LoginFragment.class.getSimpleName();
    // View view;

    boolean isFacebook = false, isGplus = false;

    boolean isLoading;

    boolean isSocialLogin = false, isNormalLogin = false;
    boolean isRegistered = true;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.parent)
    RelativeLayout view;

    @Bind(R.id.btn_socialotherlogin)
    AppCompatButton next;

    Bundle arg = new Bundle();
    String frag_id = "";

    FragmentManager manager;

    private FbConnectHelper fbConnectHelper;
    private GooglePlusSignInHelper gSignInHelper;

    SharedPreferences preferences;


    SharedPreferences.Editor editor,state,dist;
    DataController dataController;

    Userdata_Bean userModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_social_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        dataController = DataController.getsInstance(getActivity());
        preferences = getActivity().getSharedPreferences(Constants.PREFS_NAME_EDIT, Context.MODE_PRIVATE);

        editor = preferences.edit();
        state = preferences.edit();
        dist=preferences.edit();
        checkPageCome();
        manager = getActivity().getSupportFragmentManager();


        setup();
    }

    private void setup() {
        gSignInHelper = new GooglePlusSignInHelper(getActivity(), this);
        fbConnectHelper = new FbConnectHelper(this, this);

    }

    @OnClick(R.id.login_google)
    public void loginwithGoogle(View view) {
        if (!Utils.isConnectedToInternet(getActivity())) {
            Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
            return;

        }
        gSignInHelper.connect();
        setBackground(R.color.g_color);
        isFacebook = false;
        isGplus = true;
    }

    @OnClick(R.id.login_facebook)
    public void loginwithFacebook(View view) {

        if (!Utils.isConnectedToInternet(getActivity())) {
            Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
            return;

        }
        fbConnectHelper.connect();
        setBackground(R.color.fb_color);
        isFacebook = true;
        isGplus = false;
    }

    @OnClick(R.id.btn_socialotherlogin)
    public void normalLogin(View view) {
        Fragment fragment = new RegistrationFragment();
        Bundle bundle = new Bundle();
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        bundle.putString(Constants.FRAGMENT_ID, frag_id);
        fragment.setArguments(bundle);
        tran.addToBackStack(null);
        tran.replace(R.id.homeContainer, fragment,"registration").commit();
    }


    public void checkPageCome() {
        arg = getArguments();
        frag_id = arg.getString(Constants.FRAGMENT_ID);
        if (frag_id.equalsIgnoreCase("1")) {
            next.setText("REGISTER");
        } else if (frag_id.equalsIgnoreCase("2")) {
            next.setText("LOGIN");

        }


    }

    private void setBackground(int colorId) {
        // getView().setBackgroundColor(getActivity().getResources().getColor(colorId));
        progressBar.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    private void resetToDefaultView(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        getView().setBackgroundColor(getActivity().getResources().getColor(android.R.color.white));
        progressBar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        gSignInHelper.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {
        userModel = getUserModelFromGraphResponse(graphResponse);
        if (userModel != null) {

            {

                JsonObject data = new JsonObject();
                JsonObject dataFromBean = new JsonObject();

                dataFromBean.addProperty("UserName", "");
                dataFromBean.addProperty("Password", "");

                dataFromBean.addProperty("EmailId", "");
                dataFromBean.addProperty("NewPassword", "");

                dataFromBean.addProperty("FacebookId", userModel.getSocial_id());
                dataFromBean.addProperty("GoogleId", "");

                data.add("loginData", dataFromBean);
                data.add("source", Constants.getDeviceInfo());
                Log.e("Data Packet in FB id", "" + userModel.getSocial_id());
                Log.e("Data Packet in JSON", "" + data.toString());

                isFacebook = true;
                GsonRequestPost<IsRegisteredBySocial> myReq = new GsonRequestPost<>(
                        Request.Method.POST, Constants.isUserRegisteredBySocial, IsRegisteredBySocial.class, null,
                        successListenerLogin(), errorListener(), data);
                showProgressDialog(getActivity(), "Social Authorization by Facebook...");
                isLoading = true;
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "socialLogin");

            }


        } else {
            Log.e("FB Status", "Model is null");
        }
    }

    private Userdata_Bean getUserModelFromGraphResponse(GraphResponse graphResponse) {
        Userdata_Bean userModel = new Userdata_Bean();
        try {
            JSONObject jsonObject = graphResponse.getJSONObject();
            userModel.setUserFName(jsonObject.getString("name"));
            userModel.setMail(jsonObject.getString("email"));
            String id = jsonObject.getString("id");
            userModel.setSocial_id(id);
            userModel.setSocialtype("fb");
            String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";
            userModel.setImgurl(profileImg);
            Log.i(TAG, profileImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel;
    }

    @Override
    public void OnFbError(String errorMessage) {
        resetToDefaultView(errorMessage);
    }

    @Override
    public void OnGSignSuccess(Person mPerson, String emailAddress) {
        userModel = new Userdata_Bean();
        userModel.setUserFName(mPerson.getDisplayName());
        userModel.setMail(emailAddress);
        userModel.setImgurl(mPerson.getImage().getUrl());
        userModel.setSocialtype("gplus");
        userModel.setSocial_id(mPerson.getId());

        //  SharedPreferenceManager.getSharedInstance().saveUserModel(userModel);
        // isRegistered=false;
        // if(frag_id.equals("2"))
        {
            JsonObject data = new JsonObject();
            JsonObject dataFromBean = new JsonObject();

            dataFromBean.addProperty("UserName", "");
            dataFromBean.addProperty("Password", "");

            dataFromBean.addProperty("EmailId", "");
            dataFromBean.addProperty("NewPassword", "");
            dataFromBean.addProperty("FacebookId", "");
            dataFromBean.addProperty("GoogleId", userModel.getSocial_id());

            data.add("loginData", dataFromBean);
            data.add("source", Constants.getDeviceInfo());
            Log.e("Data Packet in JSON", "" + data.toString());

            isGplus = true;
            GsonRequestPost<IsRegisteredBySocial> myReq = new GsonRequestPost<>(
                    Request.Method.POST, Constants.isUserRegisteredBySocial, IsRegisteredBySocial.class, null,
                    successListenerLogin(), errorListener(), data);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog(getActivity(), "Social Authorization by Google...");
                    isLoading = true;
                }
            });

            VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "socialLogin");
        }


    }

    @Override
    public void OnGSignError(String errorMessage) {
        resetToDefaultView(errorMessage);
    }

    private void startAuth(Userdata_Bean userModel) {

        Fragment fragment = new RegistrationSecondFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userdata", userModel);
        fragment.setArguments(bundle);
        transaction.replace(R.id.homeContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (isGplus) {
            gSignInHelper.signOutGPlus();
            isGplus = false;

        } else if (isFacebook) {
            fbConnectHelper.logoutFacebook();
            isFacebook = false;
        }
        if (isLoading == true) {
            isLoading = false;
            dismissDialog();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private Response.Listener<IsRegisteredBySocial> successListenerLogin() {
        return new Response.Listener<IsRegisteredBySocial>() {
            @Override
            public void onResponse(IsRegisteredBySocial response) {

                if (response != null) {

Log.e("FB"," fb dat"+response.getIsRegisteredBySocialResult());
                    IsRegisteredBySocialResult result = response.getIsRegisteredBySocialResult();
                    if (result != null) {
                        if (result.getSuccess().toString().equals("true") && result.getData() != null) {


                            UserData d = result.getData();

                            Userdata_Bean bean = new Userdata_Bean();
                            if (null != d.getUserId()) {
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
                            if (d.getPhotoPath() != null) {


                                if (!d.getPhotoPath().equals(""))
                                    bean.setImgurl(d.getPhotoPath());
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




                            ArrayList<LocationItems> loc_st= dataController.locationList(Constants.LOCATION_STATE);

                           // Log.e(" LOGINFRAG"," LOG FRAGGG "+loc_st.size());
                           // Log.e(" LOGINFRAG"," LOG FRAGGG "+bean.getState());
                            String iid=bean.getState();
                            String parentid="";
                            for (int i=0;i<loc_st.size();i++)
                            {
                                if (iid.equalsIgnoreCase(loc_st.get(i).getId()))
                                {
                                   // Log.e(" LOGINFRAG", " LOG FRAGGG " + i);
                                    state.putInt(Constants.PREFS_STATE_POSITION, i);
                                    parentid=""+iid;
                                    state.commit();

                                }

                            }
                            //Log.e(" LOGINFRAG"," LOG FRAGGG "+Constants.PREFS_STATE_POSITION);
                            //Log.e(" LOGINFRAG"," LOG FRAGGG 2"+parentid);

                            ArrayList<LocationItems> loc_dist= dataController.locationListFiltered(Constants.LOCATION_DIST, parentid);
                            String dist1=bean.getDistrictName();
                            for (int i=0;i<loc_dist.size();i++)
                            {
                                if (dist1.equalsIgnoreCase(loc_dist.get(i).getId()))
                                {
                                    //Log.e(" LOGINFRAG"," LOG FRAGGG dist "+i);
                                    dist.putInt(Constants.PREFS_DIST_POSITION, i);
                                    dist.commit();

                                }

                            }
                           // Log.e(" LOGINFRAG"," LOG FRAGGG "+Constants.PREFS_DIST_POSITION);
                            //Log.e(" LOGINFRAG"," LOG FRAGGG "+Constants.PREFS_DIST_POSITION);
                            //editor.putInt(Constants.PREFS_DIST_POSITION, 0);
                            //editor.commit();




                            Constants.GLOBAL_USER_NAME = d.getFirstName() + " " + d.getLastName();
                            Constants.GLOBAL_USER_ID = d.getUserId();
                            Constants.GLOBAL_U_LUCK_NO = d.getLuckyNumber();
                            Constants.GLOBAL_USER_CONT = d.getUserName();

                           // Log.e("User id", "" + Constants.GLOBAL_USER_ID);


                            if (d.getLuckyChart() != null && !d.getLuckyChart().equalsIgnoreCase("null")) {
                                Constants.GLOBAL_U_LUCK_CHART = d.getLuckyChart();
                            }
                            //Log.e("User chart data1", "" + d.getLuckyChart());
                            //Log.e("User chart data2", "" + Constants.GLOBAL_U_LUCK_CHART);

                            checkUserStatus();
                            dismissDialog();


                            isRegistered = true;
                            doRegProcess(bean);

                        } else {
                            dismissDialog();

                            if (frag_id.equalsIgnoreCase("2")) {

                                if(result.getMessage().equalsIgnoreCase("User not registered !!"))
                                {
                                    Toast.makeText(getActivity(), result.getMessage() + ", Please go ahead with registartion", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    dismissDialog();
                                    Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

                                    return;
                                }
                              //  return;
                            } else if (frag_id.equalsIgnoreCase("1")) {

                                if(result.getMessage().equalsIgnoreCase("User not registered !!"))
                                {
                                    Toast.makeText(getActivity(), result.getMessage() + ", Please go ahead with registartion", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    dismissDialog();
                                    Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

                                    return;
                                }
                               // Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                               // return;

                            }
                            // resetToDefaultView("Please go ahead with Registration");
                            isRegistered = false;

                            doRegProcess(userModel);
                        }
                    } else {

                        isRegistered = false;
                        dismissDialog();


                    }


                }


            }
        };
    }


    public void doRegProcess(Userdata_Bean bean) {
        if (isRegistered) {
            preferences = getActivity().getSharedPreferences(Constants.PREF_TOURGUIDE_NAME, Context.MODE_PRIVATE);
            boolean status = preferences.getBoolean(Constants.PREFS_TOURGUIDE_KEY, false);
            if (status == false) {
                Bundle b = new Bundle();
                b.putSerializable("user", bean);
                startActivity(new Intent(LoginFragment.this.getActivity(), Showcase_guide.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(b));
                LoginFragment.this.getActivity().finish();
            }else {
                Bundle b = new Bundle();
                b.putSerializable("user", bean);
                Intent i = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(b);
                LoginFragment.this.getActivity().startActivity(i);
                LoginFragment.this.getActivity().finish();
            }

        } else {
            //Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

            if (isFacebook && !isRegistered) {
                startAuth(userModel);
            }

            if (isGplus && !isRegistered) {
                startAuth(userModel);
            }
        }
    }


    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.i("Debug: MainActivity", error.toString());
                handleVolleyError(error);
                dismissDialog();
                resetToDefaultView("Please try again");
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

    public void checkUserStatus() {
        boolean status;
        status = dataController.isUserExist();

        if (status == true) {
            editor.putBoolean(Constants.PREFS_KEY, true);
            if (!Constants.GLOBAL_USER_ID.equals("")) {
                editor.putString(Constants.PREFS_USER_ID, Constants.GLOBAL_USER_ID);
            }
            editor.commit();
        } else if (status == false) {
            editor.putBoolean(Constants.PREFS_KEY, false);
            editor.commit();
        }
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

}
