
package com.dolphin.zanders.Model.OrderView_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMethod {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Ship/Pick Up Date")
    @Expose
    private String shipPickUpDate;
    @SerializedName("Purchase Order Number")
    @Expose
    private String purchaseOrderNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShipPickUpDate() {
        return shipPickUpDate;
    }

    public void setShipPickUpDate(String shipPickUpDate) {
        this.shipPickUpDate = shipPickUpDate;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

}
