
package com.dolphin.zanders.Model.Home_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsDaily {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("item_number")
    @Expose
    private String itemNumber;
    @SerializedName("msrp")
    @Expose
    private String msrp;

    @SerializedName("product_price")
    @Expose
    private String product_price;
    @SerializedName("product_specialprice")
    @Expose
    private String product_specialprice;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("upc")
    @Expose
    private String upc;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("in_stock")
    @Expose
    private String inStock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_specialprice() {
        return product_specialprice;
    }

    public void setProduct_specialprice(String product_specialprice) {
        this.product_specialprice = product_specialprice;
    }

}
