
package com.dolphin.zanders.Model.Homedk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileHomeBanner {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_alt")
    @Expose
    private String imageAlt;
    @SerializedName("image_text")
    @Expose
    private String imageText;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

}
