package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 30/10/15.
 */
public class LocationItems implements Serializable {

    String CreatedDate;
    String Id;
    String Name;
    String ParentId;
    String ParentName;
    String TypeId;
    String TypeName;

    public String getCreatedDate() {
        return CreatedDate;
    }

    public LocationItems setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
        return this;
    }

    public String getId() {
        return Id;
    }

    public LocationItems setId(String id) {
        Id = id;
        return this;
    }

    public String getName() {
        return Name;
    }

    public LocationItems setName(String name) {
        Name = name;
        return this;
    }

    public String getParentId() {
        return ParentId;
    }

    public LocationItems setParentId(String parentId) {
        ParentId = parentId;
        return this;
    }

    public String getParentName() {
        return ParentName;
    }

    public LocationItems setParentName(String parentName) {
        ParentName = parentName;
        return this;
    }

    public String getTypeId() {
        return TypeId;
    }

    public LocationItems setTypeId(String typeId) {
        TypeId = typeId;
        return this;
    }

    public String getTypeName() {
        return TypeName;
    }

    public LocationItems setTypeName(String typeName) {
        TypeName = typeName;
        return this;
    }


}
