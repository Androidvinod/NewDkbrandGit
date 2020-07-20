
package com.dolphin.zanders.Model.NewOrderModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingAssignment {

    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("items")
    @Expose
    private List<Item__Cart_SubArray> items = null;

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public List<Item__Cart_SubArray> getItems() {
        return items;
    }

    public void setItems(List<Item__Cart_SubArray> items) {
        this.items = items;
    }

}
