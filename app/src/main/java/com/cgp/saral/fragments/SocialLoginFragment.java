package com.cgp.saral.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgp.saral.R;
import com.cgp.saral.activity.MainActivity;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.Constants;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialLoginFragment extends BaseFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    View view;
    Context context;
    Typeface typeface;
    Bundle bundle = new Bundle();
    Userdata_Bean bean = new Userdata_Bean();
    FragmentManager manager;
    SharedPreferences preferences, logoutPref;
    public static SharedPreferences.Editor editor, logoutEditor;
    DataController dataController;
    public static final String TAG = SocialLoginFragment.class.getSimpleName();

    GoogleApiClient mGoogleApiClient;

    /* Initilization of facebook variable*/
    //@InjectView(R.id.iv_fblogin)
    public static LoginButton fb_login;
    public static AccessToken accessToken;
    public static CallbackManager callbackManager;
    public static AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    public static boolean STATUS = false;
    /* Initilization of google plus variable*/
    private View btnLogout;
    public static final int RC_SIGN_IN = 0;
    private static boolean mIntentInProgress;
    private static boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    // Google client to communicate with Google


    //@InjectView(R.id.btn_signin_gplus)
    SignInButton btnSignIn;
    Bundle arg = new Bundle();
    String frag_id = "";
    @Bind(R.id.btn_socialotherlogin)
    AppCompatButton next;

    signInSocial social;


    public interface signInSocial
    {
        public void doProcessRequest(int requestCode, int resultCode, Intent data);
    }

    // public SaralApplication singlton;
    public SocialLoginFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof signInSocial)
        {
            this.social = (signInSocial)activity;
        }else
        {

            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");

        }
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);


            Log.e("Error", "CAll CALBACK SUCCESS");
        }

        @Override
        public void onCancel() {
            Log.e("Error", "CAll CALBACK CANCEL");
        }

        @Override
        public void onError(FacebookException e) {
            Log.e("Error", "CAll CALBACK EXCEPTION");
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_social_login, container, false);
        dataController = DataController.getsInstance(getActivity());
        //dataController.createDb();
        ButterKnife.bind(this, view);
        btnSignIn = (SignInButton) view.findViewById(R.id.btn_signin_gplus);
        fb_login = (LoginButton) view.findViewById(R.id.iv_fblogin);
        checkPageCome();
        context = getActivity();
        preferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
      //  logoutPref= getActivity().getSharedPreferences(Constants.PREFS_KEY_GOOGLE_LOGOUT, Context.MODE_PRIVATE);
        editor = preferences.edit();
        manager = getFragmentManager();
        Login(view);

        setRetainInstance(true);
        boolean isGoogleLogout=preferences.getBoolean(Constants.PREFS_KEY_GOOGLE_LOGOUT,false);


        Log.e("Google Logout Status", ""+isGoogleLogout);

        /* Now coding gplus*/
        btnSignIn.setOnClickListener(onLoginListener());
        //initializing google api client when start
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this).
                        addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder()
                        .build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();






        return view;
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

    @OnClick(R.id.btn_socialotherlogin)
    public void other_Login() {
        //
        Fragment fragment = new RegistrationFragment();
        Bundle bundle = new Bundle();
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        bundle.putString(Constants.FRAGMENT_ID, frag_id);
        fragment.setArguments(bundle);
        tran.addToBackStack(null);
        tran.replace(R.id.homeContainer, fragment).commit();
    }

    public void Login(View view) {
        Log.e("Error", "CAll LOGIN METHOD");
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        Init(view);
    }


    public void Init(View view) {
        Log.e("Error", "CAll INIT");

        //fb_login.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends","AccessToken"));
        fb_login.setReadPermissions("user_friends");
        fb_login.registerCallback(callbackManager, callback);
    }

    public void displayMessage(Profile profile) {
        if (profile != null) {
            Log.e("Error", "CAll PROFILE DISPLAY");
            //textView.setText(profile.getName());
            String id = dataController.login(profile.getId());
            if (id.equalsIgnoreCase(profile.getId()) && !(id.isEmpty())) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                Profile mypro = Profile.getCurrentProfile();
                bean.setSocial_id(mypro.getId());
                bean.setUserFName(mypro.getName());
                bean.setImgurl(mypro.getProfilePictureUri(50, 50).toString());
                bean.setSocialtype("fb");
                //Picasso.with(getActivity()).load(avatarUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imgProfilePic);

                Fragment fragment = new RegistrationSecondFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                bundle.putSerializable("userdata", bean);
                fragment.setArguments(bundle);
                transaction.replace(R.id.homeContainer, fragment);
                transaction.commit();
            }
        } else {
            Log.e("SocialloginFrag", "displayMessage method not work");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /* for gplus */
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

        /* for gplus*/
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.e("SocialLogin", "Onstop method call");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Error", "CAll RESUME");
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }


    /* now write a gplus intigration coding  */
    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        // Toast.makeText(getActivity(), "User is connected!", Toast.LENGTH_LONG).show();
        Log.e(TAG, "User is connected!");
        // Get user's information
        getUserInformation();

        // Update the UI after signin
        layoutAction(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            // signOutGooglePlusAccount();
            Log.e(TAG, "onPause logout g+");
        } else {
            //  LoginManager.getInstance().logOut();
            Log.e(TAG, "onPause logout fb");
        }
        Log.e(TAG, "onPause");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        layoutAction(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed");
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(),
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;
            Log.e(TAG, "mConnectionResult " + result);

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
                Log.i(TAG, "clickAgain");
            }
        }
    }

    /**
     * showing/hiding Button Login and Profile Layout
     */
    public void layoutAction(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            //profileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            // profileLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Sign in into google+
     */
    private void signInGooglePlusAccount() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Method to resolve any sign in errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Getting user's information: name, email, profile picture (avatar)
     */
    private void getUserInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                Constants.STATUS = mGoogleApiClient.isConnected();
//                Log.e("getUserInformation","status "+STATUS);
                String id = dataController.login(person.getId());
                if (id.equalsIgnoreCase(person.getId()) && !(id.isEmpty())) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Log.e("getUserInformation", "status direct " + mGoogleApiClient.isConnected());

                    String dob = person.getBirthday();
                    String location = person.getCurrentLocation();
                    int gender = person.getGender();
                    String personName = person.getDisplayName(); // user account name
                    String imgUrl = person.getImage().getUrl(); // profile image url
                    String alanguage = person.getLanguage();
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient); // account Email

                    bean.setSocial_id(person.getId());
                    bean.setUserFName(personName);
                    bean.setDob(dob);
                    bean.setLanguage(alanguage);
                    bean.setMail(email);
                    bean.setState(location);
                    bean.setImgurl(imgUrl);
                    bean.setSocialtype("gplus");
                    //Picasso.with(getActivity()).load(avatarUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imgProfilePic);
                    Fragment fragment = new RegistrationSecondFragment();
                    FragmentTransaction transaction = manager.beginTransaction();
                    bundle.putSerializable("userdata", bean);
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.homeContainer, fragment);
                    transaction.commit();
                    Log.e(TAG, "profile gender " + gender);
                /*Intent intent=new Intent(context, MainActivity.class);
                intent.putExtra("user", personName);
                context.startActivity(intent);*/
                }
            } else {

                Log.e(TAG, "profile is null");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Sign out from google account
     */
    public void signOutGooglePlusAccount() {
        Log.e(TAG, "Status g+" + STATUS);
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            layoutAction(false);
            //editor.putBoolean(Constants.PREFS_KEY, false);
            // editor.commit();
            Log.e(TAG, "Log out g+");
        }

    }


    public  void myOnActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != Activity.RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
                Log.e(TAG, "onActivityResult connect");
            }

            social.doProcessRequest(requestCode,responseCode,intent);
        }
    }


    private View.OnClickListener onLoginListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "login clicked!");
                signInGooglePlusAccount();
            }
        };
    }

    private View.OnClickListener onLogoutListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutGooglePlusAccount();
                Log.e(TAG, "logout clicked!");
            }
        };
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.e("SocialLogin", "LoadProfileImage class call");
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }



}
