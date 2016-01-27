package com.cgp.saral.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonObject;

/**
 * Created by WeeSync on 17/10/15.
 */
public class FeedBack extends Fragment {

    View view;

    AppCompatButton buttonSend;
    EditText editTextSubject1,editTextmessege1;
    TextInputLayout textSubject;
    TextInputLayout textMessage;

   // MainActivity act;


    private static final String ARG_PAGE_NUMBER = "page_number";

    public FeedBack() {

    }

    public static FeedBack newInstance(int page) {
        FeedBack fragment = new FeedBack();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.feedback, container, false);

        buttonSend = (AppCompatButton) view.findViewById(R.id.buttonSendF);
        editTextSubject1 = (EditText) view.findViewById(R.id.editTextSubject1);
        editTextmessege1 = (EditText) view.findViewById(R.id.editTextSubject2);
        textSubject = (TextInputLayout) view.findViewById(R.id.editTextSubject);
        textMessage = (TextInputLayout) view.findViewById(R.id.editTextMessage);
       // textTo.setText("app@saralvaastu.com");
      //  textTo.setOnClickListener(null);


        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                //hidekeybord();
                // String to = textTo.getText().toString();
                String subject = textSubject.getEditText().getText().toString();
                String message = textMessage.getEditText().getText().toString();

                if (subject.equals("")) {
                    Toast.makeText(getActivity(), "Subject is Empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (message.equals("")) {
                    Toast.makeText(getActivity(), "Message is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subject.equals("") && message.equals("")) {
                    Toast.makeText(getActivity(), "Subject and Message fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                JsonObject data = new JsonObject();
                JsonObject dataFromBean = new JsonObject();

                dataFromBean.addProperty("Id", "");
                dataFromBean.addProperty("UserId", Constants.GLOBAL_USER_ID);
                dataFromBean.addProperty("Rating", "0");

                dataFromBean.addProperty("Description", message);
                dataFromBean.addProperty("Subject", subject);
                dataFromBean.addProperty("CreatedDate", "");


                data.add("userRate", dataFromBean);
                data.add("sourceInfo", Constants.getDeviceInfo());
                Log.e("Data Packet in JSON", "" + data.toString());


                GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                        Request.Method.POST, Constants.postFeedback, JsonObject.class, null,
                        successListener(), errorListener(), data);


                showProgressDialog(getActivity(), "Uploading Feedback");
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "feedBack");
            }

               /* Intent email = new Intent(Intent.ACTION_SEND);
             //   email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivityForResult(Intent.createChooser(email, "Choose an Email client :"), 300);

                startTransaction();

            }
        });*/

        });

        return view;
    }
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private Response.Listener<JsonObject> successListener() {
        return new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                if (response != null) {

                    Log.e("Feedback Response", "" + response.toString());
                    dismissDialog();

                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            getActivity());
                    alert.setTitle("Thank You, " + Constants.GLOBAL_USER_NAME);
                    alert.setIcon(R.drawable.ic_launcher); //app icon here
                    alert.setMessage("Thank You for sharing the Feedback, We appreciate your time !!!");

                    alert.setPositiveButton("Done !",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    MainActivity.instanceMain.selectItemFromDrawer(0);

                                    //  startActivity(new Intent(RegistrationFragment.this.getActivity(), Showcase_guide.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(b));
                                  /*  Intent i = new Intent(act.getActivity(), Showcase_guide.class);
                                    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    // i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);


                                    i.putExtras(b);
                                    act.getActivity().startActivity(i);


                                    act.getActivity().finish();*/
                                }
                            });

                    alert.show();
                    //Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();

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


   /* public void startTransaction() {

        FragmentManager fragmentManager= getFragmentManager();
        // ((SaralApplication) getApplication()).trackEvent(MainActivity.this, "MainActivity", "App Flow", "fragment Switching " + str);
        Fragment f= fragmentManager.findFragmentById(R.id.tab);
        if(f.isVisible())
        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
           // transaction.hide(f);
            transaction.replace(R.id.content_frame, f);
            transaction.addToBackStack(null);
            transaction.commit();
        }



    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();


        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll("feedBack");


    }

    ProgressDialog progressDialog;
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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 1001:

                if (resultCode == Activity.RESULT_OK) {

                    Toast.makeText(getActivity(), "SMS Invitation Sent", Toast.LENGTH_LONG).show();


                }


                break;
            case 1000:
                if(resultCode == Activity.RESULT_OK)
                {

                    Toast.makeText(getActivity(), "Invitation Sent", Toast.LENGTH_LONG).show();

                }

                break;

            case 300:
                if (requestCode == 300) {
                    if (requestCode == 300 && resultCode == getActivity().RESULT_OK) {
                        Toast.makeText(getActivity(), "Mail sent.", Toast.LENGTH_SHORT).show();


                        

                    } else if (requestCode == 300 && resultCode == getActivity().RESULT_CANCELED) {
                        Toast.makeText(getActivity(), "Mail Sent.", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getActivity(), "Plz try again.", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;

        }
    }*/
}
