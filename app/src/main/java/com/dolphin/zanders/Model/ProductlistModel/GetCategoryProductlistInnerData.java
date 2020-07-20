
package com.dolphin.zanders.Model.ProductlistModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCategoryProductlistInnerData {

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
    @SerializedName("msrp")
    @Expose
    private String msrp;
    @SerializedName("product_specialprice")
    @Expose
    private String productSpecialprice;

    @SerializedName("map")
    @Expose
    private String map;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_thumbnail")
    @Expose
    private String productThumbnail;
    @SerializedName("wishlist")
    @Expose
    private String wishlist;
    @SerializedName("is_configurable")
    @Expose
    private Integer isConfigurable;

    //available_qty
    @SerializedName("available_qty")
    @Expose
    private String available_qty;

    @SerializedName("availability")
    @Expose
    private String availability;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public GetCategoryProductlistInnerData(String type, String productId,
                                           String productName, String productSku,
                                           String productPrice, String msrp,String map,
                                           String productSpecialprice, String productImage,
                                           String productThumbnail, String wishlist,String availability,String available_qty) {
        this.type = type;
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.productPrice = productPrice;
        this.msrp = msrp;
        this.map = map;
        this.productSpecialprice = productSpecialprice;
        this.productImage = productImage;
        this.productThumbnail = productThumbnail;
        this.wishlist = wishlist;
        this.availability = availability;
        this.available_qty = available_qty;
    }

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

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
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

    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    public Integer getIsConfigurable() {
        return isConfigurable;
    }

    public void setIsConfigurable(Integer isConfigurable) {
        this.isConfigurable = isConfigurable;
    }



    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getAvailable_qty() {
        return available_qty;
    }
    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }

}