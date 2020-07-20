
package com.dolphin.zanders.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("label")
    @Expose
    private String label;

    public Country(String label, String values) {
        this.label = label;
        this.values = values;
    }

    @SerializedName("values")
    @Expose
    private String values;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

}
