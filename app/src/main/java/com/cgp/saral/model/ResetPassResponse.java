package com.cgp.saral.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WeeSync on 06/10/15.
 */
public class ResetPassResponse implements Serializable {
    private ForgotPasswordResult ForgotPasswordResult;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The ForgotPasswordResult
     */
    public ForgotPasswordResult getForgotPasswordResult() {
        return ForgotPasswordResult;
    }

    /**
     * @param ForgotPasswordResult The ForgotPasswordResult
     */
    public void setForgotPasswordResult(ForgotPasswordResult ForgotPasswordResult) {
        this.ForgotPasswordResult = ForgotPasswordResult;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}