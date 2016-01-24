package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 17/10/15.
 */
public class IsRegisteredBySocialResult implements Serializable{
    private UserData Data;
    private String Message;

    public Boolean getSuccess() {
        return Success;
    }

    public IsRegisteredBySocialResult setSuccess(Boolean success) {
        Success = success;
        return this;
    }

    public UserData getData() {
        return Data;
    }

    public IsRegisteredBySocialResult setData(UserData data) {
        Data = data;
        return this;
    }

    public String getMessage() {
        return Message;
    }

    public IsRegisteredBySocialResult setMessage(String message) {
        Message = message;
        return this;
    }

    private Boolean Success;


}
