
package com.dolphin.zanders.Model.Home_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularProducts {

    @SerializedName("products_topsellers")
    @Expose
    private List<ProductsTopseller> productsTopsellers = null;
    @SerializedName("view")
    @Expose
    private String view;

    public List<ProductsTopseller> getProductsTopsellers() {
        return productsTopsellers;
    }

    public void setProductsTopsellers(List<ProductsTopseller> productsTopsellers) {
        this.productsTopsellers = productsTopsellers;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

}
