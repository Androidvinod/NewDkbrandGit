
package com.dolphin.zanders.Model.NewOrderDetailModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingAssignment {

    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("items")
    @Expose
    private List<Item_> items = null;

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public List<Item_> getItems() {
        return items;
    }

    public void setItems(List<Item_> items) {
        this.items = items;
    }

}
