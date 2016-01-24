package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 30/10/15.
 */
public class LocationData implements Serializable{

    public com.cgp.saral.model.GetAllLocationsResult getGetAllLocationsResult() {
        return GetAllLocationsResult;
    }

    public LocationData setGetAllLocationsResult(com.cgp.saral.model.GetAllLocationsResult getAllLocationsResult) {
        GetAllLocationsResult = getAllLocationsResult;
        return this;
    }

    GetAllLocationsResult GetAllLocationsResult;
}
