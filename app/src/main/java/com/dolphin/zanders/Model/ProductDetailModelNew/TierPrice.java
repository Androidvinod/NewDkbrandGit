
package com.dolphin.zanders.Model.ProductDetailModelNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TierPrice {

    @SerializedName("customer_group_id")
    @Expose
    private Integer customerGroupId;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes_ extensionAttributes;

    public Integer getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(Integer customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public ExtensionAttributes_ getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes_ extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

}
