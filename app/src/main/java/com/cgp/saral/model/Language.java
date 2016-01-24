package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 30/10/15.
 */
public class Language implements Serializable{

    String id;
    String name;

    public String getName() {
        return name;
    }

    public Language setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public Language setId(String id) {
        this.id = id;
        return this;
    }


}
