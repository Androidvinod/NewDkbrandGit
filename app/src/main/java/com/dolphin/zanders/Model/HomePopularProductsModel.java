package com.dolphin.zanders.Model;

public class HomePopularProductsModel {

        String popular_product_availability,popular_product_image,popular_product_name,popular_product_item_number,popular_product_upc,popular_product_msrp;

    public HomePopularProductsModel(String popular_product_availability, String popular_product_image, String popular_product_name, String popular_product_item_number, String popular_product_upc, String popular_product_msrp) {
        this.popular_product_availability = popular_product_availability;
        this.popular_product_image = popular_product_image;
        this.popular_product_name = popular_product_name;
        this.popular_product_item_number = popular_product_item_number;
        this.popular_product_upc = popular_product_upc;
        this.popular_product_msrp = popular_product_msrp;
    }

    public String getPopular_product_availability() {
        return popular_product_availability;
    }

    public void setPopular_product_availability(String popular_product_availability) {
        this.popular_product_availability = popular_product_availability;
    }

    public String getPopular_product_image() {
        return popular_product_image;
    }

    public void setPopular_product_image(String popular_product_image) {
        this.popular_product_image = popular_product_image;
    }

    public String getPopular_product_name() {
        return popular_product_name;
    }

    public void setPopular_product_name(String popular_product_name) {
        this.popular_product_name = popular_product_name;
    }

    public String getPopular_product_item_number() {
        return popular_product_item_number;
    }

    public void setPopular_product_item_number(String popular_product_item_number) {
        this.popular_product_item_number = popular_product_item_number;
    }

    public String getPopular_product_upc() {
        return popular_product_upc;
    }

    public void setPopular_product_upc(String popular_product_upc) {
        this.popular_product_upc = popular_product_upc;
    }

    public String getPopular_product_msrp() {
        return popular_product_msrp;
    }

    public void setPopular_product_msrp(String popular_product_msrp) {
        this.popular_product_msrp = popular_product_msrp;
    }
}
