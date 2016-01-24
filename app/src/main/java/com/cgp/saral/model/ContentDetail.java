package com.cgp.saral.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class ContentDetail implements Serializable{

    private String ContentId;
    private String ContentType;
    private String MediaPath;
    private String MediaType;
    private String Status;
    private String ThumbnailPath;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The ContentId
     */
    public String getContentId() {
        return ContentId;
    }

    /**
     *
     * @param ContentId
     * The ContentId
     */
    public void setContentId(String ContentId) {
        this.ContentId = ContentId;
    }

    /**
     *
     * @return
     * The ContentType
     */
    public String getContentType() {
        return ContentType;
    }

    /**
     *
     * @param ContentType
     * The ContentType
     */
    public void setContentType(String ContentType) {
        this.ContentType = ContentType;
    }

    /**
     *
     * @return
     * The MediaPath
     */
    public String getMediaPath() {
        return MediaPath;
    }

    /**
     *
     * @param MediaPath
     * The MediaPath
     */
    public void setMediaPath(String MediaPath) {
        this.MediaPath = MediaPath;
    }

    /**
     *
     * @return
     * The MediaType
     */
    public String getMediaType() {
        return MediaType;
    }

    /**
     *
     * @param MediaType
     * The MediaType
     */
    public void setMediaType(String MediaType) {
        this.MediaType = MediaType;
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
     * The ThumbnailPath
     */
    public String getThumbnailPath() {
        return ThumbnailPath;
    }

    /**
     *
     * @param ThumbnailPath
     * The ThumbnailPath
     */
    public void setThumbnailPath(String ThumbnailPath) {
        this.ThumbnailPath = ThumbnailPath;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}