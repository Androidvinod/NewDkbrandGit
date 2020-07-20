
package com.dolphin.zanders.Model.NewOrderDetailModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtensionAttributes {

    @SerializedName("shipping_assignments")
    @Expose
    private List<ShippingAssignment> shippingAssignments = null;
    @SerializedName("payment_additional_info")
    @Expose
    private List<Object> paymentAdditionalInfo = null;
    @SerializedName("applied_taxes")
    @Expose
    private List<Object> appliedTaxes = null;
    @SerializedName("item_applied_taxes")
    @Expose
    private List<Object> itemAppliedTaxes = null;

    public List<ShippingAssignment> getShippingAssignments() {
        return shippingAssignments;
    }

    public void setShippingAssignments(List<ShippingAssignment> shippingAssignments) {
        this.shippingAssignments = shippingAssignments;
    }

    public List<Object> getPaymentAdditionalInfo() {
        return paymentAdditionalInfo;
    }

    public void setPaymentAdditionalInfo(List<Object> paymentAdditionalInfo) {
        this.paymentAdditionalInfo = paymentAdditionalInfo;
    }

    public List<Object> getAppliedTaxes() {
        return appliedTaxes;
    }

    public void setAppliedTaxes(List<Object> appliedTaxes) {
        this.appliedTaxes = appliedTaxes;
    }

    public List<Object> getItemAppliedTaxes() {
        return itemAppliedTaxes;
    }

    public void setItemAppliedTaxes(List<Object> itemAppliedTaxes) {
        this.itemAppliedTaxes = itemAppliedTaxes;
    }

}
