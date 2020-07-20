
package com.dolphin.zanders.Model.RemoveProductFomCart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveCartProductModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("quote_id")
    @Expose
    private String quoteId;
    @SerializedName("items_count")
    @Expose
    private Integer itemsCount;
    @SerializedName("items_qty")
    @Expose
    private Integer itemsQty;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("delivery_fee")
    @Expose
    private String deliveryFee;

    @SerializedName("Additional Amount Needed for Free Shipping")
    @Expose
    private String additionalAmountNeededForFreeShipping;
    @SerializedName("discount_label")
    @Expose
    private String discountLabel;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("product")
    @Expose
    private List<RemoveProduct> product = null;

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

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public Integer getItemsQty() {
        return itemsQty;
    }

    public void setItemsQty(Integer itemsQty) {
        this.itemsQty = itemsQty;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<RemoveProduct> getProduct() {
        return product;
    }

    public void setProduct(List<RemoveProduct> product) {
        this.product = product;
    }
    public String getAdditionalAmountNeededForFreeShipping() {
        return additionalAmountNeededForFreeShipping;
    }
    public void setAdditionalAmountNeededForFreeShipping(String additionalAmountNeededForFreeShipping) {
        this.additionalAmountNeededForFreeShipping = additionalAmountNeededForFreeShipping;
    }
}
