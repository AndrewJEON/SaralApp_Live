package com.cgp.saral.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgp.saral.R;
import com.cgp.saral.SaralApplication;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.model.Language;
import com.cgp.saral.model.LocationItems;
import com.cgp.saral.model.Userdata_Bean;
import com.cgp.saral.myutils.CircleTransform;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;
import com.cgp.saral.social.helper.GooglePlusSignInHelper;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, GooglePlusSignInHelper.OnGoogleSignInListener{

    //
    Context ctx;
    Intent intent=null;
    Userdata_Bean  userdata=null;

   ProgressDialog progressDialog ;
    @Bind(R.id.iv_userpro)
    ImageView usr_pic;
    @Bind(R.id.iv_edit)
    ImageView iv_edit;
    @Bind(R.id.userName)
    TextView usr_name;
    @Bind(R.id.tv_mail)
    TextView usr_mail;
    @Bind(R.id.tv_birth)
    TextView usr_birth;
    @Bind(R.id.tv_useraddress)
    TextView usr_address;
    @Bind(R.id.tv_gender)
    TextView usr_gender;
    @Bind(R.id.tv_lang)
    TextView usr_language;
    @Bind(R.id.tv_contact)
    TextView usr_contact;
    @Bind(R.id.tv_helth)
    TextView usr_intrest;
    @Bind(R.id.tv_invite_frnd)
    TextView user_inviteFrd;
    @Bind(R.id.tv_logout)
    TextView sign_out;
    @Bind(R.id.iv_back)
    ImageView back;
    @Bind(R.id.txt_title)
    TextView title;

    @Bind(R.id.tv_luckyNo)
    TextView luckyNo;

    @Bind(R.id.pro)
    RelativeLayout pro;
    Userdata_Bean bean;




    FragmentManager fragmentManager;
    GooglePlusSignInHelper googlePlusSignInHelper;

  //  TextView user_luckyNo;

    public  SharedPreferences preferences;
    public   SharedPreferences.Editor editor;
    DataController dataController;

    HashMap<Integer ,String> langMap=new HashMap<>();
    HashMap<Integer ,String> gendremap=new HashMap<>();
    HashMap<Integer ,String> statemap=new HashMap<>();
    HashMap<Integer ,String> distmap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.user_profiledetails);
        ButterKnife.bind(this);
        intent=getIntent();

        Bundle b=intent.getExtras();

        googlePlusSignInHelper = new GooglePlusSignInHelper(this, this);

        if(b !=null)
        {
            userdata = new Userdata_Bean();
            userdata= (Userdata_Bean) b.getSerializable("dbuserdata");
        }else
        {
            Log.e("User Data"," is Null");
        }



        dataController=DataController.getsInstance(this);
       // dataController.createDb();

        ctx= getApplicationContext();

        variableInit();

        sign_out.setOnClickListener(this);
        usr_pic.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        List<Userdata_Bean> userdata = dataController.getAllData();
        if (null != userdata) {
            bean = userdata.get(0);
        } else {
            Log.e("Edit Profile ", "bean null");
        }
        if(bean !=null) {

            Constants.GLOBAL_U_LUCK_CHART = bean.getLucky_Chart();
            luckyNo.setText(bean.getLucky_No());

            Log.e("Data"," Lucky nnnn "+bean.getLucky_No());
        }
        setData();



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Edit Profile ", "Resume");
        onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Edit Profile ", "bean null");
    }

    public void setData()
    {
        title.setText("Profile");
        back.setVisibility(View.VISIBLE);
        if(userdata !=null ) {

            String gend = userdata.getGender();
            isProfilePic();
            usr_name.setText(userdata.getUserFName());
            usr_mail.setText(userdata.getMail());

            if (gend.equalsIgnoreCase("200001")) {
                usr_gender.setText("Male");
            } else {

                usr_gender.setText("Female");
            }

            usr_birth.setText(userdata.getDob().substring(0, 10));
            String lang = Constants.containKey(bean.getLanguage(), langMap);
            usr_language.setText(lang);
            String state = Constants.containKey(bean.getState(), statemap);

            String strTemp = bean.getDistrictName();
            Log.e("Location dist", "" + strTemp);
            if (strTemp != null && !strTemp.equalsIgnoreCase("null")){
                LocationItems locationItems = dataController.locationName(strTemp);
                if (locationItems != null) {
                    String dist = dataController.locationName(strTemp).getName();
                    // String dist=//Constants.containKey(userdata.getDistrictId(),distmap);
                    String strProfileData = bean.getAddress() + "," + dist + "," + state;

                    usr_address.setText(strProfileData);
                }
            }else{
                usr_address.setText(state);
            }
                usr_contact.setText(userdata.getContact1());
            // Log.e("Edit Profile ", "intrest " + userdata.getIntrest());
            //int[] strspl = new int[bean.getIntrest().length()];
            if(bean.getIntrest()!= null) {
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < bean.getIntrest().length(); i++) {
                    //strspl[i] = Character.getNumericValue(bean.getIntrest().charAt(i) - 1);
                    buffer.append(Character.getNumericValue(bean.getIntrest().charAt(i) - 1));
                }

                String strBuff = buffer.toString();
                String strIntrest = Utils.getSelectedItemsAsString(strBuff, Constants.intrestData);
                Log.e("Edit Profile ", "after method " + strIntrest);

                usr_intrest.setText(strIntrest);
            }
        }


    }
    
    public void variableInit()
    {


        ArrayList<Language> langs = dataController.languageList();
        for (Language l : langs) {
            langMap.put(Integer.valueOf(l.getId()), l.getName());
        }


      /*  gendremap.put(200001, "Male");
        gendremap.put(200002, "Female");*/


        ArrayList<LocationItems> stateL = dataController.locationList(Constants.LOCATION_STATE);
        for (LocationItems ll : stateL) {

            statemap.put(Integer.valueOf(ll.getId()), ll.getName());
        }

        ArrayList<LocationItems> distFiltered = dataController.locationListFiltered(Constants.LOCATION_DIST, userdata.getState());
        for (LocationItems ll : distFiltered) {
            distmap.put(Integer.valueOf(ll.getId()), ll.getName());
        }


        Log.e("Data Map"," lang "+langMap.size() +"  State -->"+statemap.size()+" -->"+distmap.size());


    }
    public boolean isProfilePic()
    {
        boolean isHave=false;

        if (bean.getImgurl()!=null)
        {

            Picasso.with(ctx).load(bean.getImgurl()).transform(new CircleTransform())
                    .placeholder(R.drawable.ic_dp_grey).error(R.drawable.ic_dp_grey).into(usr_pic);
            //usr_pic.setImageBitmap(thePic);
            isHave=true;

        }else
        {
            usr_pic.setImageResource(R.drawable.ic_dp_grey);
            isHave=false;
        }

       return isHave;
        }


    @Override
    public void onClick(View v)
    {
     if (v==sign_out)
    {
   logout_validate();
    }
        else if (v==iv_edit)
    {
        Log.e("iv_edit"," iv_edit ");
        Bundle b= new Bundle();
        b.putSerializable("user", userdata);
        startActivity(new Intent(UserProfileActivity.this, EditProfileActivity.class).putExtras(b));
        Log.e("iv_edit", " iv_edit 1");
    }
        else if (v==back)
     {
         finish();
     }


    }

    public  void logout_validate()
    {

        showProgressDialog(UserProfileActivity.this, "Please Wait, Signing out !!!");
        preferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        Intent intent=new Intent(UserProfileActivity.this,UserAuthoriseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        String type=userdata.getSocialtype();
        Log.e("Logout"," type "+type);
        String id=userdata.getUserId();

        Log.e("Logout"," Id "+id);
        if(type !=null) {
            if (type.equalsIgnoreCase("fb")) {
                Log.e("userprofile_activity", "This is fb");
                long ides = dataController.updateStatus(0, id);
                Log.e("Facebook Logout", "Update row " + ides);
                dataController.deleteUsers();
                editor.putBoolean(Constants.PREFS_KEY, false);

                editor.commit();
                showProgressDialog(UserProfileActivity.this, "Please Wait, Signing out !!!");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoginManager.getInstance().logOut();
                    }
                }).start();



                startActivity(intent);
                finish();

            } else if (type.equalsIgnoreCase("gplus")) {

               // Toast.makeText(ctx," g+ Logout in Dev Process ",Toast.LENGTH_LONG).show();


                showProgressDialog(UserProfileActivity.this, "Please Wait, Signing out !!!");

                long ides = dataController.updateStatus(0, id);
                Log.e("Google Plus Logout", "Update row " + ides);
                dataController.deleteUsers();
                editor.putBoolean(Constants.PREFS_KEY, false);
                editor.commit();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        googlePlusSignInHelper.signOut();
                    }
                }).start();



                startActivity(intent);
                finish();




            } else if (type.equalsIgnoreCase("normal")) {
                showProgressDialog(UserProfileActivity.this, "Please Wait, Signing out !!!");
                long ides = dataController.updateStatus(0, id);
                Log.e("Normal Signout", "Update row " + ides);
                dataController.deleteUsers();
                editor.putBoolean(Constants.PREFS_KEY, false);
                editor.commit();
                startActivity(intent);
                finish();
                Log.e("userprofile_activity", "This is normal");
            }
        }else
        {
            showProgressDialog(UserProfileActivity.this, "Please Wait, Signing out !!!");
            long ides = dataController.updateStatus(0, id);

            Log.e("userprofile_activity", "Update row " + ides);
            dataController.deleteUsers();
            editor.putBoolean(Constants.PREFS_KEY, false);
            editor.commit();
            startActivity(intent);
            finish();
            Log.e("userprofile_activity", "This is normal");
        }
       dismissDialog();
    }

    public void startTransaction(Fragment fragment, String str) {

        fragmentManager=getSupportFragmentManager();
                ((SaralApplication) getApplication()).trackEvent(UserProfileActivity.this, "MainActivity", "App Flow", "fragment Switching " + str);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.pro, fragment, str);
        transaction.addToBackStack(null);
        transaction.commit();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
           // Constants.progressDialog = null;
        }
    }

    @Override
    public void OnGSignSuccess(Person mPerson, String emailAddress) {

    }

    @Override
    public void OnGSignError(String errorMessage) {

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
}
