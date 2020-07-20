
package com.dolphin.zanders.Model.Shipingmethod;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingMethodModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("shipping_method")
    @Expose
    private List<ShippingMethod> shippingMethod = null;

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

    public List<ShippingMethod> getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(List<ShippingMethod> shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

}
