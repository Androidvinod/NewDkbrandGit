
package com.dolphin.zanders.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivacyModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("page_layout")
    @Expose
    private String pageLayout;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("content_heading")
    @Expose
    private String contentHeading;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("creation_time")
    @Expose
    private String creationTime;
    @SerializedName("update_time")
    @Expose
    private String updateTime;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("active")
    @Expose
    private Boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(String pageLayout) {
        this.pageLayout = pageLayout;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getContentHeading() {
        return contentHeading;
    }

    public void setContentHeading(String contentHeading) {
        this.contentHeading = contentHeading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
