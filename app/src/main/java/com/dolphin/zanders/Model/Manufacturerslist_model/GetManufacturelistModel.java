
package com.dolphin.zanders.Model.Manufacturerslist_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetManufacturelistModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("manufacturs")
    @Expose
    private List<Manufactur> manufacturs = null;
    @SerializedName("is_last")
    @Expose
    private Integer isLast;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Manufactur> getManufacturs() {
        return manufacturs;
    }

    public void setManufacturs(List<Manufactur> manufacturs) {
        this.manufacturs = manufacturs;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

}
