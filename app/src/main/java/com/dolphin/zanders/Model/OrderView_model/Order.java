
package com.dolphin.zanders.Model.OrderView_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("increment_id")
    @Expose
    private String incrementId;
    @SerializedName("order_detail")
    @Expose
    private OrderDetail orderDetail;
    @SerializedName("Shipping Address")
    @Expose
    private ShippingAddress shippingAddress;
    @SerializedName("Billing Address")
    @Expose
    private BillingAddress billingAddress;
    @SerializedName("Delivery Method")
    @Expose
    private DeliveryMethod deliveryMethod;
    @SerializedName("Payment Method")
    @Expose
    private PaymentMethod paymentMethod;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;

    public String getIncrementId() {
        return incrementId;
    }

    public void setIncrementId(String incrementId) {
        this.incrementId = incrementId;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

}
