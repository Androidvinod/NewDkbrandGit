
package com.dolphin.zanders.Model.PaymentMethodModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMethod {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private String value;

    public String getLabel() {
        return label;
    }

    public PaymentMethod(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
