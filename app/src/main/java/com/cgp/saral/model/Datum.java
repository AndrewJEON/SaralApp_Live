package com.cgp.saral.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Datum implements Serializable{

    private String ApprovalDate;
    private String ApprovedBy;
    private List<ContentDetail> ContentDetails = new ArrayList<ContentDetail>();
    private String CreatedBy;
    private String CreatedDate;
    private String Description;
    private String FeaturesTag;
    private String Id;
    private String Language;
    private String LastUpdatedBy;
    private String LastUpdatedDate;
    private String Status;
    private String Summary;

    private String YouDisliked;
    private String YouLiked;

    public String getYouDisliked() {
        return YouDisliked;
    }

    public Datum setYouDisliked(String youDisliked) {
        YouDisliked = youDisliked;
        return this;
    }

    public String getYouLiked() {
        return YouLiked;
    }

    public Datum setYouLiked(String youLiked) {
        YouLiked = youLiked;
        return this;
    }


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();




    public boolean isLiked() {
        return isLiked;
    }

    public Datum setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
        return this;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public Datum setIsDisliked(boolean isDisliked) {
        this.isDisliked = isDisliked;
        return this;
    }

    private boolean isLiked;
    private boolean isDisliked;

    public com.cgp.saral.model.ContentStats getContentStats() {
        return ContentStats;
    }

    public Datum setContentStats(com.cgp.saral.model.ContentStats contentStats) {
        ContentStats = contentStats;
        return this;
    }

    private ContentStats ContentStats;

    /**
     *
     * @return
     * The ApprovalDate
     */
    public String getApprovalDate() {
        return ApprovalDate;
    }

    /**
     *
     * @param ApprovalDate
     * The ApprovalDate
     */
    public void setApprovalDate(String ApprovalDate) {
        this.ApprovalDate = ApprovalDate;
    }

    /**
     *
     * @return
     * The ApprovedBy
     */
    public String getApprovedBy() {
        return ApprovedBy;
    }

    /**
     *
     * @param ApprovedBy
     * The ApprovedBy
     */
    public void setApprovedBy(String ApprovedBy) {
        this.ApprovedBy = ApprovedBy;
    }

    /**
     *
     * @return
     * The ContentDetails
     */
    public List<ContentDetail> getContentDetails() {
        return ContentDetails;
    }

    /**
     *
     * @param ContentDetails
     * The ContentDetails
     */
    public void setContentDetails(List<ContentDetail> ContentDetails) {
        this.ContentDetails = ContentDetails;
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
     * The Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     *
     * @param Description
     * The Description
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     *
     * @return
     * The FeaturesTag
     */
    public String getFeaturesTag() {
        return FeaturesTag;
    }

    /**
     *
     * @param FeaturesTag
     * The FeaturesTag
     */
    public void setFeaturesTag(String FeaturesTag) {
        this.FeaturesTag = FeaturesTag;
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
     * The Language
     */
    public String getLanguage() {
        return Language;
    }

    /**
     *
     * @param Language
     * The Language
     */
    public void setLanguage(String Language) {
        this.Language = Language;
    }

    /**
     *
     * @return
     * The LastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return LastUpdatedBy;
    }

    /**
     *
     * @param LastUpdatedBy
     * The LastUpdatedBy
     */
    public void setLastUpdatedBy(String LastUpdatedBy) {
        this.LastUpdatedBy = LastUpdatedBy;
    }

    /**
     *
     * @return
     * The LastUpdatedDate
     */
    public String getLastUpdatedDate() {
        return LastUpdatedDate;
    }

    /**
     *
     * @param LastUpdatedDate
     * The LastUpdatedDate
     */
    public void setLastUpdatedDate(String LastUpdatedDate) {
        this.LastUpdatedDate = LastUpdatedDate;
    }

    /**
     *
     * @return
     * The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     *
     * @param Status
     * The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     *
     * @return
     * The Summary
     */
    public String getSummary() {
        return Summary;
    }

    /**
     *
     * @param Summary
     * The Summary
     */
    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}