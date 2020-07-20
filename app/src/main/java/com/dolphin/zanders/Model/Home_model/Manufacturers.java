
package com.dolphin.zanders.Model.Home_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Manufacturers {

    @SerializedName("products_manufacturers")
    @Expose
    private List<ProductsManufacturer> productsManufacturers = null;
    @SerializedName("view")
    @Expose
    private String view;

    public List<ProductsManufacturer> getProductsManufacturers() {
        return productsManufacturers;
    }

    public void setProductsManufacturers(List<ProductsManufacturer> productsManufacturers) {
        this.productsManufacturers = productsManufacturers;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

}
