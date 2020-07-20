
package com.dolphin.zanders.Model.ProductDetailModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("item_number")
    @Expose
    private String itemNumber;
    @SerializedName("msrp")
    @Expose
    private String msrp;
    @SerializedName("product_specialprice")
    @Expose
    private String product_specialprice;

    @SerializedName("product_price")
    @Expose
    private String product_price;
    @SerializedName("map")
    @Expose
    private String map;

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("features")
    @Expose
    private String features;
    @SerializedName("quick_overview")
    @Expose
    private String quickOverview;
    @SerializedName("warning")
    @Expose
    private String warning;
    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("available_qty")
    @Expose
    private String available_qty;

    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("no_image")
    @Expose
    private String productThumbnail;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;
    @SerializedName("additional")
    @Expose
    private List<Additional> additional = null;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getQuickOverview() {
        return quickOverview;
    }

    public void setQuickOverview(String quickOverview) {
        this.quickOverview = quickOverview;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }

    public List<Additional> getAdditional() {
        return additional;
    }

    public void setAdditional(List<Additional> additional) {
        this.additional = additional;
    }
    public String getProduct_specialprice() {
        return product_specialprice;
    }

    public void setProduct_specialprice(String product_specialprice) {
        this.product_specialprice = product_specialprice;
    }
    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
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
