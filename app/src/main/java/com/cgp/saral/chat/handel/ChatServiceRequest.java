package com.cgp.saral.chat.handel;

import com.cgp.saral.myutils.Constants;

/**
 * Created by WeeSync on 23/10/15.
 */
public class ChatServiceRequest {

    public Constants.ServiceLevel getType() {
        return type;
    }

    public ChatServiceRequest setType(Constants.ServiceLevel type) {
        this.type = type;
        return this;
    }

    public String getConclusion() {
        return conclusion;
    }

    public ChatServiceRequest setConclusion(String conclusion) {
        this.conclusion = conclusion;
        return this;
    }

    private Constants.ServiceLevel type;
    private String conclusion = null;
}
