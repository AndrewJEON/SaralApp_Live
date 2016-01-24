package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 06/10/15.
 */
public class UpdateUserResult implements Serializable{

    private UserData Data;

    public UserData getData() {
        return Data;
    }

    public UpdateUserResult setData(UserData data) {
        Data = data;
        return this;
    }

    public String getMessage() {
        return Message;
    }

    public UpdateUserResult setMessage(String message) {
        Message = message;
        return this;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public UpdateUserResult setSuccess(Boolean success) {
        Success = success;
        return this;
    }

    private String Message;
    private Boolean Success;
}
