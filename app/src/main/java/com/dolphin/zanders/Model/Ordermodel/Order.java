
package com.dolphin.zanders.Model.Ordermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("increment_id")
    @Expose
    private String incrementId;

    public Order(String incrementId, String status,
                 String createdAt, String shipTo, String grandTotal) {
        this.incrementId = incrementId;
        this.status = status;
        this.createdAt = createdAt;
        this.shipTo = shipTo;
        this.grandTotal = grandTotal;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("ship_to")
    @Expose
    private String shipTo;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;

    public String getIncrementId() {
        return incrementId;
    }

    public void setIncrementId(String incrementId) {
        this.incrementId = incrementId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

}
