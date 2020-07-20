package com.dolphin.zanders.Model;

public class WishlistModel {

        String wishlist_item_id,wishlist_id,product_id,sku,name,thumbnail;

    Double price,special_price;

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSpecial_price(Double special_price) {
        this.special_price = special_price;
    }

    public WishlistModel(String wishlist_item_id, String wishlist_id, String product_id,
                         String sku, Double price, Double special_price, String name, String thumbnail) {
        this.wishlist_item_id = wishlist_item_id;
        this.wishlist_id = wishlist_id;
        this.product_id = product_id;
        this.sku = sku;
        this.price = price;
        this.special_price = special_price;
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getWishlist_item_id() {
        return wishlist_item_id;
    }

    public void setWishlist_item_id(String wishlist_item_id) {
        this.wishlist_item_id = wishlist_item_id;
    }

    public String getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(String wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPrice() {
        return price;
    }



    public Double getSpecial_price() {
        return special_price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
