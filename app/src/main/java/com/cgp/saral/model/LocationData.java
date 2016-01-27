package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 30/10/15.
 */
public class LocationData implements Serializable{

    public GetAllLocationsResult getGetAllLocationsResult() {
        return GetAllLocationsResult;
    }

    public LocationData setGetAllLocationsResult(GetAllLocationsResult getAllLocationsResult) {
        GetAllLocationsResult = getAllLocationsResult;
        return this;
    }

    GetAllLocationsResult GetAllLocationsResult;
}
