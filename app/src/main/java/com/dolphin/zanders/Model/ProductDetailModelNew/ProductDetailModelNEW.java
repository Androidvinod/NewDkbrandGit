
package com.dolphin.zanders.Model.ProductDetailModelNew;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailModelNEW {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("search_criteria")
    @Expose
    private SearchCriteria searchCriteria;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
