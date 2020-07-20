
package com.dolphin.zanders.Model.ProductDetailModelNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryLink {

    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("category_id")
    @Expose
    private String categoryId;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
