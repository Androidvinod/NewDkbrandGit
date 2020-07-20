
package com.dolphin.zanders.Model.Checkout_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Totals {

    @SerializedName("Shippable Subtotal")
    @Expose
    private String shippableSubtotal;
    @SerializedName("Additional Amount Needed for Free Shipping")
    @Expose
    private String additionalAmountNeededForFreeShipping;
    @SerializedName("Subtotal")
    @Expose
    private String subtotal;
    @SerializedName("Shipping And Handling Name")
    @Expose
    private String shippingAndHandlingName;
    @SerializedName("Shipping And Handling")
    @Expose
    private String shippingAndHandling;
    @SerializedName("Grand Total")
    @Expose
    private String grandTotal;
    @SerializedName("Shippable Grand Total")
    @Expose
    private String shippableGrandTotal;

    public String getShippableSubtotal() {
        return shippableSubtotal;
    }

    public void setShippableSubtotal(String shippableSubtotal) {
        this.shippableSubtotal = shippableSubtotal;
    }

    public String getAdditionalAmountNeededForFreeShipping() {
        return additionalAmountNeededForFreeShipping;
    }

    public void setAdditionalAmountNeededForFreeShipping(String additionalAmountNeededForFreeShipping) {
        this.additionalAmountNeededForFreeShipping = additionalAmountNeededForFreeShipping;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getShippingAndHandlingName() {
        return shippingAndHandlingName;
    }

    public void setShippingAndHandlingName(String shippingAndHandlingName) {
        this.shippingAndHandlingName = shippingAndHandlingName;
    }

    public String getShippingAndHandling() {
        return shippingAndHandling;
    }

    public void setShippingAndHandling(String shippingAndHandling) {
        this.shippingAndHandling = shippingAndHandling;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getShippableGrandTotal() {
        return shippableGrandTotal;
    }

    public void setShippableGrandTotal(String shippableGrandTotal) {
        this.shippableGrandTotal = shippableGrandTotal;
    }

}
