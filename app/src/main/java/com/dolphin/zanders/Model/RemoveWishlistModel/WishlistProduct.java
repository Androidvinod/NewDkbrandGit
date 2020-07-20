
package com.dolphin.zanders.Model.RemoveWishlistModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishlistProduct {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_sku")
    @Expose
    private String productSku;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_specialprice")
    @Expose
    private String productSpecialprice;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_thumbnail")
    @Expose
    private String productThumbnail;
    @SerializedName("is_configurable")
    @Expose
    private Integer isConfigurable;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSpecialprice() {
        return productSpecialprice;
    }

    public void setProductSpecialprice(String productSpecialprice) {
        this.productSpecialprice = productSpecialprice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public Integer getIsConfigurable() {
        return isConfigurable;
    }

    public void setIsConfigurable(Integer isConfigurable) {
        this.isConfigurable = isConfigurable;
    }

}
