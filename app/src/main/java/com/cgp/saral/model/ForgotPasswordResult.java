package com.cgp.saral.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WeeSync on 06/10/15.
 */
public class ForgotPasswordResult {
    private Data Data;
    private String Message;
    private Boolean Success;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The Data
     */
    public Data getData() {
        return Data;
    }

    /**
     *
     * @param Data
     * The Data
     */
    public void setData(Data Data) {
        this.Data = Data;
    }

    /**
     *
     * @return
     * The Message
     */
    public String getMessage() {
        return Message;
    }

    /**
     *
     * @param Message
     * The Message
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     *
     * @return
     * The Success
     */
    public Boolean getSuccess() {
        return Success;
    }

    /**
     *
     * @param Success
     * The Success
     */
    public void setSuccess(Boolean Success) {
        this.Success = Success;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
