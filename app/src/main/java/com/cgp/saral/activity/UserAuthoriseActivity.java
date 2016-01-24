package com.cgp.saral.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cgp.saral.R;
import com.cgp.saral.databaseHelper.DataController;
import com.cgp.saral.fragments.LoginFragment;
import com.cgp.saral.fragments.LoginHomeFragment;
import com.cgp.saral.myutils.Constants;

public class UserAuthoriseActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    SharedPreferences preferences;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("someVarA", someVarA);
       // outState.putString("someVarB", someVarB);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //someVarA = savedInstanceState.getInt("someVarA");
        //someVarB = savedInstanceState.getString("someVarB");
    }

    boolean condtion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_autherise);
        preferences=getApplicationContext().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        condtion=preferences.getBoolean(Constants.PREFS_KEY, false);
       // backHandle();

        Log.e("User Auth"," -->"+condtion);
        if(condtion != false)
        {

        }
       boolean userExist= DataController.getsInstance(this).isUserExist();

        Log.e("UserAuthoriseActivity", "condtion "+userExist);

        if(userExist==true )
        {
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            startTransaction(new LoginHomeFragment());
        }

         DataController.getsInstance(this).closeDB();


    }

/*public void backHandle()
{
    FragmentManager fm = getSupportFragmentManager();
    fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            if(getFragmentManager().getBackStackEntryCount() == 0) finish();
        }
    });
}*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Error", "CAll Result On Activity");
        Log.d("User Auth", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.homeContainer);
        if(loginFragment!=null)
        {
            loginFragment.onActivityResult(requestCode, resultCode, data);
        }

       // LoginFragment.callbackManager.onActivityResult(requestCode, resultCode, data);

        /* for gplus*/
      /*  if (requestCode == SocialLoginFragment.RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                SocialLoginFragment.myOnActivityResult(requestCode, resultCode, data);
                Log.e("onActivityResult", "resultCode "+resultCode);
            }else {
                SocialLoginFragment.myOnActivityResult(requestCode,resultCode,data);
                Log.e("onActivityResult", "resultCode "+resultCode);
            }
        }*/
    }
    public void startTransaction(Fragment fragment)
    {
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.homeContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

       Log.e("On Back User Auth", ""+count);
        if (count == 1) {
            finish();
            //additional code
            Log.e("On Back User Auth", " Count --->");
        } else {
            getSupportFragmentManager().popBackStack();
            Log.e("On Back User Auth", "else --->");
        }

    }



}
