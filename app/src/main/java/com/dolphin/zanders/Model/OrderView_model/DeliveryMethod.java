
package com.dolphin.zanders.Model.OrderView_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryMethod {

    @SerializedName("shipping_description")
    @Expose
    private String shippingDescription;

    public String getShippingDescription() {
        return shippingDescription;
    }

    public void setShippingDescription(String shippingDescription) {
        this.shippingDescription = shippingDescription;
    }

}
