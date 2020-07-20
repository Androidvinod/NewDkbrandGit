
package com.dolphin.zanders.Model.Shipingmethod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingMethod {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("carrier_title")
    @Expose
    private String carrierTitle;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarrierTitle() {
        return carrierTitle;
    }

    public void setCarrierTitle(String carrierTitle) {
        this.carrierTitle = carrierTitle;
    }

}
