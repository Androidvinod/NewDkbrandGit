
package com.dolphin.zanders.Model.NewProductListModel;

import java.util.Collections;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CustomAttribute {

    @SerializedName("attribute_code")
    @Expose
    private String attributeCode;
    @SerializedName("value")
    @Expose

    // private List<String> value = null;
    private String value = null;

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

  /*  public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
*/


    public String getValuee() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getValue() {
        return Collections.singletonList(value);
    }

    public void setValue(List<String> value) {
        this.value = String.valueOf(value);
    }
}
