package com.cgp.saral.fragments;


import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cgp.saral.R;
import com.cgp.saral.myutils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteTabFragment extends BaseFragment {
    View view;

    @Bind(R.id.img_whatsapp)
    ImageView wtsApp;
    @Bind(R.id.img_phonecontact)
    ImageView imgSMS;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    String strMessage = "It's wonderful to use Vaastu for better life ! Please use Saral Vaastu App located at "+ Constants.APP_PLAY_STORE_URL;

    private static final String ARG_PAGE_NUMBER = "page_number";

    public InviteTabFragment() {

    }

    public static InviteTabFragment newInstance(int page) {
        InviteTabFragment fragment = new InviteTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.invite_friend, container, false);

        ButterKnife.bind(this, view);


        wtsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final ComponentName name = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
                Intent oShareIntent = new Intent();
                oShareIntent.setComponent(name);
                oShareIntent.setType("text/plain");
                oShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, strMessage);
                startActivityForResult(oShareIntent, 5000);
            }
        });

        imgSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);

               // startActivity();
               // Intent sendIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                sendIntent.putExtra("sms_body", strMessage);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivityForResult(sendIntent, 5001);

            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 5001:

                if (resultCode == getActivity().RESULT_OK) {
                    progressBar.setVisibility(View.GONE);

                    Cursor s = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            null, null, null);

                    if (s.moveToFirst()) {
                        String phoneNum = s.getString(s.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Toast.makeText(getActivity(), phoneNum, Toast.LENGTH_LONG).show();
                    }

                }

                break;

            case 5000:
                if (resultCode == getActivity().RESULT_OK) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Invitation Sent", Toast.LENGTH_LONG).show();

            }

        }


    }

    @Override
    public void onStop() {
        super.onStop();

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
