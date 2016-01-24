package com.cgp.saral.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by WeeSync on 30/10/15.
 */
public class GetAllLocationsResult implements Serializable{

    public ArrayList<LocationItems> getData() {
        return Data;
    }

    public GetAllLocationsResult setData(ArrayList<LocationItems> data) {
        Data = data;
        return this;
    }

    ArrayList<LocationItems> Data;

}
