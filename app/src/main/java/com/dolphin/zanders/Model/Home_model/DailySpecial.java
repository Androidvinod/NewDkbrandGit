
package com.dolphin.zanders.Model.Home_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailySpecial {

    @SerializedName("products_daily")
    @Expose
    private List<ProductsDaily> productsDaily = null;
    @SerializedName("view")
    @Expose
    private String view;

    public List<ProductsDaily> getProductsDaily() {
        return productsDaily;
    }

    public void setProductsDaily(List<ProductsDaily> productsDaily) {
        this.productsDaily = productsDaily;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

}
