package com.cgp.saral.model;

/**
 * Created by WeeSync on 17/10/15.
 */
public class ContentStats {

    public String getDisLikes() {
        return DisLikes;
    }

    public ContentStats setDisLikes(String disLikes) {
        DisLikes = disLikes;
        return this;
    }

    public String getLikes() {
        return Likes;
    }

    public ContentStats setLikes(String likes) {
        Likes = likes;
        return this;
    }

    private String DisLikes;
    private String     Likes;
}
