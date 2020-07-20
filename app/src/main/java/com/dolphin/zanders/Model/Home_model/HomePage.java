
package com.dolphin.zanders.Model.Home_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomePage {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("daily_special")
    @Expose
    private DailySpecial dailySpecial;
    @SerializedName("popular_products")
    @Expose
    private PopularProducts popularProducts;
    @SerializedName("manufacturers")
    @Expose
    private Manufacturers manufacturers;
    @SerializedName("vedio")
    @Expose
    private String vedio;
    @SerializedName("image_blocks_2")
    @Expose
    private List<String> imageBlocks2 = null;
    @SerializedName("static_image")
    @Expose
    private List<String> staticImage = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DailySpecial getDailySpecial() {
        return dailySpecial;
    }

    public void setDailySpecial(DailySpecial dailySpecial) {
        this.dailySpecial = dailySpecial;
    }

    public PopularProducts getPopularProducts() {
        return popularProducts;
    }

    public void setPopularProducts(PopularProducts popularProducts) {
        this.popularProducts = popularProducts;
    }

    public Manufacturers getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(Manufacturers manufacturers) {
        this.manufacturers = manufacturers;
    }

    public String getVedio() {
        return vedio;
    }

    public void setVedio(String vedio) {
        this.vedio = vedio;
    }

    public List<String> getImageBlocks2() {
        return imageBlocks2;
    }

    public void setImageBlocks2(List<String> imageBlocks2) {
        this.imageBlocks2 = imageBlocks2;
    }

    public List<String> getStaticImage() {
        return staticImage;
    }

    public void setStaticImage(List<String> staticImage) {
        this.staticImage = staticImage;
    }

}
