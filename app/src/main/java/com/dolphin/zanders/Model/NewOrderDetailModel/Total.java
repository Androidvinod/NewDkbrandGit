
package com.dolphin.zanders.Model.NewOrderDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Total {

    @SerializedName("base_shipping_amount")
    @Expose
    private Integer baseShippingAmount;
    @SerializedName("shipping_amount")
    @Expose
    private Integer shippingAmount;

    public Integer getBaseShippingAmount() {
        return baseShippingAmount;
    }

    public void setBaseShippingAmount(Integer baseShippingAmount) {
        this.baseShippingAmount = baseShippingAmount;
    }

    public Integer getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Integer shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

}
