
package com.dolphin.zanders.Model.Sortlist_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSortModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("filter")
    @Expose
    private Object filter;
    @SerializedName("sort_list")
    @Expose
    private List<SortListSort> sortList = null;
    @SerializedName("default_sort")
    @Expose
    private DefaultSort defaultSort;

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

    public Object getFilter() {
        return filter;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

    public List<SortListSort> getSortList() {
        return sortList;
    }

    public void setSortList(List<SortListSort> sortList) {
        this.sortList = sortList;
    }

    public DefaultSort getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(DefaultSort defaultSort) {
        this.defaultSort = defaultSort;
    }

}
