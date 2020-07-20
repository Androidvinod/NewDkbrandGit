package com.dolphin.zanders.Model;

public class TierPriceModel {
    String qty ,customer_group_id,value,discount;

    public TierPriceModel(String qty, String customer_group_id, String value, String discount) {
        this.qty = qty;
        this.customer_group_id = customer_group_id;
        this.value = value;
        this.discount = discount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCustomer_group_id() {
        return customer_group_id;
    }

    public void setCustomer_group_id(String customer_group_id) {
        this.customer_group_id = customer_group_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
