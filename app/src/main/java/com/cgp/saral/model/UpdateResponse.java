package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 06/10/15.
 */
public class UpdateResponse implements Serializable{

    public com.cgp.saral.model.UpdateUserResult getUpdateUserResult() {
        return UpdateUserResult;
    }

    public UpdateResponse setUpdateUserResult(com.cgp.saral.model.UpdateUserResult updateUserResult) {
        UpdateUserResult = updateUserResult;
        return this;
    }

    public UpdateUserResult UpdateUserResult;


}
