
package com.dolphin.zanders.Model.Homedk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileHomeCategory {

    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("category_image_alt")
    @Expose
    private String categoryImageAlt;
    @SerializedName("category_text")
    @Expose
    private String categoryText;
    @SerializedName("category_id")
    @Expose
    private String categoryId;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryImageAlt() {
        return categoryImageAlt;
    }

    public void setCategoryImageAlt(String categoryImageAlt) {
        this.categoryImageAlt = categoryImageAlt;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
