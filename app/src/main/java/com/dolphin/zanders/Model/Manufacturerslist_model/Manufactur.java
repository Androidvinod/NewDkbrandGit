
package com.dolphin.zanders.Model.Manufacturerslist_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Manufactur {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("serialized_display_on")
    @Expose
    private String serializedDisplayOn;
    @SerializedName("serialized_text")
    @Expose
    private String serializedText;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("web")
    @Expose
    private String web;
    @SerializedName("image_type")
    @Expose
    private String imageType;
    @SerializedName("enable")
    @Expose
    private String enable;
    @SerializedName("image")
    @Expose
    private String image;

    public Manufactur(String id, String name, String address, String serializedDisplayOn,
                      String serializedText, String phone, String web,
                      String imageType, String enable, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.serializedDisplayOn = serializedDisplayOn;
        this.serializedText = serializedText;
        this.phone = phone;
        this.web = web;
        this.imageType = imageType;
        this.enable = enable;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSerializedDisplayOn() {
        return serializedDisplayOn;
    }

    public void setSerializedDisplayOn(String serializedDisplayOn) {
        this.serializedDisplayOn = serializedDisplayOn;
    }

    public String getSerializedText() {
        return serializedText;
    }

    public void setSerializedText(String serializedText) {
        this.serializedText = serializedText;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
