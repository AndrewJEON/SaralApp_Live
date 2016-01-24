package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 03/10/15.
 */
public class CreateUserResult implements Serializable {

    public UserData getData() {
        return Data;
    }

    public CreateUserResult setData(UserData data) {
        Data = data;
        return this;
    }

    public String getMessage() {
        return Message;
    }

    public CreateUserResult setMessage(String message) {
        Message = message;
        return this;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public CreateUserResult setSuccess(Boolean success) {
        Success = success;
        return this;
    }

    private UserData Data;
    private String Message;
    private Boolean Success;

}
