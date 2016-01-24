package com.cgp.saral.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class FeedData implements Serializable{

    private com.cgp.saral.model.GetAllContentsResult GetAllContentsResult;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The GetAllContentsResult
     */
    public com.cgp.saral.model.GetAllContentsResult getGetAllContentsResult() {
        return GetAllContentsResult;
    }

    /**
     *
     * @param GetAllContentsResult
     * The GetAllContentsResult
     */
    public void setGetAllContentsResult(com.cgp.saral.model.GetAllContentsResult GetAllContentsResult) {
        this.GetAllContentsResult = GetAllContentsResult;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}