
package com.dolphin.zanders.Model.Homedk;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Homedata {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("store name")
    @Expose
    private String storeName;
    @SerializedName("mobile_home_banner")
    @Expose
    private MobileHomeBanner mobileHomeBanner;
    @SerializedName("main_content")
    @Expose
    private String mainContent;
    @SerializedName("mobile_home_category")
    @Expose
    private List<MobileHomeCategory> mobileHomeCategory = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public MobileHomeBanner getMobileHomeBanner() {
        return mobileHomeBanner;
    }

    public void setMobileHomeBanner(MobileHomeBanner mobileHomeBanner) {
        this.mobileHomeBanner = mobileHomeBanner;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public List<MobileHomeCategory> getMobileHomeCategory() {
        return mobileHomeCategory;
    }

    public void setMobileHomeCategory(List<MobileHomeCategory> mobileHomeCategory) {
        this.mobileHomeCategory = mobileHomeCategory;
    }

}
