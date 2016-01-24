package com.cgp.saral.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.cgp.saral.adapter.ChatAdapter;
import com.cgp.saral.adapter.ChatMessageAdapter;
import com.cgp.saral.chat.handel.ChatServiceRequest;
import com.cgp.saral.chat.handel.ServiceRequestIntfc;
import com.cgp.saral.model.ChatMessage;
import com.cgp.saral.model.GetChatHistoryBySessionIdResult;
import com.cgp.saral.model.GetChatResponse;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.network.GsonRequestPost;
import com.cgp.saral.network.VolleySingleton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class YouFragment extends BaseFragment implements ServiceRequestIntfc {

    public ServiceRequestIntfc getNext() {
        return next;
    }

    public YouFragment setNext(ServiceRequestIntfc next) {
        this.next = next;
        return this;
    }

    ServiceRequestIntfc next;
    View view;
    ChatMessage chatMessage;

    String messageText;

    private String chatSessionId = "0";


    private static final String ARG_PAGE_NUMBER = "page_number";


    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    private int chatHistoryId = 0;

    Thread th;

    private ChatMessageAdapter mAdapter;

    public YouFragment() {
        // Required empty public constructor
    }

    public static YouFragment newInstance(int page) {
        YouFragment fragment = new YouFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chat, container, false);
        initControls();

        th = new Thread() {
            public void run() {
                for (; ; ) {
                    // do stuff in a separate thread
                    try {

                        uiCallback.sendEmptyMessage(0);
                        Thread.sleep(10000);
                    } catch (Exception ex)// sleep for 3 seconds
                    {
                        Log.e("Exp", "" + ex.toString());
                    }
                }
            }
        };
        th.start();
        return view;

    }

    private void initControls() {
        messagesContainer = (ListView) view.findViewById(R.id.messagesContainer);
        messageET = (EditText) view.findViewById(R.id.messageEdit);
        sendBtn = (Button) view.findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) view.findViewById(R.id.meLbl);
        String[] splited = Constants.GLOBAL_USER_NAME.split("\\s+");
        meLabel.setText(splited[0]);
        TextView companionLabel = (TextView) view.findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.container);
        companionLabel.setText("Saral Vaastu");

        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                messageET.setText("");

                JsonObject data;


                if (chatSessionId.equals("0")) {
                    data = new JsonObject();
                    data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);
                    data.add("source", Constants.getDeviceInfo());
                    Log.e("Data Packet in JSON", "" + data.toString());
                    GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                            Request.Method.POST, Constants.getChatSessionId, JsonObject.class, null,
                            successListener(), errorListener(), data);
                    //showProgressDialog(getActivity(), "Initiating Chat...");
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "getChatSessionId");
                } else {
                    data = new JsonObject();
                    data.addProperty("sessionId", chatSessionId);
                    data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);
                    if (!messageText.equals("")) {
                        data.addProperty("chatText", messageText);
                    } else {

                        data.addProperty("chatText", "Default");

                    }
                    data.add("source", Constants.getDeviceInfo());
                    Log.e("JSON with Session", "" + data.toString());
                    GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                            Request.Method.POST, Constants.sendChatText, JsonObject.class, null,
                            successChatSendListener(), errorListener(), data);
                    //showProgressDialog(getActivity(), "Sending Chat...");
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "postChat");

                }

            }
        });


    }


    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory() {

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi ! " + Constants.GLOBAL_USER_NAME + ", hope you would be having great day today..");

        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("Please tell me, how can I help you today?");

        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);


        adapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }


    private Handler uiCallback = new Handler() {
        public void handleMessage(Message msg) {
            //   Log.e("In Handeler",""+msg);

            if (!chatSessionId.equals("0")) {
                getChatHistory();
            }
        }
    };


    private Response.Listener<JsonObject> successListener() {
        return new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                if (response != null) {
                    JsonObject obj = response.getAsJsonObject("GetChatSessionIdResult");
                    JsonElement jG = obj.get("Data");

                    String sessionId = jG.toString();
                    if (!sessionId.equals("")) {
                        chatSessionId = sessionId;
                    }

                    JsonObject data = new JsonObject();
                    JsonObject dataFromBean = new JsonObject();

                    data.addProperty("sessionId", chatSessionId);
                    data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);
                    data.addProperty("isAdmin", "0");

                    data.add("source", Constants.getDeviceInfo());
                    Log.e(" JSON for Session", "" + data.toString());


                    GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                            Request.Method.POST, Constants.chatStartSession, JsonObject.class, null,
                            successSessionListener(), errorListener(), data);
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "initiateSessionChat");


                }

            }
        };
    }

    private Response.Listener<JsonObject> successSessionListener() {
        return new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                if (response != null) {


                    Log.e("Chat Session Id", " " + response);


                    JsonObject data = new JsonObject();


                    data.addProperty("sessionId", chatSessionId);
                    data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);


                    if (!messageText.equals("")) {
                        data.addProperty("chatText", messageText);
                    } else {
                        data.addProperty("chatText", "Test");
                    }
                    //data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);


                    //  data.add("loginData", dataFromBean);
                    data.add("source", Constants.getDeviceInfo());
                    Log.e(" JSON for Chat", "" + data.toString());


                    GsonRequestPost<JsonObject> myReq = new GsonRequestPost<>(
                            Request.Method.POST, Constants.sendChatText, JsonObject.class, null,
                            successChatSendListener(), errorListener(), data);
                    //showProgressDialog(getActivity(), "Initiating Chat...");
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "postChat");

                    //dismissDialog();

                    //  displayMessage(chatMessage);
                }

            }
        };
    }

    private Response.Listener<JsonObject> successChatSendListener() {
        return new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                if (response != null) {


                    Log.e("successChatSendListener", " " + response);
                    getChatHistory();

                }

            }
        };
    }

    private synchronized void getChatHistory() {

        JsonObject data = new JsonObject();


        data.addProperty("sessionId", chatSessionId);
        data.addProperty("currentUserId", Constants.GLOBAL_USER_ID);

        data.add("source", Constants.getDeviceInfo());
        Log.e(" JSON", "" + data.toString());
        GsonRequestPost<GetChatResponse> myReq = new GsonRequestPost<>(
                Request.Method.POST, Constants.getChatTextHistory, GetChatResponse.class, null,
                successGetChatListener(), errorListener(), data);
        // showProgressDialog(getActivity(), "Initiating Chat...");
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq, "getChatHSessionId");

    }


    private Response.Listener<GetChatResponse> successGetChatListener() {
        return new Response.Listener<GetChatResponse>() {
            @Override
            public void onResponse(GetChatResponse response) {

                if (response != null) {


                    if (progressDialog != null) {
                        dismissDialog();
                    }

                    Log.e("GetChatHistory", "" + response);
                    GetChatHistoryBySessionIdResult chatMessageResult = response.getGetChatHistoryBySessionIdResult();

                    if (chatMessageResult != null) {
                        ArrayList<ChatMessage> list = chatMessageResult.getData();

                        if (list != null) {


                            for (int i = 0; i < list.size(); i++) {

                                // msg ;
                                int historyId = Integer.valueOf(list.get(i).getChatHistoryId());
                                //Log.e("Hist", " ---->" + historyId + " --->" + chatHistoryId);

                                if (chatHistoryId < historyId) {

                                    ChatMessage msg = list.get(i);
                                    if (Constants.GLOBAL_USER_ID.equals(list.get(i).getUserId())) {
                                        msg.setMe(true);
                                    } else {
                                        msg.setMe(false);
                                    }
                                    msg.setMessage(list.get(i).getChatText());
                                    msg.setDate(list.get(i).getCreatedDate());


                                    chatHistoryId = historyId;
                                    displayMessage(msg);

                                }
                            }

                        }

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


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progressDialog != null) {
            progressDialog.dismiss();
            // Constants.progressDialog = null;
        }


        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll("getChatSessionId");
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
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    ProgressDialog progressDialog;

    @Override
    public void handleRequest(ChatServiceRequest request) {
        if (request.getType() == Constants.ServiceLevel.SESSION_INITIATED) {
            request.setConclusion("Chat initiated !!");
        } else {
            {
                if (next != null) {
                    next.handleRequest(request);
                } else {
                    throw new IllegalArgumentException("No handler found for :: " + request.getType());
                }
            }
        }
    }
}

