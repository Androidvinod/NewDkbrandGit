
package com.dolphin.zanders.Model.Sortlist_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SortListSort {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("label")
    @Expose
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
