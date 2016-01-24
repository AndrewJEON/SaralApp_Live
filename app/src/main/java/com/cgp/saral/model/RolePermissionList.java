package com.cgp.saral.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WeeSync on 06/10/15.
 */
public class RolePermissionList {
    private String Add;
    private String CreatedBy;
    private String CreatedByName;
    private String CreatedDate;
    private String Delete;
    private String Edit;
    private String Id;
    private String PermissionId;
    private String PermissionName;
    private String RoleId;
    private String RoleName;
    private String View;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The Add
     */
    public String getAdd() {
        return Add;
    }

    /**
     *
     * @param Add
     * The Add
     */
    public void setAdd(String Add) {
        this.Add = Add;
    }

    /**
     *
     * @return
     * The CreatedBy
     */
    public String getCreatedBy() {
        return CreatedBy;
    }

    /**
     *
     * @param CreatedBy
     * The CreatedBy
     */
    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    /**
     *
     * @return
     * The CreatedByName
     */
    public String getCreatedByName() {
        return CreatedByName;
    }

    /**
     *
     * @param CreatedByName
     * The CreatedByName
     */
    public void setCreatedByName(String CreatedByName) {
        this.CreatedByName = CreatedByName;
    }

    /**
     *
     * @return
     * The CreatedDate
     */
    public String getCreatedDate() {
        return CreatedDate;
    }

    /**
     *
     * @param CreatedDate
     * The CreatedDate
     */
    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    /**
     *
     * @return
     * The Delete
     */
    public String getDelete() {
        return Delete;
    }

    /**
     *
     * @param Delete
     * The Delete
     */
    public void setDelete(String Delete) {
        this.Delete = Delete;
    }

    /**
     *
     * @return
     * The Edit
     */
    public String getEdit() {
        return Edit;
    }

    /**
     *
     * @param Edit
     * The Edit
     */
    public void setEdit(String Edit) {
        this.Edit = Edit;
    }

    /**
     *
     * @return
     * The Id
     */
    public String getId() {
        return Id;
    }

    /**
     *
     * @param Id
     * The Id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     *
     * @return
     * The PermissionId
     */
    public String getPermissionId() {
        return PermissionId;
    }

    /**
     *
     * @param PermissionId
     * The PermissionId
     */
    public void setPermissionId(String PermissionId) {
        this.PermissionId = PermissionId;
    }

    /**
     *
     * @return
     * The PermissionName
     */
    public String getPermissionName() {
        return PermissionName;
    }

    /**
     *
     * @param PermissionName
     * The PermissionName
     */
    public void setPermissionName(String PermissionName) {
        this.PermissionName = PermissionName;
    }

    /**
     *
     * @return
     * The RoleId
     */
    public String getRoleId() {
        return RoleId;
    }

    /**
     *
     * @param RoleId
     * The RoleId
     */
    public void setRoleId(String RoleId) {
        this.RoleId = RoleId;
    }

    /**
     *
     * @return
     * The RoleName
     */
    public String getRoleName() {
        return RoleName;
    }

    /**
     *
     * @param RoleName
     * The RoleName
     */
    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }

    /**
     *
     * @return
     * The View
     */
    public String getView() {
        return View;
    }

    /**
     *
     * @param View
     * The View
     */
    public void setView(String View) {
        this.View = View;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

