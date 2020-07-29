
package com.dolphin.zanders.Model.NewOrderDetailModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewOrderDetailModel {

    @SerializedName("base_currency_code")
    @Expose
    private String baseCurrencyCode;
    @SerializedName("base_discount_amount")
    @Expose
    private Double baseDiscountAmount;
    @SerializedName("base_grand_total")
    @Expose
    private Double baseGrandTotal;
    @SerializedName("base_shipping_amount")
    @Expose
    private Double baseShippingAmount;
    @SerializedName("base_subtotal")
    @Expose
    private Double baseSubtotal;
    @SerializedName("base_tax_amount")
    @Expose
    private Double baseTaxAmount;
    @SerializedName("base_total_due")
    @Expose
    private Double baseTotalDue;
    @SerializedName("billing_address_id")
    @Expose
    private Double billingAddressId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("customer_firstname")
    @Expose
    private String customerFirstname;
    @SerializedName("customer_group_id")
    @Expose
    private Double customerGroupId;
    @SerializedName("customer_id")
    @Expose
    private Double customerId;
    @SerializedName("customer_lastname")
    @Expose
    private String customerLastname;
    @SerializedName("discount_amount")
    @Expose
    private Double discountAmount;
    @SerializedName("entity_id")
    @Expose
    private Double entityId;
    @SerializedName("grand_total")
    @Expose
    private Double grandTotal;
    @SerializedName("increment_id")
    @Expose
    private String incrementId;
    @SerializedName("protect_code")
    @Expose
    private String protectCode;
    @SerializedName("shipping_amount")
    @Expose
    private Double shippingAmount;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("subtotal")
    @Expose
    private Double subtotal;
    @SerializedName("subtotal_incl_tax")
    @Expose
    private Double subtotalInclTax;
    @SerializedName("tax_amount")
    @Expose
    private Double taxAmount;
    @SerializedName("total_due")
    @Expose
    private Double totalDue;
    @SerializedName("total_item_count")
    @Expose
    private Double totalItemCount;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("billing_address")
    @Expose
    private BillingAddress billingAddress;
    @SerializedName("payment")
    @Expose
    private Payment payment;
    @SerializedName("status_histories")
    @Expose
    private List<Object> statusHistories = null;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public Double getBaseDiscountAmount() {
        return baseDiscountAmount;
    }

    public void setBaseDiscountAmount(Double baseDiscountAmount) {
        this.baseDiscountAmount = baseDiscountAmount;
    }

    public Double getBaseGrandTotal() {
        return baseGrandTotal;
    }

    public void setBaseGrandTotal(Double baseGrandTotal) {
        this.baseGrandTotal = baseGrandTotal;
    }

    public Double getBaseShippingAmount() {
        return baseShippingAmount;
    }

    public void setBaseShippingAmount(Double baseShippingAmount) {
        this.baseShippingAmount = baseShippingAmount;
    }

    public Double getBaseSubtotal() {
        return baseSubtotal;
    }

    public void setBaseSubtotal(Double baseSubtotal) {
        this.baseSubtotal = baseSubtotal;
    }

    public Double getBaseTaxAmount() {
        return baseTaxAmount;
    }

    public void setBaseTaxAmount(Double baseTaxAmount) {
        this.baseTaxAmount = baseTaxAmount;
    }

    public Double getBaseTotalDue() {
        return baseTotalDue;
    }

    public void setBaseTotalDue(Double baseTotalDue) {
        this.baseTotalDue = baseTotalDue;
    }

    public Double getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Double billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerFirstname() {
        return customerFirstname;
    }

    public void setCustomerFirstname(String customerFirstname) {
        this.customerFirstname = customerFirstname;
    }

    public Double getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(Double customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public Double getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Double customerId) {
        this.customerId = customerId;
    }

    public String getCustomerLastname() {
        return customerLastname;
    }

    public void setCustomerLastname(String customerLastname) {
        this.customerLastname = customerLastname;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getEntityId() {
        return entityId;
    }

    public void setEntityId(Double entityId) {
        this.entityId = entityId;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getIncrementId() {
        return incrementId;
    }

    public void setIncrementId(String incrementId) {
        this.incrementId = incrementId;
    }

    public String getProtectCode() {
        return protectCode;
    }

    public void setProtectCode(String protectCode) {
        this.protectCode = protectCode;
    }

    public Double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getSubtotalInclTax() {
        return subtotalInclTax;
    }

    public void setSubtotalInclTax(Double subtotalInclTax) {
        this.subtotalInclTax = subtotalInclTax;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(Double totalDue) {
        this.totalDue = totalDue;
    }

    public Double getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Double totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Object> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<Object> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

}
