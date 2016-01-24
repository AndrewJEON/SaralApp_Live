package com.cgp.saral.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetAllContentsResult implements Serializable{

    private List<Datum> Data = new ArrayList<Datum>();
    private String Message;
    private Boolean Success;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The Data
     */
    public List<Datum> getData() {
        return Data;
    }


    public void setData(List<Datum> Data) {
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