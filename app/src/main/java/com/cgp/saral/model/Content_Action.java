package com.cgp.saral.model;

/**
 * Created by WeeSync on 03/11/15.
 */
public class Content_Action {

    String strContentId;
    String strLiked;

    public String getStrContentId() {
        return strContentId;
    }

    public Content_Action setStrContentId(String strContentId) {
        this.strContentId = strContentId;
        return this;
    }

    public String getStrDisliked() {
        return strDisliked;
    }

    public Content_Action setStrDisliked(String strDisliked) {
        this.strDisliked = strDisliked;
        return this;
    }

    public String getStrLiked() {
        return strLiked;
    }

    public Content_Action setStrLiked(String strLiked) {
        this.strLiked = strLiked;
        return this;
    }

    String strDisliked;
}
