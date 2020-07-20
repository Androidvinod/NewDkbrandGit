
package com.dolphin.zanders.Model.ProductDetailModelNew;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterGroup {

    @SerializedName("filters")
    @Expose
    private List<Filter> filters = null;

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

}
