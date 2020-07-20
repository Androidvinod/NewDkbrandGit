
package com.dolphin.zanders.Model.Filter_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFilterlist {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("filter")
    @Expose
    private List<Filter> filter = null;
    @SerializedName("sort_list")
    @Expose
    private SortList sortList;
    @SerializedName("default_sort")
    @Expose
    private String defaultSort;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Filter> getFilter() {
        return filter;
    }

    public void setFilter(List<Filter> filter) {
        this.filter = filter;
    }

    public SortList getSortList() {
        return sortList;
    }

    public void setSortList(SortList sortList) {
        this.sortList = sortList;
    }

    public String getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(String defaultSort) {
        this.defaultSort = defaultSort;
    }

}
