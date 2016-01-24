package com.cgp.saral.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WeeSync on 06/10/15.
 */
public class ChangePassResponse implements Serializable {
    public com.cgp.saral.model.ChangePasswordResult getChangePasswordResult() {
        return ChangePasswordResult;
    }

    public ChangePassResponse setChangePasswordResult(com.cgp.saral.model.ChangePasswordResult changePasswordResult) {
        ChangePasswordResult = changePasswordResult;
        return this;
    }

    private ChangePasswordResult ChangePasswordResult;



}