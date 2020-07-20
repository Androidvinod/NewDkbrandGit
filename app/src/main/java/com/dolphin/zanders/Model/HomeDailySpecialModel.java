package com.dolphin.zanders.Model;

public class HomeDailySpecialModel {

    String product_availability,product_image,product_name,product_item_name,product_upc,product_msrp;

    public HomeDailySpecialModel(String product_availability, String product_image, String product_name, String product_item_name, String product_upc, String product_msrp) {
        this.product_availability = product_availability;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_item_name = product_item_name;
        this.product_upc = product_upc;
        this.product_msrp = product_msrp;
    }

    public String getProduct_availability() {
        return product_availability;
    }

    public void setProduct_availability(String product_availability) {
        this.product_availability = product_availability;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_item_name() {
        return product_item_name;
    }

    public void setProduct_item_name(String product_item_name) {
        this.product_item_name = product_item_name;
    }

    public String getProduct_upc() {
        return product_upc;
    }

    public void setProduct_upc(String product_upc) {
        this.product_upc = product_upc;
    }

    public String getProduct_msrp() {
        return product_msrp;
    }

    public void setProduct_msrp(String product_msrp) {
        this.product_msrp = product_msrp;
    }
}
