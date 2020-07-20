
package com.dolphin.zanders.Model.CartlistModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartlistInnerData {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_thumbnail")
    @Expose
    private String productThumbnail;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("item_number")
    @Expose
    private String itemNumber;
    @SerializedName("product_qty")
    @Expose
    private Integer productQty;
    @SerializedName("upc")
    @Expose
    private String upc;
    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("msrp")
    @Expose
    private String msrp;
    @SerializedName("row_total")
    @Expose
    private String rowTotal;
    @SerializedName("in_cart")
    @Expose
    private Integer inCart;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getProductQty() {
        return productQty;
    }

    public void setProductQty(Integer productQty) {
        this.productQty = productQty;
    }

        public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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

    public String getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(String rowTotal) {
        this.rowTotal = rowTotal;
    }

    public Integer getInCart() {
        return inCart;
    }

    public void setInCart(Integer inCart) {
        this.inCart = inCart;
    }

}
