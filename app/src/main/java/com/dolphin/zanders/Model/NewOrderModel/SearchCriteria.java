
package com.dolphin.zanders.Model.NewOrderModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchCriteria {

    @SerializedName("filter_groups")
    @Expose
    private List<FilterGroup> filterGroups = null;

    public List<FilterGroup> getFilterGroups() {
        return filterGroups;
    }

    public void setFilterGroups(List<FilterGroup> filterGroups) {
        this.filterGroups = filterGroups;
    }

}
