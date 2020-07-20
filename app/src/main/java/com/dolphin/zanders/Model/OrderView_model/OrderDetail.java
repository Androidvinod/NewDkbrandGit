
package com.dolphin.zanders.Model.OrderView_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;


    @SerializedName("tax_amount")
    @Expose
    private String tax_amount;

    @SerializedName("shipping_charges")

    @Expose
    private String shippingCharges;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(String shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }
    public String getTax_amount() {
        return tax_amount;
    }
    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

}
