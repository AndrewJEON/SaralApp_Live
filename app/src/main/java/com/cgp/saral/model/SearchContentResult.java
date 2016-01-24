package com.cgp.saral.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeeSync on 30/10/15.
 */
public class SearchContentResult implements Serializable{

    public List<Datum> getData() {
        return Data;
    }

    public SearchContentResult setData(List<Datum> data) {
        Data = data;
        return this;
    }

    public String getMessage() {
        return Message;
    }

    public SearchContentResult setMessage(String message) {
        Message = message;
        return this;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public SearchContentResult setSuccess(Boolean success) {
        Success = success;
        return this;
    }

    private List<Datum> Data = new ArrayList<Datum>();
    private String Message;
    private Boolean Success;
}
