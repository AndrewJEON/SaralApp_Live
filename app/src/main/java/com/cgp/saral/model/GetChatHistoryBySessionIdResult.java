package com.cgp.saral.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeeSync on 23/10/15.
 */
public class GetChatHistoryBySessionIdResult {

    private ArrayList<ChatMessage> Data ;

    public ArrayList<ChatMessage> getData() {
        return Data;
    }

    public GetChatHistoryBySessionIdResult setData(ArrayList<ChatMessage> data) {
        Data = data;
        return this;
    }

    public String getMessage() {
        return Message;
    }

    public GetChatHistoryBySessionIdResult setMessage(String message) {
        Message = message;
        return this;
    }

    public String getSuccess() {
        return Success;
    }

    public GetChatHistoryBySessionIdResult setSuccess(String success) {
        Success = success;
        return this;
    }

    private String Message;
    private String Success;
}
