package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 04/10/15.
 */
public class CreateUserResponse implements Serializable{

    public com.cgp.saral.model.CreateUserResult getCreateUserResult() {
        return CreateUserResult;
    }

    public CreateUserResponse setCreateUserResult(com.cgp.saral.model.CreateUserResult createUserResult) {
        CreateUserResult = createUserResult;
        return this;
    }

    CreateUserResult CreateUserResult;
}
