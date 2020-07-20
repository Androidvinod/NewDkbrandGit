
package com.dolphin.zanders.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewCartListModel {

    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("qty")
    @Expose
    private Integer qty;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")

    @Expose
    private Integer price;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("quote_id")
    @Expose
    private String quoteId;


    String img;
    public NewCartListModel(Integer itemId, String sku, Integer qty, String name, Integer price, String productType, String quoteId,String img) {
        this.itemId = itemId;
        this.sku = sku;
        this.qty = qty;
        this.name = name;
        this.price = price;
        this.productType = productType;
        this.quoteId = quoteId;
        this.img = img;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

}
