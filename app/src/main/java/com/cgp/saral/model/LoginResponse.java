package com.cgp.saral.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WeeSync on 01/10/15.
 */
public class LoginResponse implements Serializable {


    private com.cgp.saral.model.LoginResult LoginResult;
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return
     * The LoginResult
     */
    public com.cgp.saral.model.LoginResult getLoginResult() {
        return LoginResult;
    }

    /**
     *
     * @param LoginResult
     * The LoginResult
     */
    public void setLoginResult(com.cgp.saral.model.LoginResult LoginResult) {
        this.LoginResult = LoginResult;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
