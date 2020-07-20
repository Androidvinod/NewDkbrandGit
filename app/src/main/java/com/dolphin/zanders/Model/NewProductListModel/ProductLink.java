
package com.dolphin.zanders.Model.NewProductListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductLink {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("link_type")
    @Expose
    private String linkType;
    @SerializedName("linked_product_sku")
    @Expose
    private String linkedProductSku;
    @SerializedName("linked_product_type")
    @Expose
    private String linkedProductType;
    @SerializedName("position")
    @Expose
    private Integer position;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkedProductSku() {
        return linkedProductSku;
    }

    public void setLinkedProductSku(String linkedProductSku) {
        this.linkedProductSku = linkedProductSku;
    }

    public String getLinkedProductType() {
        return linkedProductType;
    }

    public void setLinkedProductType(String linkedProductType) {
        this.linkedProductType = linkedProductType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
